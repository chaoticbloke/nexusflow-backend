package io.canduer.nexusflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private String name;
    private String email;
    private String type;
    private String address;
    private String phone;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany( mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Invoice> invoices;
}
