package io.canduer.nexusflow.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNumber;
    private String status;
    private String services;
    private Date date;
    private double total;

    @ManyToOne
    @JoinColumn( name ="customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Builder.Default
    private LocalDateTime createAt = LocalDateTime.now();
}
