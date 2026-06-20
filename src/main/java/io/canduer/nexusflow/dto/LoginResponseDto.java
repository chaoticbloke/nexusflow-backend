package io.canduer.nexusflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private int httpStatusCode;
    private boolean success;
    private String message;
    private String token;
}
