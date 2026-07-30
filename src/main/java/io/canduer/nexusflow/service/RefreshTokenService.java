package io.canduer.nexusflow.service;

import io.canduer.nexusflow.entity.User;

public interface RefreshTokenService {
    String createRefreshToken(User user);
}
