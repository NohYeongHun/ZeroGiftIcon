package com.zerogift.backend.pay.service;

import com.zerogift.backend.common.exception.code.PayErrorCode;
import com.zerogift.backend.common.exception.pay.PayRedissonClientException;
import com.zerogift.backend.pay.type.LockStatus;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    private static final String PRODUCT = "PRODUCT_";

    public RLock lock(Long productId) {
        RLock lock = redissonClient.getLock(getLockKey(productId));

        try {
            boolean available = lock.tryLock(3, 1, TimeUnit.SECONDS);

            if(!available) {
                log.error("[redis] lock을 얻는데 실패하였습니다.");
                throw new PayRedissonClientException(PayErrorCode.REDISSON_EXCEPTION);
            }
        } catch (Exception e) {
            log.error("[redis] redis lock거는데 실패하였습니다.");
        }
        return lock;
    }

    private String getLockKey(Long productId) {
        return PRODUCT + productId;
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

}
