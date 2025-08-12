package com.imogo.imogo_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private Long id; // mesmo id do usuário

    private String name;
    private Integer age;
    private String phone;
    private String address;
    // outros campos do perfil

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
