package io.canduer.nexusflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class InvoiceDTO {
    private String invoiceNumber;
    private String customerName;
    @NotBlank
    private String status;
    private String services;
    private Date date;
    private double totalAmount;
    private LocalDateTime createdAt;
}
