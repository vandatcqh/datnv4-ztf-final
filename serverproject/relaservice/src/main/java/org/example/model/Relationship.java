package org.example.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "relationships")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // dễ quản lý hơn là chỉ PK (user1, user2)

    @Column(name = "user_id1", nullable = false)
    private Integer userId1;

    @Column(name = "user_id2", nullable = false)
    private Integer userId2;

    @Column(name = "initiator_id", nullable = false)
    private Integer initiatorId;

    @Column(name = "status", nullable = false)
    private Integer status; // 0 = pending, 1 = accepted, 2 = blocked

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
