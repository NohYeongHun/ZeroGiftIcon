package com.zerogift.backend.view.service;

import com.zerogift.backend.common.dto.Result;
import org.springframework.http.ResponseEntity;

public interface ViewHistoryService {

    // 조회수 증가
    ResponseEntity<Result<?>> addViewHistory(String email, Long productId);
}
