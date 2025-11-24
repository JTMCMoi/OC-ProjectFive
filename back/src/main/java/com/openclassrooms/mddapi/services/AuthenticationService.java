package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.dto.UserLoginDto;
import com.openclassrooms.mddapi.dto.TokenResponseDto;
import com.openclassrooms.mddapi.exceptions.AuthenticatedUserNotFound;
import com.openclassrooms.mddapi.models.AppUserDetails;
import com.openclassrooms.mddapi.models.UserEntity;
import com.openclassrooms.mddapi.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service gérant l'authentification et la génération du token JWT.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenResponseDto authenticate(UserLoginDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            UserEntity user = userDetails.getUser();

            String token = jwtService.generateToken(authentication);

            return new TokenResponseDto(token);

        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Authentication failed: " + e.getMessage(), e);
        }
    }

    public TokenResponseDto generateToken(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        UserEntity user = userDetails.getUser();
            
        if (user == null) {
            throw new AuthenticationServiceException("Unable to find user for JWT generation.");
        }

        String token = jwtService.generateToken(authentication);

        return new TokenResponseDto(token);
    }

    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new AuthenticatedUserNotFound(
                    "No authenticated user found in Security Context",
                    "AuthenticationService.getAuthenticatedUserEmail"
            );
        }
    }
}