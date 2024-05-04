package JSWD.Web.service.security;

import JSWD.Web.model.security.User;
import JSWD.Web.model.security.RegularToken;
import JSWD.Web.repositories.SecurityAuth.RefreshTokenRepository;
import JSWD.Web.repositories.SecurityAuth.RegularTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private RegularTokenRepository tokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${cardinal.app.jwtSecret}")
    private String jwtSecret;

    @Value("${cardinal.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public JwtService(RegularTokenRepository tokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.tokenRepository = tokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String generateToken(Authentication authentication, HttpServletRequest request) {
        final Map<String, Object> claims = new HashMap<>();

        final String ua = this.getUserAgent(request);
        final String ip = this.getClientIp(request);

        claims.put("User-Agent", ua);
        claims.put("X-FORWARDED-FOR", ip);

        return this.generateJwtToken(claims, authentication, 1);
    }

    public String generateToken(User userDetails, HttpServletRequest request) {
        final Map<String, Object> claims = new HashMap<>();

        final String ua = this.getUserAgent(request);
        final String ip = this.getClientIp(request);

        claims.put("User-Agent", ua);
        claims.put("X-FORWARDED-FOR", ip);

        return this.generateJwtToken(claims, userDetails, 1);
    }

    public String generateToken(User userDetails, HttpServletRequest request, int timeMultiplayer) {
        final Map<String, Object> claims = new HashMap<>();

        final String ua = this.getUserAgent(request);
        final String ip = this.getClientIp(request);

        claims.put("User-Agent", ua);
        claims.put("X-FORWARDED-FOR", ip);

        return this.generateJwtToken(claims, userDetails, timeMultiplayer);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, Authentication  authentication, int timeMultiplayer) {

        User userPrincipal = (User) authentication.getPrincipal();

        return this.generateJwtToken(extraClaims, userPrincipal, timeMultiplayer);
    }

    public String generateJwtToken(Map<String, Object> extraClaims, User userPrincipal, int timeMultiplayer) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs * timeMultiplayer))
                .signWith(SignatureAlgorithm.HS256, key())
                .compact();
    }

    public String getUserAgent(HttpServletRequest request) {
        String ua = "";
        if (request != null) {
            ua = request.getHeader("User-Agent");
        }
        return ua;
    }

    private String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String authToken, User userDetails, HttpServletRequest request) {
        try {
            Jwt token = Jwts.parser().setSigningKey(key()).parseClaimsJws(authToken);

            final String ua = this.getUserAgent(request);
            final String ip = this.getClientIp(request);
            final String username = getUserNameFromJwtToken(authToken);

            Optional<RegularToken> cachedToken = tokenRepository.findByToken(authToken);
            if (cachedToken.isEmpty() || cachedToken.get().isExpired() || cachedToken.get().isRevoked()) {
                if (cachedToken.isPresent()) {
                    RegularToken token1 = cachedToken.get();

                    token1.setRevoked(true);
                    token1.setRevokedTime(Instant.now());

                    if (this.isTokenExpired(authToken)) {
                        token1.setExpired(true);
                        token1.setExpiredTime(Instant.now());
                    }

                    tokenRepository.save(token1);
                }

                return false;
            }

            return username.equals(userDetails.getUsername()) && !this.isTokenExpired(authToken) &&
                    ua.equals(this.getUserAgent(authToken)) && ip.equals(this.getForwardedFor(authToken));
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public boolean isTokenExpired(String token) {
        try {
            return this.extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            logger.warn("Error parsing token", e);

            return true;
        }
    }

    public String getUserAgent(String token) {
        return (String) this.extractClaim(token, claims -> claims.get("User-Agent"));
    }

    public String getForwardedFor(String token) {
        return (String) this.extractClaim(token, claims -> claims.get("X-FORWARDED-FOR"));
    }


    public String getUserNameFromJwtToken(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(this.extractClaims(token));
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }


}
