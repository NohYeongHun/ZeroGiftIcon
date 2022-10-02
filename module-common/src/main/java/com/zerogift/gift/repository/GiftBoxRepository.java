package com.zerogift.gift.repository;

import com.zerogift.gift.domain.GiftBox;
import com.zerogift.member.domain.Member;
import com.zerogift.product.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GiftBoxRepository extends JpaRepository<GiftBox, Long>, GiftBoxRepositoryCustom {

    List<GiftBox> findByRecipientMemberAndProductAndReview(Member member, Product product, Boolean review);
}
