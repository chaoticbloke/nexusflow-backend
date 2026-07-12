package io.canduer.nexusflow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Entity
@Table( name = "USERS")
@Getter
@Setter
public class User extends BaseEntity{

    private String userId;

    //user profile related
    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;
    private String position;
    private String bio;
    private String address;

    @Column(unique = true)
    private String phone;

    private String profileImageUrl;

    //Auth related
    private String password;
    private boolean accountLocked;
    private boolean emailVerified;
    private boolean phoneVerified;

    private boolean enabled;
    private LocalDateTime lastLogin;
    private boolean mfaEnabled;
    private int failedLoginAttempts;

    @ManyToMany(fetch  = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns =  @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name="ROLE_ID")
    )
    private Set<Role> roles;
}
