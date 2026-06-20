package io.canduer.nexusflow.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class InvoiceDTO {
    private String invoiceNumber;
    @NotBlank
    private String status;
    private String services;
    private Date date;
    private double totalAmount;
}
