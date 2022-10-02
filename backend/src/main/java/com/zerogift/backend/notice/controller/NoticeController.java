package com.zerogift.backend.notice.controller;

import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.common.exception.code.MemberErrorCode;
import com.zerogift.backend.common.exception.member.MemberException;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.notice.service.NoticeService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "Notice", description = "알림 관련 API")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final MemberRepository memberRepository;
    private final NoticeService noticeService;

    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    private static final String CHECK_NOTICE = "알림 확인";
    private static final Long TIMEOUT = 60 * 60 * 1000L;

    @CrossOrigin
    @GetMapping(value = "/notice", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public SseEmitter Notice(@AuthenticationPrincipal LoginInfo loginInfo) {

        Long memberId = memberRepository.findByEmail(loginInfo.getEmail())
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)).getId();

        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sseEmitters.put(memberId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(memberId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(memberId));
        sseEmitter.onError((e) -> sseEmitters.remove(memberId));

        return sseEmitter;
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
