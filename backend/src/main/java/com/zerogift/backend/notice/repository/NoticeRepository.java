package com.zerogift.backend.notice.repository;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByMember(Member member);

    List<Notice> findByMemberAndIsView(Member member, Boolean isView);
}
