package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.auth.AuthService;
import io.canduer.nexusflow.dto.LoginRequestDTO;
import io.canduer.nexusflow.dto.LoginResponseDto;
import io.canduer.nexusflow.dto.RegistrationRequestDTO;
import io.canduer.nexusflow.dto.RegistrationResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegistrationResponseDTO register(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) {
        return authService.register(registrationRequestDTO);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return authService.login(loginRequestDTO);
    }
}
