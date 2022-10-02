package com.zerogift.gift.repository;

import com.zerogift.gift.application.dto.GiftMessageDto;
import com.zerogift.gift.application.dto.GiftMessageForm;
import java.util.Optional;

public interface GiftMessageRepositoryCustom {

    GiftMessageForm findByGiftMessageForm(Long giftBoxId, Long memberId);

    Optional<GiftMessageDto> findByGiftMessage(Long giftMessageId, Long MemberId);
}
