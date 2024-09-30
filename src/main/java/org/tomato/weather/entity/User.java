package org.tomato.weather.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    private String password;

    @OneToOne
    @PrimaryKeyJoinColumn
    @ToString.Exclude
    private Session session;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Location> locations;

}
