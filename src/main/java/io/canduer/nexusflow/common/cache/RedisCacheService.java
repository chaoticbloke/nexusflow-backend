package io.canduer.nexusflow.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }


    @Retryable(retryFor = RedisConnectionFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
