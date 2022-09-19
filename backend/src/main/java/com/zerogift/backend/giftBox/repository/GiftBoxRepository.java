package com.zerogift.backend.giftBox.repository;

import com.zerogift.backend.giftBox.entity.GiftBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftBoxRepository extends JpaRepository<GiftBox, Long>, GiftBoxRepositoryCustom {

}
