package io.canduer.nexusflow.dto;

import lombok.Data;

@Data
public class CreateCustomerRequestDTO {

    private String name;
    private String email;
    private String type;
    private String address;
    private String phone;
}