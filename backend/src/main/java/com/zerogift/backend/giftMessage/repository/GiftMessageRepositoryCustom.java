package com.zerogift.backend.giftMessage.repository;

import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.giftMessage.dto.GiftMessageDto;
import com.zerogift.backend.giftMessage.dto.GiftMessageForm;
import java.util.Optional;

public interface GiftMessageRepositoryCustom {

    GiftMessageForm findByGiftMessageForm(Long giftBoxId, Long memberId);

    Optional<GiftMessageDto> findByGiftMessage(Long giftMessageId, Long MemberId);

}
