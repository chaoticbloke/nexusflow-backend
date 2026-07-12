package io.canduer.nexusflow.auth.Impl;

import io.canduer.nexusflow.auth.AuthService;
import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.Role;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.RolesEnum;
import io.canduer.nexusflow.exception.EmailAlreadyExistsException;
import io.canduer.nexusflow.jwt.JwtService;
import io.canduer.nexusflow.mapper.UserEntityMapper;
import io.canduer.nexusflow.repository.RoleRepository;
import io.canduer.nexusflow.repository.UserRepository;
import io.canduer.nexusflow.utils.IdentifierUUIDGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityMapper userEntityMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private IdentifierUUIDGenerator identifierUUIDGenerator;
    @Override
    @Transactional
    public RegistrationResponseDTO register(RegistrationRequestDTO registrationRequestDTO) {

       userRepository.findByEmail(registrationRequestDTO.getEmail()).ifPresent((user)->new EmailAlreadyExistsException("User already exists with this email :"+ user.getEmail()));
        //create new user and save it to DB
        User user = new User();
        user.setUserId(identifierUUIDGenerator.generateUserId());
        user.setEmail(registrationRequestDTO.getEmail());
        user.setFirstName(registrationRequestDTO.getFirstName());
        user.setLastName(registrationRequestDTO.getLastName());
        user.setAccountLocked(false);
        user.setEmailVerified(false);
        user.setMfaEnabled(false);

        Role role = roleRepository.findByRoleName(RolesEnum.ROLE_ADMIN).orElseThrow();
        System.out.println("ROLE FROM DB "+role.getRoleName());
        role.setRoleName(RolesEnum.ROLE_ADMIN);
        user.setRoles(Set.of(role));

        user.setFailedLoginAttempts(0);
        user.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
        User newUser = userRepository.save(user);

        return userEntityMapper.entityToDto(newUser);
    }

    @Override
    public ApiResponse<LoginResponseDto> login(LoginRequestDTO loginRequestDTO) {

        SecurityContext securityContext =  SecurityContextHolder.getContext();

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Authentication authenticatedAuthentication = authenticationManager.authenticate(authenticationRequest);
        System.out.println("authenticatedAuthentication identity"+ authenticatedAuthentication.getAuthorities());

        //set the context: DON'T NEED . WHY?
        securityContext.setAuthentication(authenticatedAuthentication);

        CustomUserDetails principal = (CustomUserDetails) authenticatedAuthentication.getPrincipal();

        User user = principal.getUser();

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream().map(Role::getRoleName).map(RolesEnum::name).toList())
                .build();

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .token(jwtService.generateToken((CustomUserDetails) authenticatedAuthentication.getPrincipal()))
                .user(userDto)
                .build();
        return ApiResponse.<LoginResponseDto>builder()
                .success(authenticatedAuthentication.isAuthenticated())
                .message("User logged in successfully")
                .data(loginResponseDto)
                .build();
    }

}
