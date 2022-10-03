package com.zerogift.pay.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.pay.application.dto.SaleStatisticsDto;
import java.util.List;

public interface PayHistoryRepositoryCustomer {

    List<SaleStatisticsDto> findStatisticBySeller(Member seller);

}
