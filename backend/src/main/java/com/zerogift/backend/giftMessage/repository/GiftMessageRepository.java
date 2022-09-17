package com.zerogift.backend.giftMessage.repository;

import com.zerogift.backend.giftMessage.entity.GiftMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftMessageRepository extends JpaRepository<GiftMessage, Long>, GiftMessageRepositoryCustom {
}
