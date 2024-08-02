package com.project.NovelWeb.utils.jwt;

import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.TokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${jwt.secretKey}")
    private String secretKey;
    private final TokenRepository tokenRepository;

    public String generateToken(com.project.NovelWeb.models.entities.User user) throws Exception{
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration + 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new Exception("Cannot create jwt token, error :" + e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, User user) {
//        try {
//            String email = extractEmail(token);
//            Token existingToken = tokenRepository.findByToken(token);
//            if (existingToken == null ||
//                    existingToken.isRevoked() ||
//                    !user.isActive()) {
//                return false;
//            }
//        } catch (MalformedJwtException e) {
//            throw new Exception("Invalid JWT token. error: " + e.getMessage());
//        } catch (ExpiredJwtException e) {
//            throw new Exception("JWT token is expired, error: " +  e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            throw new Exception("JWT token is unsupported, error: " + e.getMessage());
//        }
//        return false;
        String phoneNumber = extractEmail(token);
        return (phoneNumber.equals(user.getUsername()))
                && !isTokenExpired(token);
    }

}
