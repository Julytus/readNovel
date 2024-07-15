package com.project.NovelWeb.repositories;

import com.project.NovelWeb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
