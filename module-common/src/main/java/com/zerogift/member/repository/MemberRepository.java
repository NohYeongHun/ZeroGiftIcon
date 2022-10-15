package com.zerogift.member.repository;

import com.zerogift.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
}
