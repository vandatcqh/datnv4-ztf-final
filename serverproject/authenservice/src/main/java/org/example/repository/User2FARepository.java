package org.example.repository;

import org.example.model.User2FA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User2FARepository extends JpaRepository<User2FA, Integer> {
    Optional<User2FA> findByUserId(Integer userId);
}
