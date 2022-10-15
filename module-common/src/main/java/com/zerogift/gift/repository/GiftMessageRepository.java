package com.zerogift.gift.repository;

import com.zerogift.gift.domain.GiftMessage;
import com.zerogift.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftMessageRepository extends JpaRepository<GiftMessage, Long>, GiftMessageRepositoryCustom {
    List<GiftMessage> findByToMember(Member member);
}
