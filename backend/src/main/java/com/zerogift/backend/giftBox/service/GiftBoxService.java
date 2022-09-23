package com.zerogift.backend.giftBox.service;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.common.exception.gift.NotEqualsNotBarcodeException;
import com.zerogift.backend.common.exception.gift.NotFoundGiftBoxException;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftBox.repository.GiftBoxRepository;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.security.dto.LoginInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftBoxService {
    private final GiftBoxRepository giftBoxRepository;
    private final MemberRepository memberRepository;

    public List<GiftBoxDto> findByGiftBoxList(LoginInfo loginInfo, MyPageableDto myPageableDto) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        return giftBoxRepository.findByIsUseEqFalse(member, myPageableDto);
    }

    public GiftBoxDetail getGiftBoxDetail(LoginInfo loginInfo, Long giftBoxId) {
        Member member = memberRepository.findByEmail(loginInfo.getEmail())
            .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        return giftBoxRepository.findByGiftBoxId(giftBoxId, member);
    }

    @Transactional
    public void useGiftCon(Long giftBoxId, String code) {
        GiftBox giftBox = giftBoxRepository.findById(giftBoxId)
            .orElseThrow(() -> new NotFoundGiftBoxException("선물함에 존재하지 않습니다."));

        checkBarcodeCode(giftBox, code);
        giftBox.use();
    }

    private void checkBarcodeCode(GiftBox giftBox, String code) {
        if(!code.equals(giftBox.getCode())) {
            throw new NotEqualsNotBarcodeException("코드가 일치 하지 않습니다.");
        }
    }

}
