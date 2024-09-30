package org.tomato.weather.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sessions", indexes = {@Index(name = "expires_at_idx", columnList = "expires_at")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}