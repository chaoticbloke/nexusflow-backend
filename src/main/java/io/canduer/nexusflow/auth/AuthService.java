package io.canduer.nexusflow.auth;

import io.canduer.nexusflow.dto.*;

public interface AuthService {
    RegistrationResponseDTO register(RegistrationRequestDTO registrationRequestDTO);
    ApiResponse<LoginResponseDto> login(LoginRequestDTO loginRequestDTO);
}
