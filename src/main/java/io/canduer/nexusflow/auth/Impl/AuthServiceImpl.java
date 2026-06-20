package io.canduer.nexusflow.auth.Impl;

import io.canduer.nexusflow.auth.AuthService;
import io.canduer.nexusflow.dto.LoginRequestDTO;
import io.canduer.nexusflow.dto.LoginResponseDto;
import io.canduer.nexusflow.dto.RegistrationRequestDTO;
import io.canduer.nexusflow.dto.RegistrationResponseDTO;
import io.canduer.nexusflow.entity.Role;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.RolesEnum;
import io.canduer.nexusflow.exception.EmailAlreadyExistsException;
import io.canduer.nexusflow.jwt.JwtService;
import io.canduer.nexusflow.mapper.UserEntityMapper;
import io.canduer.nexusflow.repository.RoleRepository;
import io.canduer.nexusflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityMapper userEntityMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    @Override
    @Transactional
    public RegistrationResponseDTO register(RegistrationRequestDTO registrationRequestDTO) {

       userRepository.findByEmail(registrationRequestDTO.getEmail()).ifPresent((user)->new EmailAlreadyExistsException("User already exists with this email :"+ user.getEmail()));
        //create new user and save it to DB
        User user = new User();
        user.setEmail(registrationRequestDTO.getEmail());
        user.setFirstName(registrationRequestDTO.getFirstName());
        user.setLastName(registrationRequestDTO.getLastName());
        user.setAccountLocked(false);
        user.setEmailVerified(false);
        user.setUsingMfa(false);

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
    public LoginResponseDto login(LoginRequestDTO loginRequestDTO) {

        SecurityContext securityContext =  SecurityContextHolder.getContext();

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
        System.out.println("authenticationRequest identity"+ authenticationRequest.getAuthorities());
        Authentication authenticatedAuthentication = authenticationManager.authenticate(authenticationRequest);
        System.out.println("authenticatedAuthentication identity"+ authenticatedAuthentication.getAuthorities());

        //set the context
        securityContext.setAuthentication(authenticatedAuthentication);

        return LoginResponseDto.builder()
                .success(authenticatedAuthentication.isAuthenticated())
                .message("User logged in successfully")
                .httpStatusCode(200)
                .token(jwtService.generateToken((CustomUserDetails) authenticatedAuthentication.getPrincipal()))
                .build();
    }

}
