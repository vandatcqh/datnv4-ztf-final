package org.example.repository;

import org.example.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query("SELECT p FROM Permission p " +
            "JOIN RolePermission rp ON rp.permissionId = p.id " +
            "WHERE rp.roleId = :roleId " +
            "AND :path LIKE p.apiPath " +
            "AND p.method = :method")
    Optional<Permission> findByRoleAndPathAndMethod(
            @Param("roleId") int roleId,
            @Param("path") String path,
            @Param("method") String method
    );
}
