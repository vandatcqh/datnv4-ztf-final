package org.example.repository;

import org.example.model.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    Optional<Relationship> findByUserId1AndUserId2(Integer userId1, Integer userId2);

    List<Relationship> findByUserId1OrUserId2AndStatus(Integer userId, Integer userId2, int status);

    List<Relationship> findByInitiatorIdAndStatus(Integer initiatorId, int status);

    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.userId1 = :userId OR r.userId2 = :userId) " +
            "AND r.status = :status " +
            "AND r.initiatorId <> :userId")
    List<Relationship> findByUserIdAndStatusAndNotInitiator(@Param("userId") Integer userId,
                                                            @Param("status") int status);


    @Query("SELECT r FROM Relationship r " +
            "WHERE (r.userId1 = :userId OR r.userId2 = :userId) " +
            "AND r.status = 1")
    List<Relationship> findFriendsByUserId(@Param("userId") Integer userId);


    @Query("SELECT r FROM Relationship r WHERE (r.userId1 = :userId OR r.userId2 = :userId) AND r.status = 2")
    List<Relationship> findBlockedRelationshipsByUserId(@Param("userId") int userId);

    // Tìm danh sách người dùng bị chặn bởi current user
    @Query("SELECT r FROM Relationship r WHERE r.initiatorId = :userId AND r.status = 2")
    List<Relationship> findBlockedUsersByUserId(@Param("userId") int userId);

}
