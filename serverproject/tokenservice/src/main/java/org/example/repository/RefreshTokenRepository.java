package org.example.repository;

import org.example.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // Tìm token chính xác (không quan tâm hết hạn hay chưa)
    Optional<RefreshToken> findByToken(String token);

    // Tìm token còn hạn
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.token = :token AND rt.validUntil > :now")
    Optional<RefreshToken> findValidToken(@Param("token") String token, @Param("now") LocalDateTime now);

    // Lấy tất cả token còn hạn của user
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.userId = :userId AND rt.validUntil > :now")
    List<RefreshToken> findValidTokensByUserId(@Param("userId") Integer userId, @Param("now") LocalDateTime now);

    // Lấy tất cả token (không quan tâm expired)
    List<RefreshToken> findAllByUserId(Integer userId);

    // Đếm số token còn hạn
    @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.userId = :userId AND rt.validUntil > :now")
    long countValidTokensByUserId(@Param("userId") Integer userId, @Param("now") LocalDateTime now);

    // Vô hiệu hóa 1 token
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.validUntil = :now WHERE rt.token = :token")
    void invalidateToken(@Param("token") String token, @Param("now") LocalDateTime now);

    // Vô hiệu hóa toàn bộ token của user
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.validUntil = :now WHERE rt.userId = :userId")
    int invalidateAllTokensByUserId(@Param("userId") Integer userId, @Param("now") LocalDateTime now);

    // Tìm tất cả token đã hết hạn
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.validUntil <= :now")
    List<RefreshToken> findExpiredTokens(@Param("now") LocalDateTime now);
}
