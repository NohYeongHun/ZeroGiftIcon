package com.zerogift.product.application;

import com.zerogift.support.auth.userdetails.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class viewHistoryAop {

    private final ViewHistoryService viewHistoryService;

    @Around("execution(* com.zerogift.product.presentation.ProductController.getDetail(..)) && args(productId, loginInfo)")
    public Object viewHistoryAOP(ProceedingJoinPoint joinPoint, Long productId, LoginInfo loginInfo) throws Throwable {
        Object ogj = joinPoint.proceed();
        viewHistoryService.addViewHistory(loginInfo, productId);
        return ogj;
    }

}
