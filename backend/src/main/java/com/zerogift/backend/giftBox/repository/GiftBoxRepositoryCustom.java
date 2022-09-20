package com.zerogift.backend.giftBox.repository;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.giftBox.dto.GiftBoxDetail;
import com.zerogift.backend.giftBox.dto.GiftBoxDto;
import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface GiftBoxRepositoryCustom {

    Page<GiftBoxDto> findByIsUseEqFalse(Member member, MyPageableDto myPageableDto);

    GiftBoxDetail findByGiftBoxId(Long giftBoxId, Member member);

    Optional<GiftBox> findGiftBoxById(Long giftBoxId);

}
