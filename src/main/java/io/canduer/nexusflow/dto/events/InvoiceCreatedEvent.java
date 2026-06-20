package io.canduer.nexusflow.dto.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceCreatedEvent {

    private String invoiceNumber;

    private String customerId;
}