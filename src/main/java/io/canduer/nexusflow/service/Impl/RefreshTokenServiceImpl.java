package io.canduer.nexusflow.service.Impl;
import io.canduer.nexusflow.auth.Impl.CustomUserDetails;
import io.canduer.nexusflow.entity.RefreshToken;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.jwt.JwtService;
import io.canduer.nexusflow.repository.RefreshTokenRepository;
import io.canduer.nexusflow.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createRefreshToken(User user) {

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateRefreshToken(userDetails);

        RefreshToken entity = RefreshToken.builder()
                .token(token)
                .user(user)
                .createdAt(LocalDateTime.now())
                .revoked(false)
                .build();

        refreshTokenRepository.save(entity);

        return token;
    }
}
