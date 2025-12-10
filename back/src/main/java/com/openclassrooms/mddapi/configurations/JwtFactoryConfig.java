package com.openclassrooms.mddapi.configurations;

import com.openclassrooms.mddapi.security.HmacJwtFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFactoryConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public HmacJwtFactory jwtFactory() {
        return new HmacJwtFactory(secret);
    }
}
