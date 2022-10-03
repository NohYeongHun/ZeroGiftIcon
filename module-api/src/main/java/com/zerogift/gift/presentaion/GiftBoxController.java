package com.zerogift.gift.presentaion;

import com.zerogift.gift.application.GiftBoxService;
import com.zerogift.support.auth.userdetails.LoginInfo;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.support.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Result<?>> listGiftBox(
        @AuthenticationPrincipal LoginInfo loginInfo,
        MyPageableDto myPageableDto) {
        return ResponseEntity.ok(Result.builder()
            .data(giftBoxService.findByGiftBoxList(loginInfo, myPageableDto))
            .build()
        );
    }

    @Operation(
        summary = "선물함 상세보기 요청", description = "선물함의 상세 정보를 조회합니다.",
        tags = {"Gift Box"}
    )
    @GetMapping("/giftbox/{giftBoxId}")
    public ResponseEntity<Result<?>> getGiftBox(@PathVariable Long giftBoxId,
        @AuthenticationPrincipal LoginInfo loginInfo) {
        return ResponseEntity.ok(Result.builder()
            .data(giftBoxService.getGiftBoxDetail(loginInfo, giftBoxId))
            .build()
        );
    }

    @Operation(
        summary = "기프트콘 사용", description = "기프트콘 사용를 요청하는 API입니다.",
        tags = {"Gift Box"}
    )
    @GetMapping("/barcode")
    public ResponseEntity<Result> useGiftCon(@RequestParam Long giftBoxId,
        @RequestParam String code) {
        giftBoxService.useGiftCon(giftBoxId, code);
        return ResponseEntity.ok(Result.builder()
            .data("사용 성공하였습니다.")
            .build());
    }

}
