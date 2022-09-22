package com.zerogift.backend.giftBox.repository;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.member.entity.Member;
import java.util.List;
import java.util.Optional;

public interface GiftBoxRepositoryCustom {

    List<GiftBoxDto> findByIsUseEqFalse(Member member, MyPageableDto myPageableDto);

    GiftBoxDetail findByGiftBoxId(Long giftBoxId, Member member);

    Optional<GiftBox> findGiftBoxById(Long giftBoxId);

}
