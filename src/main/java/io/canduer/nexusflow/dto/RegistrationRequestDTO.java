package io.canduer.nexusflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDTO {

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, max = 100)
    private String password;
}
