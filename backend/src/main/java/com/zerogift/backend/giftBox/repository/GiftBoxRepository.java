package com.zerogift.backend.giftBox.repository;

import com.zerogift.backend.giftBox.entity.GiftBox;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiftBoxRepository extends JpaRepository<GiftBox, Long>, GiftBoxRepositoryCustom {

    List<GiftBox> findByRecipientMemberAndProductAndReview(Member member, Product product, Boolean review);

}
