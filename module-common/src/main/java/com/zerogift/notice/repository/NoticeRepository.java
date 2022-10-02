package com.zerogift.notice.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.notice.domain.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findByToMember(Member member);

    List<Notice> findByToMemberAndIsView(Member member, Boolean isView);

    Optional<Notice> findByIdAndToMember(Long noticeId, Member member);
}
