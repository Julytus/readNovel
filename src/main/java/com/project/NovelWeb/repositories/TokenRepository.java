package com.project.NovelWeb.repositories;



import com.project.NovelWeb.models.entity.Token;
import com.project.NovelWeb.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
    Token findByToken(String token);
    Token findByRefreshToken(String token);
}

