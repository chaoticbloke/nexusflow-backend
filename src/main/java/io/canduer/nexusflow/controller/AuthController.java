package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.auth.AuthService;
import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.RefreshToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public RegistrationResponseDTO register(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) {
        return authService.register(registrationRequestDTO);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDto> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return authService.login(loginRequestDTO);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<RefreshTokenResponseDto> getRefreshToken(@RequestBody String refreshToken) {
        return authService.getRefreshToken(refreshToken.trim());
    }
}
