package org.example.repository;

import org.example.model.UserRole;
import org.example.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Query("SELECT ur.roleId FROM UserRole ur WHERE ur.userId = :userId")
    List<Integer> findRoleIdsByUserId(int userId);
}
