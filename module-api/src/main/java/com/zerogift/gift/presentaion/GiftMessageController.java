package com.zerogift.gift.presentaion;

import com.zerogift.gift.application.GiftMessageService;
import com.zerogift.gift.application.dto.GiftMessageRequest;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Result<?>> getGiftMessageForm(@PathVariable Long giftBoxId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
            .data(giftMessageService.getGiftMessageForm(giftBoxId, loginInfo))
            .build()
        );
    }

    @Operation(
        summary = "감사 메시지 전송", description = "감사 메시지 전송",
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
    public ResponseEntity<Result> getGiftMessage(@PathVariable Long giftMessageId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
            .data(giftMessageService.getGiftMessage(giftMessageId, loginInfo))
            .build()
        );
    }

    @Operation(
        summary = "받은 감사 메시지 리스트 조회", description = "받은 감사 메시지 리스트 조회",
        security = {@SecurityRequirement(name = "Authorization")},
        tags = {"Gift Message"}
    )
    @GetMapping("/giftMessage/list")
    public ResponseEntity<Result> getGiftMessageList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
            .data(giftMessageService.getGiftMessageList(loginInfo))
            .build()
        );
    }

}