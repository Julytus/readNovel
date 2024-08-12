package com.project.NovelWeb.configurations;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import com.project.NovelWeb.configurations.management.SecurityMetersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.project.NovelWeb.utils.SecurityUtils.JWT_ALGORITHM;

@Configuration
public class SecurityJwtConfig {
    @Value("${jwt.secretKey}")
    private String secretKey;
    private final Logger log = LoggerFactory.getLogger(SecurityJwtConfig.class);
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(secretKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    @Bean
    public JwtDecoder jwtDecoder(SecurityMetersService metersService) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(JWT_ALGORITHM)
                .build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                if (e.getMessage().contains("Invalid signature")) {
                    metersService.trackTokenInvalidSignature();
                } else if (e.getMessage().contains("Jwt expired at")) {
                    metersService.trackTokenExpired();
                } else if (
                        e.getMessage().contains("Invalid JWT serialization") ||
                                e.getMessage().contains("Malformed token") ||
                                e.getMessage().contains("Invalid unsecured/JWS/JWE")
                ) {
                    metersService.trackTokenMalformed();
                } else {
                    log.error("Unknown JWT error {}", e.getMessage());
                }
                throw e;
            }
        };
    }
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

}
