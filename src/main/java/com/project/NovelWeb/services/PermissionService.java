package com.project.NovelWeb.services;

import com.project.NovelWeb.models.dtos.PermissionDTO;
import com.project.NovelWeb.models.entities.Permission;

public interface PermissionService {
    boolean isPermissionExist(PermissionDTO permissionDTO);
    Permission fetchById(long id);
    Permission create(PermissionDTO permissionDTO);
    Permission update(PermissionDTO permissionDTO);
    void delete(long id);
}
