package com.zerogift.backend.pay.controller;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.pay.dto.PayHisoryRequest;
import com.zerogift.backend.pay.service.PayService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pay",description = "결제 관련 API")
@RestController
@RequiredArgsConstructor
public class PayHistoryController {

    private final PayService payService;

    private static final String PAY_SUCCESS = "결제 성공하였습니다.";

    @Operation(
        summary = "결제 요청", description = "결제를 진행합니다.",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"Pay"}
    )
    @PostMapping("/pay")
    public ResponseEntity<Result> savePayHistory(@RequestBody PayHisoryRequest payHisoryRequest,
        @AuthenticationPrincipal LoginInfo loginInfo) {

        payService.pay(payHisoryRequest, loginInfo.getEmail());
        return ResponseEntity.ok(Result.builder()
            .data(PAY_SUCCESS)
            .build());
    }

}
