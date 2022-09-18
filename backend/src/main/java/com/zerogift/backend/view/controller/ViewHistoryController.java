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

    // 조회수 증가
    @PutMapping("/user/{productId}/viewHistory")
    public ResponseEntity<?> addViewHistory(@PathVariable Long productId) {
        String email = TokenUtil.getAdminEmail() != null ? TokenUtil.getAdminEmail() : TokenUtil.getMemberEmail();
        return viewHistoryService.addViewHistory(email, productId);
    }
}
