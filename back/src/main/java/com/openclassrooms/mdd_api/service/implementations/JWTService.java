package com.openclassrooms.mdd_api.service.implementations;

import com.openclassrooms.mdd_api.model.User;
import com.openclassrooms.mdd_api.service.interfaces.IJWTService;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Service
public class JWTService implements IJWTService {

    private final JwtEncoder jwtEncoder;

    public JWTService(final JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateToken(final User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("self").issuedAt(now).expiresAt(now.plus(1, ChronoUnit.DAYS)).subject(String.valueOf(user.getId())).build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

}