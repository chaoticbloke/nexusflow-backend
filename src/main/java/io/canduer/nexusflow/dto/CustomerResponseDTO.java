package io.canduer.nexusflow.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponseDTO {

    private String customerId;
    private String name;
    private String email;
    private String type;
    private String address;
    private String phone;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}