package com.zerogift.backend.pay.repository;

import com.zerogift.backend.pay.entity.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long> {
}
