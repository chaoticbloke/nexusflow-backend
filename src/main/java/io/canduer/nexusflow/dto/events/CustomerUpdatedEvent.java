package io.canduer.nexusflow.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerUpdatedEvent {
    private String customerId;
}
