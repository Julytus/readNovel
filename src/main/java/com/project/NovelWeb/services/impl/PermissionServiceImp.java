package com.project.NovelWeb.services.impl;

import com.project.NovelWeb.exceptions.DataNotFoundException;
import com.project.NovelWeb.models.dtos.PermissionDTO;
import com.project.NovelWeb.models.entities.Permission;
import com.project.NovelWeb.models.entities.Role;
import com.project.NovelWeb.repositories.PermissionRepository;
import com.project.NovelWeb.repositories.RoleRepository;
import com.project.NovelWeb.services.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImp implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public boolean isPermissionExist(PermissionDTO p) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                p.getModule(),
                p.getApiPath(),
                p.getMethod());
    }

    @Override
    public Permission fetchById(long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        return permissionOptional.orElse(null);
    }

    @Override
    public Permission create(PermissionDTO permissionDTO) {
        //Get List ContentType for Novel
        List<Role> roles = permissionDTO.getRoleId().stream()
                .map(id -> {
                    try {
                        return roleRepository.findById(id)
                                .orElseThrow(() -> new DataNotFoundException("Cannot find Role with id: " + id));
                    } catch (DataNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        Permission p = Permission.builder()
                .name(permissionDTO.getName())
                .apiPath(permissionDTO.getApiPath())
                .module(permissionDTO.getModule())
                .method(permissionDTO.getMethod())
                .build();
        p.setRoles(roles);
        return permissionRepository.save(p);
    }

    @Override
    public Permission update(PermissionDTO p) {
        Permission permissionDB = this.fetchById(p.getId());
        if (permissionDB != null) {
            permissionDB.setName(p.getName());
            permissionDB.setApiPath(p.getApiPath());
            permissionDB.setMethod(p.getMethod());
            permissionDB.setModule(p.getModule());

            // update
            permissionDB = permissionRepository.save(permissionDB);
            return permissionDB;
        }
        return null;
    }

    @Override
    public void delete(long id) {
        // delete permission_role
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) return;
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete permission
        permissionRepository.delete(currentPermission);

    }
}
