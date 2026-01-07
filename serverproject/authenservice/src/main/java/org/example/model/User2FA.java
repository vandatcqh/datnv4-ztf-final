package org.example.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_2fa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User2FA {

    @Id
    @Column(name = "user_id")
    private int userId;  // ✅ đổi sang int

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "secret_key", nullable = false, length = 255)
    private String secretKey;

    @Column(name = "qr_code_url", nullable = false, columnDefinition = "TEXT")
    private String qrCodeURL;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}
