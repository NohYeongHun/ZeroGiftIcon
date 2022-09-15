package com.zerogift.backend.view.controller;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.code.ProductErrorCode;
import com.zerogift.backend.util.TokenUtil;
import com.zerogift.backend.view.service.ViewHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ViewHistoryController {

    private final ViewHistoryService viewHistoryService;


    // --- 테스트 용 ---
    // 조회수 증가 (임의의 이메일을 RequestBody 로 받아서 사용)
    @PutMapping("/user/{productId}/viewHistory/Temp")
    public ResponseEntity<Result<?>> addViewHistoryTemp(@PathVariable Long productId, @RequestHeader("EMAIL") String email) {
        return viewHistoryService.addViewHistory(email, productId);
    }

    // 조회수 증가
    @PutMapping("/user/{productId}/viewHistory")
    public ResponseEntity<?> addViewHistory(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail();
        if (email == null) {
            return ResponseEntity.badRequest().body(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        return viewHistoryService.addViewHistory(email, productId);
    }
}
