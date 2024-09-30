package org.tomato.weather.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    // GUID
    @Id
    @GeneratedValue
    private UUID id;

    private Date expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
