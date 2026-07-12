package io.canduer.nexusflow.dto;

import io.canduer.nexusflow.enums.CustomerType;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerResponseDTO implements Serializable {

    private String customerId;
    private String name;
    private String email;
    private CustomerType type;
    private String address;
    private String phone;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}