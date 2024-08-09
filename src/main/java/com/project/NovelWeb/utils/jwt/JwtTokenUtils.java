package com.project.NovelWeb.utils.jwt;

import com.project.NovelWeb.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public String generateToken(com.project.NovelWeb.models.entities.User user) throws Exception{

        Instant now = Instant.now();
        Instant validity = now.plus(expiration, ChronoUnit.SECONDS);
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
            return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        } catch (Exception e) {
            throw new Exception("Cannot create jwt token, error :" + e.getMessage());
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

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }
}
