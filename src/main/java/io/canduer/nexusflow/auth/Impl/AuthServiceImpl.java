package io.canduer.nexusflow.auth.Impl;

import io.canduer.nexusflow.auth.AuthService;
import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.RefreshToken;
import io.canduer.nexusflow.entity.Role;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.RolesEnum;
import io.canduer.nexusflow.exception.EmailAlreadyExistsException;
import io.canduer.nexusflow.exception.InvalidRefreshTokenException;
import io.canduer.nexusflow.exception.ResourceNotFoundException;
import io.canduer.nexusflow.jwt.JwtService;
import io.canduer.nexusflow.mapper.UserEntityMapper;
import io.canduer.nexusflow.repository.RefreshTokenRepository;
import io.canduer.nexusflow.repository.RoleRepository;
import io.canduer.nexusflow.repository.UserRepository;
import io.canduer.nexusflow.service.RefreshTokenService;
import io.canduer.nexusflow.utils.IdentifierUUIDGenerator;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
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
    private final IdentifierUUIDGenerator identifierUUIDGenerator;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RegistrationResponseDTO register(RegistrationRequestDTO registrationRequestDTO) {
        userRepository.findByEmail(registrationRequestDTO.getEmail())
                .ifPresent(user -> {
                    throw new EmailAlreadyExistsException(
                            "User already exists with email: " + user.getEmail());
                });        //create new user and save it to DB
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

        //SecurityContext securityContext =  SecurityContextHolder.getContext();

        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Authentication authenticatedAuthentication = authenticationManager.authenticate(authenticationRequest);
        //this line does lot of things . one of important -Compares Passwords(BCryptPasswordEncoder.matches())
        //if the password/email here is wrong - BadCredentialsException is thrown
        System.out.println("authenticatedAuthentication identity"+ authenticatedAuthentication.getAuthorities());


        /**
         * TODO: read
         * set the context: DON'T NEED . WHY?
         * SecurityContextHolder stores the authentication only for the lifetime of the current request
         * The SecurityContext is discarded when the request finishes. Future requests won't use it because your application is stateless
         * and each request is authenticated by your JWT filter,
         * which reconstructs the Authentication from the token and places it into the SecurityContext.
         */
        //securityContext.setAuthentication(authenticatedAuthentication);

        CustomUserDetails principal = (CustomUserDetails) authenticatedAuthentication.getPrincipal();

        User user = principal.getUser();

        UserDto userDto = UserDto.builder()
                .userId(user.getUserId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles().stream().map(Role::getRoleName).map(RolesEnum::name).toList())
                .build();

       String accessJwtToken = jwtService.generateAccessToken(principal);

       //generates Refresh token and saved in DB
       String refreshToken = refreshTokenService.createRefreshToken(user);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken(accessJwtToken)
                .refreshToken(refreshToken)
                .user(userDto)
                .build();

        //send both tokens in login api response
        return ApiResponse.<LoginResponseDto>builder()
                .success(authenticatedAuthentication.isAuthenticated())
                .message("User logged in successfully")
                .data(loginResponseDto)
                .build();
    }

    @Override
    public ApiResponse<RefreshTokenResponseDto> getRefreshToken(String refreshToken) {
        Claims claims = jwtService.getClaims(refreshToken);

        if (!"REFRESH".equals(claims.get("tokenType"))) {
            throw new InvalidRefreshTokenException("Invalid refresh token type.");
        }

        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found."));

        if (entity.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked.");
        }

        User user = entity.getUser();

        CustomUserDetails userDetails = new CustomUserDetails(user);

        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new InvalidRefreshTokenException("Refresh token has expired or is invalid.");
        }

        String accessToken = jwtService.generateAccessToken(userDetails);

        RefreshTokenResponseDto response = RefreshTokenResponseDto.builder()
                                            .accessToken(accessToken)
                                            .refreshToken(entity.getToken()) // static refresh
                                             .build();

        return ApiResponse.<RefreshTokenResponseDto>builder()
                .success(true)
                .message("Access token refreshed successfully.")
                .data(response)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<GenericResponseDTO> logout(LogoutRequest logoutRequest) {

        String refreshToken = logoutRequest.getRefreshToken();

        //validate refresh token
        jwtService.getClaims(refreshToken);

        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token."));

        if(entity.isRevoked()){
             GenericResponseDTO dto = GenericResponseDTO.builder().message("User already logged out.")
                     .success(true)
                    .build();

             return ApiResponse.<GenericResponseDTO>builder()
                     .success(true)
                     .data(dto)
                     .build();
        }
            entity.setRevoked(true);
            //refreshTokenRepository.save(entity); //no need to explicit if we have transactional. hibernate will auto update
            GenericResponseDTO dto = GenericResponseDTO.builder().message("Logged out successfully.")
                    .success(true)
                    .build();

            return ApiResponse.<GenericResponseDTO>builder()
                    .success(true)
                    .data(dto)
                    .build();
    }

}
