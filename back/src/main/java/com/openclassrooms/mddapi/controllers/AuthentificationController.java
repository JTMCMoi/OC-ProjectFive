package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.UserLoginDto;
import com.openclassrooms.mddapi.dto.UserRegisterDto;
import com.openclassrooms.mddapi.dto.TokenResponseDto;
import com.openclassrooms.mddapi.services.AuthenticationService;
import com.openclassrooms.mddapi.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthentificationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthentificationController(AuthenticationService authenticationService,
                                      UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto loginRequest) {
        log.debug("Authenticating user: {}", loginRequest.usernameOrEmail());

        try {
            TokenResponseDto token = authenticationService.authenticate(loginRequest);
            log.debug("User {} successfully authenticated", loginRequest.usernameOrEmail());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            log.error("Authentication failed for {}: {}", loginRequest.usernameOrEmail(), e.getMessage());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenResponseDto("Invalid username/email or password"));
        }
    }

    /**
     * REGISTER
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto registerRequest) {
        log.debug("Registering new user: {}", registerRequest.email());

        if (userService.existsByEmail(registerRequest.email())) {
            log.warn("Email {} is already taken.", registerRequest.email());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new TokenResponseDto( "Error: Email is already taken!"));
        }

        if (userService.existsByUserName(registerRequest.username())) {
            log.warn("Username {} is already taken.", registerRequest.username());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new TokenResponseDto("Error: Username is already taken!"));
        }

        userService.createUser(registerRequest);
        log.debug("User {} created successfully.", registerRequest.email());

        // Login automatique après inscription
        UserLoginDto loginRequest = new UserLoginDto(
                registerRequest.username(),
                registerRequest.password()
        );

        TokenResponseDto token = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(token);
    }
}
