package io.canduer.nexusflow.entity;

import io.canduer.nexusflow.enums.Permissions;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //permissions
    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private Permissions name;
}
