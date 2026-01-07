package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.example.repository.UserRoleRepository;
import org.example.repository.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthzService {

    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;

    public boolean checkPermission(int userId, String path, String method) {
        var roleIds = userRoleRepository.findRoleIdsByUserId(userId);
        if (roleIds == null || roleIds.isEmpty()) return false;

        // Normalize path: /management/users/12 → /management/users/*
        String normalizedPath = path.replaceAll("/\\d+", "/*");

        // Kiểm tra từng role
        for (Integer roleId : roleIds) {
            boolean hasPermission = permissionRepository
                    .findByRoleAndPathAndMethod(roleId, normalizedPath, method)
                    .isPresent();
            if (hasPermission) {
                return true; // chỉ cần 1 role có quyền là OK
            }
        }

        return false;
    }
}
