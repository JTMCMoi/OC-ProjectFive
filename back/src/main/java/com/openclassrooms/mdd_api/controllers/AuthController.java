package com.openclassrooms.mdd_api.controllers;

import com.openclassrooms.mdd_api.dto.user.GetUserResponseDto;
import com.openclassrooms.mdd_api.dto.user.UserAuthResponseDto;
import com.openclassrooms.mdd_api.dto.user.UserLoginRequestDto;
import com.openclassrooms.mdd_api.dto.user.UserRegisterRequestDto;
import com.openclassrooms.mdd_api.exception.ApiException;
import com.openclassrooms.mdd_api.service.implementations.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody final UserRegisterRequestDto user) {
        try {
            UserAuthResponseDto userAuthResponseDto = userService.registerUser(user);
            return ResponseEntity.ok().body(userAuthResponseDto);
        } catch (ApiException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody final UserLoginRequestDto user) {
        try {
            UserAuthResponseDto userAuthResponseDto = userService.loginUser(user);
            return ResponseEntity.ok().body(userAuthResponseDto);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(final JwtAuthenticationToken token) {
        try {
            GetUserResponseDto user = userService.getUser(token);
            return ResponseEntity.ok().body(user);
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }
    }
}
