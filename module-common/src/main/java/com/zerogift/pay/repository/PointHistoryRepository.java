package com.zerogift.pay.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.pay.domain.PointHistory;
import com.zerogift.pay.domain.PointType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    boolean existsByMemberAndPointTypeAndPointTypeId(Member member, PointType review, Long id);
}
