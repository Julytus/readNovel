package com.project.NovelWeb.services;

import com.project.NovelWeb.models.entities.Token;
import com.project.NovelWeb.models.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, User user) throws Exception;
}
