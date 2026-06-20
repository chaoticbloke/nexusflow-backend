package io.canduer.nexusflow.mapper;

import io.canduer.nexusflow.dto.RegistrationResponseDTO;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.RolesEnum;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    public RegistrationResponseDTO  entityToDto(User user) {
        RegistrationResponseDTO registrationResponseDTO = new RegistrationResponseDTO();
        registrationResponseDTO.setRoles(RolesEnum.ROLE_USER.toString());
        registrationResponseDTO.setUserId(user.getUserId().toString());
        registrationResponseDTO.setFirstName(user.getFirstName());
        registrationResponseDTO.setEmail(user.getEmail());
        registrationResponseDTO.setLastName(user.getLastName());
        return registrationResponseDTO;
    }
}
