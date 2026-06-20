package io.canduer.nexusflow.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@JsonPropertyOrder({
        "success",
        "message",
        "errors"
})
public class ErrorResponseDTO {
    private boolean success;
    private String message;
    private List<ValidationError> errors;

    @Setter
    @Getter
    public static class ValidationError {
        private String field;
        private String message;
    }
}
