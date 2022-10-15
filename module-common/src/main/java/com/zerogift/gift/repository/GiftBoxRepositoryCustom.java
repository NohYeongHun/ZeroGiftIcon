package com.zerogift.gift.repository;

import com.zerogift.gift.domain.GiftBox;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.gift.application.dto.GiftBoxDetail;
import com.zerogift.gift.application.dto.GiftBoxDto;
import com.zerogift.member.domain.Member;
import java.util.List;
import java.util.Optional;

public interface GiftBoxRepositoryCustom {

    List<GiftBoxDto> findByIsUseEqFalse(Member member, MyPageableDto myPageableDto);

    GiftBoxDetail findByGiftBoxId(Long giftBoxId, Member member);

    Optional<GiftBox> findGiftBoxById(Long giftBoxId);

}
