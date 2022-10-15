package com.zerogift.pay.application;

import com.zerogift.pay.application.dto.PayHisoryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PayLockAop {
    private final LockService lockService;

    @Around("@annotation(com.zerogift.pay.application.PayLock) && args(payHisoryRequest, email)")
    public Object aroundMethod(ProceedingJoinPoint pjp, PayHisoryRequest payHisoryRequest, String email) throws Throwable {
        Long productId = payHisoryRequest.getProductId();

        log.info("[START] PayLockAop");

        RLock lock = lockService.lock(productId);
        Object proceed;
        try {
            proceed = pjp.proceed();
        } finally {
            lockService.unlock(lock);
        }
        return proceed;
    }

}
