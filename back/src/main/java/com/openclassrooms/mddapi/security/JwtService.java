package com.openclassrooms.mddapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.models.AppUserDetails;

@Service
public class JwtService {

    private final HmacJwtFactory jwtFactory;

    public JwtService(HmacJwtFactory jwtFactory) {
        this.jwtFactory = jwtFactory;
    }

    public String generateToken(Authentication authentication) {
        return jwtFactory.generateToken(authentication);
    }

    public Long extractUserId(String token) {
        return jwtFactory.extractUserId(token);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return extractUserId(token).equals(((AppUserDetails) userDetails).getId());
    }

}
