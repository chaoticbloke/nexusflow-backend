package io.canduer.nexusflow.entity;
import io.canduer.nexusflow.enums.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice extends BaseEntity {

    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
    private String services;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private BigDecimal total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
}
