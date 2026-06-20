package io.canduer.nexusflow.dto;

import lombok.Data;

@Data
public class RegistrationResponseDTO {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String roles;
}
