package JSWD.Web.controler;

import JSWD.Web.model.comunication.LoginRequest;
import JSWD.Web.model.comunication.LogoutRequest;
import JSWD.Web.model.comunication.RefreshRequest;
import JSWD.Web.model.comunication.RegisterRequest;
import JSWD.Web.service.security.AuthorizationService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthorizationController {
    @Autowired
    AuthorizationService service;
    public AuthorizationController(AuthorizationService service) {
        this.service = service;
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody LogoutRequest logoutRequest) {
        return service.logout(request, response, logoutRequest);
    }

    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest) {
        return service.login(request, loginRequest);
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {
        return service.register(signUpRequest);
    }
    @RolesAllowed("ROLE_USER")
    @PostMapping("/refresh-token")
    @ResponseBody
    public ResponseEntity<?> refreshToken(HttpServletRequest request, @Valid @RequestBody RefreshRequest refreshRequest) {
        return service.refreshToken(request, refreshRequest);
    }
    @RolesAllowed("ROLE_USER")
    @GetMapping("/verify")
    @ResponseBody
    public ResponseEntity<?> logoutUser() {
        return service.verify();
    }
}
