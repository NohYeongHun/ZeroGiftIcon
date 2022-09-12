package com.zerogift.batch.core.repository.pay;

import com.zerogift.batch.core.entity.pay.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long>, PayHistoryRepositoryCustomer {
}
