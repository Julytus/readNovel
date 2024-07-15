package com.project.NovelWeb.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    private String retypePassword;

    @Column(name = "is_active")
    private boolean active;
}
