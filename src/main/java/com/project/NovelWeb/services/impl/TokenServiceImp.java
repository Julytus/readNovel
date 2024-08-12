package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.IdInvalidException;
import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.models.entities.User;
import com.project.NovelWeb.repositories.TokenRepository;
import com.project.NovelWeb.services.TokenService;
import com.project.NovelWeb.services.UserService;
import com.project.NovelWeb.utils.JwtTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImp implements TokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration-access-token}")
    private int expirationAccessToken; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    @Transactional
    @Override
    public Token addToken(User user, String token, boolean isMobileDevice) throws Exception {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        if (tokenCount >= MAX_TOKENS) {
            //will be true if at least one token is not a mobile device, otherwise it will be false.
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream()
                        .filter(userToken -> !userToken.isMobile())
                        .findFirst()
                        .orElse(userTokens.get(0));
            } else {
                //all tokens are mobile, we will delete the first token in the list
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }

        String newRefreshToke = jwtTokenUtils.createRefreshToken(user);
        //Generate a new token for the user
        Token newToken = Token.builder()
                .user(user)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(LocalDateTime.now().plusSeconds(expirationAccessToken))
                .isMobile(isMobileDevice)
                .build();
        newToken.setRefreshToken(newRefreshToke);
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token refreshToken(String refreshToken, String email) throws Exception {
        if (refreshToken.equals("abc")) {
            throw new IdInvalidException("You do not have a refresh token in your cookie.");
        }
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        User currentUser = userService.getUserDetailsFromRefreshToken(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Invalid refresh Token");
        }

        String newAccessToken = jwtTokenUtils.createAccessToken(currentUser);
        String newRefreshToken = jwtTokenUtils.createRefreshToken(currentUser);
        existingToken.setToken(newAccessToken);
        existingToken.setExpirationDate(LocalDateTime.now().plusSeconds(expirationAccessToken));
        existingToken.setRefreshToken(newRefreshToken);
        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        return tokenRepository.save(existingToken);
    }
}
