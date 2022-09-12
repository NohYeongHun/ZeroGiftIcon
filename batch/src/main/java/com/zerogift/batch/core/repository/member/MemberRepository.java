package com.zerogift.batch.core.repository.member;

import com.zerogift.batch.core.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
