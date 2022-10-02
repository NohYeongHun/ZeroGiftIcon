package com.zerogift.gift.repository;

import com.zerogift.gift.domain.GiftMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftMessageRepository extends JpaRepository<GiftMessage, Long>, GiftMessageRepositoryCustom {
}
