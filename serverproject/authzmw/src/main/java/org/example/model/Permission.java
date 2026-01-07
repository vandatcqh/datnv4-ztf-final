package org.example.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "api_path", nullable = false, length = 255)
    private String apiPath;

    @Column(nullable = false, length = 10)
    private String method; // GET, POST, PUT, DELETE

    @Column(length = 255)
    private String description;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
