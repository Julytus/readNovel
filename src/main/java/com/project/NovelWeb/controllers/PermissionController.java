package com.project.NovelWeb.controllers;

import com.project.NovelWeb.models.dtos.PermissionDTO;
import com.project.NovelWeb.models.entities.Permission;
import com.project.NovelWeb.services.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/permissions")
public class PermissionController {
    private final PermissionService permissionService;
    @PostMapping("")
    public ResponseEntity<Permission> create(@Valid @RequestBody PermissionDTO p) throws Exception {
        // check exist
        if (permissionService.isPermissionExist(p)) {
            throw new Exception("Permission already exists.");
        }

        // create new permission
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.create(p));
    }
    @PutMapping("")
    public ResponseEntity<Permission> update(@Valid @RequestBody PermissionDTO p) throws Exception {
        // check exist by id
        if (this.permissionService.fetchById(p.getId()) == null) {
            throw new Exception("Permission với id = " + p.getId() + " không tồn tại.");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(p)) {
            throw new Exception("Permission đã tồn tại.");
        }

        // update permission
        return ResponseEntity.ok().body(this.permissionService.update(p));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws Exception {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new Exception("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

}

