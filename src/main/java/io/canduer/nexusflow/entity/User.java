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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID userId;

    //user profile related
    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String position;
    private String bio;
    private String address;
    private String phone;
    private String profileUrl;

    //Auth related
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isAccountLocked;
    private boolean emailVerified;
    private boolean phoneVerified;

    private boolean usingMfa;
    private int failedLoginAttempts;

    @ManyToMany(fetch  = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns =  @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name="ROLE_ID")
    )
    private Set<Role> roles;

    @PrePersist
    public void prePersist() {
        userId = UUID.randomUUID();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
