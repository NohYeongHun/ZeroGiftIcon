package com.zerogift.notice.repository;

import com.zerogift.member.domain.Member;
import com.zerogift.notice.application.dto.NoticeResponse;
import java.util.List;

public interface NoticeRepositoryCustom {
    List<NoticeResponse> findByNoticeList(Member member, boolean view);
}
