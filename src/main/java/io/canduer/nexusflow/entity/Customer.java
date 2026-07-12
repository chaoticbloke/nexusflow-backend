package io.canduer.nexusflow.entity;

import io.canduer.nexusflow.enums.CustomerType;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends BaseEntity {

    private String customerId;
    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerType type;

    private String address;
    private String phone;

    //Many customers can be created by one user.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany( mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Invoice> invoices = new HashSet<>();
}
