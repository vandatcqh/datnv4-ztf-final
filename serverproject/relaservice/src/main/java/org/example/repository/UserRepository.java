package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Tìm user bằng ID
    Optional<User> findById(Integer id);

    // Tìm user bằng username
    Optional<User> findByUsername(String username);

    // Tìm user bằng username và kiểm tra available
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isAvailable() = true")
    Optional<User> findByUsernameAndAvailable(@Param("username") String username);

    // Tìm tất cả user available
    @Query("SELECT u FROM User u WHERE u.isAvailable() = true")
    List<User> findAllAvailableUsers();

    // Kiểm tra username đã tồn tại chưa (dùng cho create/update)
    boolean existsByUsername(String username);

    // Kiểm tra username đã tồn tại và khác user hiện tại (dùng cho update)
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = :username AND u.id != :userId")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("userId") Integer userId);

    // Tìm user bằng ID và kiểm tra available
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isAvailable() = true")
    Optional<User> findByIdAndAvailable(@Param("id") Integer id);
}