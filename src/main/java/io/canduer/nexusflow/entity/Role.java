package io.canduer.nexusflow.entity;

import io.canduer.nexusflow.enums.RolesEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RolesEnum roleName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name ="ROLE_PERMISSIONS",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn( name = "PERMISSION_ID")
    )
    private Set<Permission> permissions;
}
