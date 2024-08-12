package com.project.NovelWeb.utils;

import com.nimbusds.jose.util.Base64;
import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.project.NovelWeb.utils.SecurityUtils.JWT_ALGORITHM;


@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration-access-token}")
    private int expirationAccessToken;
    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;
    @Value("${jwt.secretKey}")
    private String secretKey;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final TokenRepository tokenRepository;

    public String createAccessToken(com.project.NovelWeb.models.entities.User user) throws Exception{

        Instant now = Instant.now();
        Instant validity = now.plus(expirationAccessToken, ChronoUnit.SECONDS);
        try {
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuedAt(Instant.now())
                    .expiresAt(validity)
                    .subject(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("userId", user.getId())
                    .claim("role", user.getRole().getName())
                    .build();
            JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
            return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        } catch (Exception e) {
            throw new Exception("Cannot create jwt token, error :" + e.getMessage());
        }
    }
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(secretKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    public String createRefreshToken(com.project.NovelWeb.models.entities.User user) throws Exception{

        Instant now = Instant.now();
        Instant validity = now.plus(expirationRefreshToken, ChronoUnit.SECONDS);
        try {
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuedAt(Instant.now())
                    .expiresAt(validity)
                    .subject(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("userId", user.getId())
                    .build();

            JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
            return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        } catch (Exception e) {
            throw new Exception("Cannot create refresh token, error :" + e.getMessage());
        }
    }

    public Jwt checkValidRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(SecurityUtils.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh Token error: " + e.getMessage());
            throw e;
        }
    }
    public boolean isTokenExpired(String token) {
        try {
            // Decode the JWT token to extract the claims
            Jwt jwt = jwtDecoder.decode(token);

            // Get the expiration time from the token
            Instant expiration = jwt.getExpiresAt();

            // Check if the token is expired
            assert expiration != null;
            return expiration.isBefore(Instant.now());
        } catch (Exception e) {
            // If an exception occurs (e.g., token is invalid), consider the token as expired
            return true;
        }
    }

    public String extractEmail(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            // Trích xuất email từ claims
            return jwt.getClaimAsString("email");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract email from token: " + e.getMessage());
        }
    }

    public String extractSubject(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            // Trích xuất subject
            return jwt.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract email from token: " + e.getMessage());
        }
    }

    public boolean validateToken(String token, User userDetails) {
        try {
            String subject = extractSubject(token);
            //subject is phoneNumber or email
            Token existingToken = tokenRepository.findByToken(token);
            if(existingToken == null ||
                    existingToken.isRevoked() ||
                    !userDetails.isActive()
            ) {
                return false;
            }
            return (subject.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
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
}
