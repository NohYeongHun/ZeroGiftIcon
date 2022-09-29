package com.zerogift.backend.view.aop;

import com.zerogift.backend.security.dto.LoginInfo;
import com.zerogift.backend.view.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class viewHistoryAop {

    private final ViewHistoryService viewHistoryService;

    @Around("execution(* com.zerogift.backend.product.controller.ProductController.getDetail(..)) && args(productId, loginInfo)")
    public Object viewHistoryAOP(ProceedingJoinPoint joinPoint, Long productId, LoginInfo loginInfo) throws Throwable {
        Object ogj = joinPoint.proceed();
        viewHistoryService.addViewHistory(loginInfo, productId);
        return ogj;
    }



}
