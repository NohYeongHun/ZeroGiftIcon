package com.zerogift.backend.giftBox.controller;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.common.dto.Result;
import com.zerogift.backend.config.authorization.AuthenticationPrincipal;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.service.GiftBoxService;
import com.zerogift.backend.security.dto.LoginInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Gift Box", description = "선물함 관련 API")
@RestController
@RequiredArgsConstructor
public class GiftBoxController {

    private final GiftBoxService giftBoxService;

    @Operation(
        summary = "선물함 요청", description = "선물함리스틀를 조회합니다.",
        tags = {"Gift Box"}
    )
    @GetMapping("/giftbox")
    public ResponseEntity<Page<GiftBoxDto>> listGiftBox(
        @AuthenticationPrincipal LoginInfo loginInfo,
        MyPageableDto myPageableDto) {
        return ResponseEntity.ok(giftBoxService.findByGiftBoxList(loginInfo, myPageableDto));
    }

    @Operation(
        summary = "선물함 상세보기 요청", description = "선물함의 상세 정보를 조회합니다.",
        tags = {"Gift Box"}
    )
    @GetMapping("/giftbox/{giftBoxId}")
    public ResponseEntity<GiftBoxDetail> getGiftBox(@PathVariable Long giftBoxId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(giftBoxService.getGiftBoxDetail(loginInfo, giftBoxId));
    }

    @Operation(
        summary = "기프트콘 사용", description = "기프트콘 사용를 요청하는 API입니다.",
        tags = {"Gift Box"}
    )
    @GetMapping("/giftBox/giftcon/{giftBoxId}")
    public ResponseEntity<Result> useGiftCon(@PathVariable Long giftBoxId,
        @RequestParam String code) {
        giftBoxService.useGiftCon(giftBoxId, code);
        return ResponseEntity.ok(Result.builder()
            .data("사용 성공하였습니다.")
            .build());
    }

}
