package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String Email);
}
