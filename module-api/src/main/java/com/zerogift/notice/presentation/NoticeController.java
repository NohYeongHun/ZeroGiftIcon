package com.zerogift.notice.presentation;

import com.zerogift.notice.application.NoticeService;
import com.zerogift.support.auth.authorization.AuthenticationPrincipal;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "Notice", description = "알림 관련 API")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    private static final String CHECK_NOTICE = "알림 확인";

    @CrossOrigin
    @GetMapping(value = "/notice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter Notice(@AuthenticationPrincipal LoginInfo loginInfo) {
        return noticeService.createSseEmitter(loginInfo);
    }

    @Operation(
        summary = "알림 체크", description = "알림 확인 유무 변경.",
        tags = {"Notice"}
    )
    @GetMapping("/notice/check/{noticeId}")
    public ResponseEntity<?> checkNotice(@AuthenticationPrincipal LoginInfo loginInfo, @PathVariable Long noticeId) {
        noticeService.checkNotice(loginInfo, noticeId);
        return ResponseEntity.ok(Result.builder()
            .data(CHECK_NOTICE)
            .build()
        );
    }

    @Operation(
        summary = "알림 리스트", description = "사용자한테 날라온 알림 전부 호출.",
        tags = {"Notice"}
    )
    @GetMapping("/notice/list")
    public ResponseEntity<?> noticeList(@AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
            .data(noticeService.noticeList(loginInfo))
            .build()
        );
    }

    @Operation(
        summary = "알림 리스트", description = "확인하지 않은 알림 리스트 호출.",
        tags = {"Notice"}
    )
    @GetMapping("/notice/list/uncheck")
    public ResponseEntity<?> uncheckedNoticeList(@AuthenticationPrincipal LoginInfo loginInfo) {

        return ResponseEntity.ok(Result.builder()
            .data(noticeService.uncheckedNoticeList(loginInfo))
            .build()
        );
    }
}
