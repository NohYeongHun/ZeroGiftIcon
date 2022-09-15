package com.zerogift.backend.view.service;

import com.zerogift.backend.common.dto.Result;
import org.springframework.http.ResponseEntity;

public interface ViewHistoryService {

    // 무조건 보면 조회수 증가 (중복 o)
    ResponseEntity<Result<?>> addViewCount(Long productId);

    // 조회수 증가 (중복 x)
    ResponseEntity<Result<?>> addViewHistory(String email, Long productId);
}
