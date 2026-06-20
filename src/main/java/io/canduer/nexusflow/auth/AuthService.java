package io.canduer.nexusflow.auth;

import io.canduer.nexusflow.dto.LoginRequestDTO;
import io.canduer.nexusflow.dto.LoginResponseDto;
import io.canduer.nexusflow.dto.RegistrationRequestDTO;
import io.canduer.nexusflow.dto.RegistrationResponseDTO;

public interface AuthService {
    RegistrationResponseDTO register(RegistrationRequestDTO registrationRequestDTO);
    LoginResponseDto login(LoginRequestDTO loginRequestDTO);
}
