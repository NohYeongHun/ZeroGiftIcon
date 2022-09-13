package com.zerogift.batch.core.repository.pay;

import com.zerogift.batch.core.dto.SaleStatisticsDto;
import com.zerogift.batch.core.entity.member.Member;
import java.util.List;

public interface PayHistoryRepositoryCustomer {

    List<SaleStatisticsDto> findStatisticBySeller(Member seller);

}
