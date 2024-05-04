package JSWD.Web.service.security;

import JSWD.Web.comunication.*;
import JSWD.Web.exceptions.RefreshTokenNotFoundException;
import JSWD.Web.exceptions.RegularTokenNotFoundException;
import JSWD.Web.exceptions.TokenInvalidException;
import JSWD.Web.exceptions.TokenRevokedException;
import JSWD.Web.model.UserDetails;
import JSWD.Web.model.security.*;
import JSWD.Web.repositories.SecurityAuth.*;
import JSWD.Web.repositories.UserDetailsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AuthorizationService {
  @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private RegularTokenRepository regularTokenRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    public AuthorizationService authorizationService;


  public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, LogoutRequest logoutRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }

    String jwt = jwtService.parseJwt(request);
    User userDetails = userRepository.findByUsername(jwtService.getUserNameFromJwtToken(jwt)).get();
    if (!jwtService.isTokenValid(jwt, userDetails, request)) {
      throw new TokenInvalidException();
    }

    Optional<RegularToken> preToken = regularTokenRepository.findByToken(jwt);
    if (preToken.isEmpty()) {
      throw new RegularTokenNotFoundException();
    }

    RegularToken token = preToken.get();

    if (!token.isRevoked()) {
      token.setRevoked(true);
      token.setRevokedTime(Instant.now());
    }

    if (!token.isExpired()) {
      token.setExpired(true);
      token.setExpiredTime(Instant.now());
    }

    regularTokenRepository.save(token);

    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(logoutRequest.getRefreshToken());
    if (refreshTokenOptional.isEmpty()) {
      throw new RefreshTokenNotFoundException(logoutRequest.getRefreshToken());
    }

    RefreshToken oldRefreshToken = refreshTokenOptional.get();

    if (!oldRefreshToken.isRevoked()) {
      oldRefreshToken.setRevoked(true);
      oldRefreshToken.setRevokedTime(Instant.now());
    }

    if (!oldRefreshToken.isExpired()) {
      oldRefreshToken.setExpired(true);
      oldRefreshToken.setExpiredTime(Instant.now());
    }
    refreshTokenRepository.save(oldRefreshToken);

    return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
  }

  public ResponseEntity<?> login(HttpServletRequest request, LoginRequest loginRequest) {
    String login = loginRequest.getUsername().toLowerCase();
    String password = loginRequest.getPassword();

    if (loginRequest.getUsername().contains("@")) {
      Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getUsername());
      if (optionalUser.isPresent()) {
        login = optionalUser.get().getUsername().toLowerCase();
      }
    }

Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(login, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtService.generateToken(authentication, request);

    User userDetails = (User) authentication.getPrincipal();
    String refresh = jwtService.generateToken(userDetails, request, 10);
    List<String> roles = userDetails.getRole().stream()
            .map(role -> role.getName().name())
            .collect(Collectors.toList());

    Optional<User> user = userRepository.findByUsername(login);

    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    regularTokenRepository.save(new RegularToken(user.get().getUserCredentials(), jwt, Instant.now()));
    refreshTokenRepository.save(new RefreshToken(user.get().getUserCredentials(), refresh, Instant.now()));

    return ResponseEntity.ok(new JwtResponse(
            jwt,
            refresh,
            user.get().getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  public ResponseEntity<?> refreshToken(HttpServletRequest request, RefreshRequest refreshRequest) {
    String token = jwtService.parseJwt(request);

    String username0 = jwtService.getUserNameFromJwtToken(token);
    String username = jwtService.getUserNameFromJwtToken(refreshRequest.getRefreshToken());

    if (username.isEmpty() || username0.isEmpty() || !username0.equals(username)) {
      return ResponseEntity.badRequest().build();
    }

    User userDetails = userRepository.findByUsername(username).get();
    if (!jwtService.isTokenValid(refreshRequest.getRefreshToken(), userDetails, request)) {
      return ResponseEntity.badRequest().build();
    }

    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    Optional<RegularToken> realToken = regularTokenRepository.findByToken(token);
    if (realToken.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    if (realToken.get().isRevoked()) {
      return ResponseEntity.badRequest().build();
    }


    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    String jwt = jwtService.generateToken(userDetails, request);
    String newRefresh = jwtService.generateToken(userDetails, request);

    RegularToken oldToken = realToken.get();
    if (oldToken.isRevoked()) {
      throw new TokenRevokedException();
    }
    oldToken.setRevoked(true);
    oldToken.setRevokedTime(Instant.now());

    if (!oldToken.isExpired()) {
      oldToken.setExpired(true);
      oldToken.setExpiredTime(Instant.now());
    }
    regularTokenRepository.save(oldToken);
    regularTokenRepository.save(new RegularToken(user.get().getUserCredentials(), jwt, Instant.now()));

    Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(refreshRequest.getRefreshToken());
    if (refreshTokenOptional.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }

    RefreshToken oldRefreshToken = refreshTokenOptional.get();
    if (!oldRefreshToken.isRevoked()) {
      oldRefreshToken.setRevoked(true);
      oldRefreshToken.setRevokedTime(Instant.now());
    }

    if (!oldRefreshToken.isExpired()) {
      oldRefreshToken.setExpired(true);
      oldRefreshToken.setExpiredTime(Instant.now());
    }
    refreshTokenRepository.save(oldRefreshToken);
    refreshTokenRepository.save(new RefreshToken(user.get().getUserCredentials(), jwt, Instant.now()));

    return ResponseEntity.ok(new JwtResponse(
            jwt,
            newRefresh,
            user.get().getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }

  public ResponseEntity<?> register(RegisterRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername().toLowerCase())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByUsername(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    UserCredentials userCredentials = new UserCredentials(
            encoder.encode(signUpRequest.getPassword()),
            Instant.now()
    );

    if (!rolesRepository.existsByName(Roles.ERole.ROLE_USER)){
      rolesRepository.save(new Roles(Roles.ERole.ROLE_USER));
    }

  UserDetails userDetails = new UserDetails();
  Roles roles = rolesRepository.findByName(Roles.ERole.ROLE_USER);
    User user = new User(
            signUpRequest.getUsername().toLowerCase(),
            signUpRequest.getPassword(),
            signUpRequest.getEmail(),
            userDetails,
            userCredentials,
            roles

    );

    userCredentialsRepository.save(userCredentials);
    userDetailsRepository.save(userDetails);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User " + signUpRequest.getUsername() + " has been registered!"));
  }

  public ResponseEntity<?> verify() {
    return ResponseEntity.ok(new MessageResponse("Token ok!"));
  }

  public void revokeAllAccess(UserCredentials userCredentials) {
    this.revokeAllTokens(userCredentials);
    this.revokeAllRefreshTokens(userCredentials);
  }

  public void revokeAllTokens(UserCredentials userCredentials) {
    Collection<RegularToken> tokens = this.regularTokenRepository.findByUserCredentials(userCredentials);
    tokens.forEach(token -> {
      if (!token.isRevoked()) {
        token.setRevoked(true);
        token.setRevokedTime(Instant.now());
      }

      if (!token.isExpired()) {
        token.setExpired(true);
        token.setExpiredTime(Instant.now());
      }

      regularTokenRepository.save(token);
    });
  }


  //
  public void revokeAllRefreshTokens(UserCredentials userCredentials) {
    Collection<RefreshToken> tokens = this.refreshTokenRepository.findByUserCredentials(userCredentials).stream().toList();
    tokens.forEach(token -> {
      if (!token.isRevoked()) {
        token.setRevoked(true);
        token.setRevokedTime(Instant.now());
      }

      if (!token.isExpired()) {
        token.setExpired(true);
        token.setExpiredTime(Instant.now());
      }

      refreshTokenRepository.save(token);
    });
  }

}
