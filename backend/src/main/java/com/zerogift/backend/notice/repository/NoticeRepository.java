package com.zerogift.backend.notice.repository;

import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByToMember(Member member);

    List<Notice> findByToMemberAndIsView(Member member, Boolean isView);

    Optional<Notice> findByIdAndToMember(Long noticeId, Member member);
}
