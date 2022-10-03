package com.zerogift.gift.application;

import com.zerogift.global.error.code.GiftBoxErrorCode;
import com.zerogift.global.error.code.MemberErrorCode;
import com.zerogift.global.error.exception.MemberException;
import com.zerogift.global.error.exception.NotEqualsNotBarcodeException;
import com.zerogift.global.error.exception.NotFoundGiftBoxException;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.gift.application.dto.GiftBoxDetail;
import com.zerogift.gift.application.dto.GiftBoxDto;
import com.zerogift.gift.domain.GiftBox;
import com.zerogift.gift.repository.GiftBoxRepository;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zerogift.support.auth.userdetails.LoginInfo;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftBoxService {
    private final GiftBoxRepository giftBoxRepository;
    private final MemberRepository memberRepository;

    public List<GiftBoxDto> findByGiftBoxList(LoginInfo loginInfo, MyPageableDto myPageableDto) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return giftBoxRepository.findByIsUseEqFalse(member, myPageableDto);
    }

    public GiftBoxDetail getGiftBoxDetail(LoginInfo loginInfo, Long giftBoxId) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        return giftBoxRepository.findByGiftBoxId(giftBoxId, member);
    }

    @Transactional
    public void useGiftCon(Long giftBoxId, String code) {
        GiftBox giftBox = giftBoxRepository.findById(giftBoxId)
            .orElseThrow(() -> new NotFoundGiftBoxException(GiftBoxErrorCode.GIFT_BOX_NOT_FOUND));

        checkBarcodeCode(giftBox, code);
        giftBox.use();
    }

    private void checkBarcodeCode(GiftBox giftBox, String code) {
        if(!code.equals(giftBox.getCode())) {
            throw new NotEqualsNotBarcodeException(GiftBoxErrorCode.CODE_NOT_MATCH);
        }
    }

}
