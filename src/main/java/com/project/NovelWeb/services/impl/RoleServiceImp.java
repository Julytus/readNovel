package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp {
    private final RoleRepository roleRepository;
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
