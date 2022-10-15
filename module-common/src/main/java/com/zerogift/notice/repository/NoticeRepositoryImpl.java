package com.zerogift.notice.repository;

import static com.zerogift.member.domain.QMember.member;
import static com.zerogift.notice.domain.QNotice.notice;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.QMember;
import com.zerogift.notice.application.dto.NoticeResponse;
import com.zerogift.notice.domain.QNotice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeResponse> findByNoticeList(Member loginMember, boolean view) {
        return queryFactory
            .select(Projections.constructor(NoticeResponse.class,
                notice.noticeType,
                notice.noticeTypeId,
                member.nickname,
                notice.message,
                notice.createdDate
            ))
            .from(notice)
            .innerJoin(notice.toMember, member)
            .where(member.eq(loginMember),
                eqIsView(view))
            .fetch();
    }

    private BooleanExpression eqIsView(boolean view) {
        if(view == true) {
            return null;
        }
        return notice.isView.eq(view);
    }

}
