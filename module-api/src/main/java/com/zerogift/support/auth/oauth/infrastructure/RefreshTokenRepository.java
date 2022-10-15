package com.zerogift.support.auth.oauth.infrastructure;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PREFIX = "refreshToken::";

    public void save(String username, String refreshToken, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(KEY_PREFIX + username, refreshToken, duration);
    }

    public boolean existsByUsername(String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String refreshToken = values.get(KEY_PREFIX + username);

        if (!StringUtils.hasText(refreshToken)) {
            return false;
        }

        return true;
    }

    public Optional<String> findByUsername(String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();

        return Optional.ofNullable(values.get(KEY_PREFIX + username));
    }

    public void deleteByUsername(String username) {
        redisTemplate.delete(KEY_PREFIX + username);
    }

}
