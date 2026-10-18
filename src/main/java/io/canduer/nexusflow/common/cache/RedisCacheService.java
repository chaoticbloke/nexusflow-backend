package io.canduer.nexusflow.common.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    private final ObjectMapper objectMapper;

    public <T> T get(String key, Class<T> type) {

        Object cached = redisTemplate.opsForValue().get(key);

        if (cached == null) {
            return null;
        }

        return objectMapper.convertValue(cached, type);
    }

    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }


    @Retryable(retryFor = RedisConnectionFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
