package com.zerogift.backend.giftMessage.controller;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.giftMessage.dto.GiftMessageDto;
import com.zerogift.backend.giftMessage.dto.GiftMessageForm;
import com.zerogift.backend.giftMessage.dto.GiftMessageRequest;
import com.zerogift.backend.giftMessage.service.GiftMessageService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Gift Message",description = "감사메시지 관련 API")
@RestController
@RequiredArgsConstructor
public class GiftMessageController {
    private final GiftMessageService giftMessageService;

    @Operation(
        summary = "감사메지지 보낼 폼 정보 요청", description = "감사 메시지 폼 정보 조회",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"Gift Message"}
    )
    @GetMapping("/giftMessage/form/{giftBoxId}")
    public ResponseEntity<GiftMessageForm> getGiftMessageForm(@PathVariable Long giftBoxId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(giftMessageService.getGiftMessageForm(giftBoxId, loginInfo));
    }

    @Operation(
        summary = "감사메시지 조회", description = "감사 메시지 전송",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"Gift Message"}
    )
    @PostMapping("/giftMessage")
    public ResponseEntity<Result> sendGiftMessage(@Valid @RequestBody GiftMessageRequest giftMessageRequest,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        giftMessageService.sendGiftMessage(giftMessageRequest, loginInfo);
        return ResponseEntity.ok(Result
            .builder()
            .data("성공적으로 메시지 전송되었습니다.")
            .build());
    }

    @Operation(
        summary = "감사 메시지 상세 정보 조회", description = "감사 메시지 상세정보 조회",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"Gift Message"}
    )
    @GetMapping("/giftMessage/{giftMessageId}")
    public ResponseEntity<GiftMessageDto> getGiftMessage(@PathVariable Long giftMessageId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(giftMessageService.getGiftMessage(giftMessageId, loginInfo));
    }

}
