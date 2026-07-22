package io.canduer.nexusflow.exception;

import io.canduer.nexusflow.dto.ErrorResponseDTO;
import io.canduer.nexusflow.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExist(EmailAlreadyExistsException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setSuccess(false);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(ResourceNotFoundException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setSuccess(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setMessage(ex.getMessage());
        errorResponseDTO.setSuccess(false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationError(MethodArgumentNotValidException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setSuccess(false);
        errorResponseDTO.setMessage(AppConstants.VALIDATION_ERROR_MESSAGE);
        errorResponseDTO.setErrors(handleValidationMessage(ex.getBindingResult().getFieldErrors()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialError(MethodArgumentNotValidException ex){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        errorResponseDTO.setSuccess(false);
        errorResponseDTO.setMessage(AppConstants.CREDENTIALS_ERROR_MESSAGE);
        errorResponseDTO.setErrors(handleValidationMessage(ex.getBindingResult().getFieldErrors()));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
    }

    private List< ErrorResponseDTO.ValidationError > handleValidationMessage(List<FieldError> fieldErrors){
        List< ErrorResponseDTO.ValidationError > errors = new ArrayList<>();
        for (FieldError ex : fieldErrors) {
            ErrorResponseDTO.ValidationError validationError = new ErrorResponseDTO.ValidationError();
            validationError.setField(ex.getField());
            validationError.setMessage(ex.getDefaultMessage());
            errors.add(validationError);
        }
        return errors;
    }

}
