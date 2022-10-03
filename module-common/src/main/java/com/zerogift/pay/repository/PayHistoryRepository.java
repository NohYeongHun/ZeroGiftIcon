package com.zerogift.pay.repository;

import com.zerogift.pay.domain.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long>, PayHistoryRepositoryCustomer {
}
