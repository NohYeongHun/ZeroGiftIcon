package com.zerogift.member.repository;

import static com.zerogift.member.domain.QMember.member;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.support.dto.MyPageableDto;
import com.zerogift.member.application.dto.MemberSearchCondition;
import com.zerogift.member.application.dto.MemberSearchDetail;
import com.zerogift.member.application.dto.MemberSearchOutputDto;
import com.zerogift.member.domain.MemberStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository {

    private final JPAQueryFactory queryFactory;
    public List<MemberSearchOutputDto> searchMemberList(MemberSearchCondition condition, MyPageableDto myPageableDto){

        return queryFactory
            .select(Projections.constructor(MemberSearchOutputDto.class,
                member.id,
                member.profileImageUrl,
                member.nickname
            ))
            .from(member)
            .where(
                memberEmailContains(condition.getEmail()),
                memberNicknameContains(condition.getNickname()),
                memberStatusPermitted()
            )
            .offset(currentOffset(myPageableDto))
            .limit(nextOffset(myPageableDto))
            .fetch();
    }

    public Long getTotalCount(MemberSearchCondition condition){
        Long count = queryFactory.select(member.count())
            .from(member)
            .where(
                memberEmailContains(condition.getEmail()),
                memberNicknameContains(condition.getNickname()),
                memberStatusPermitted()
            )
            .fetchOne();

        return count == null ? 0 : count;
    }

    private long currentOffset(MyPageableDto myPageableDto) {
        return (long) myPageableDto.getPage() * myPageableDto.getSize();
    }

    private long nextOffset(MyPageableDto myPageableDto) {
        return (long) (myPageableDto.getPage() + 1) * myPageableDto.getSize();
    }

    private BooleanExpression memberEmailContains(String email){
        return hasText(email) ? member.email.contains(email) : null;
    }

    private BooleanExpression memberNicknameContains(String nickname){
        return hasText(nickname) ? member.nickname.contains(nickname) : null;
    }

    private BooleanExpression memberStatusPermitted(){
        return hasText(MemberStatus.PERMITTED.name()) ? member.status.eq(MemberStatus.PERMITTED) : null;
    }

    public MemberSearchDetail searchMemberDetail(Long memberId){

        return queryFactory
            .select(Projections.constructor(MemberSearchDetail.class,
                member.id,
                member.nickname,
                member.email,
                member.point
            ))
            .from(member)
            .where(
                member.id.eq(memberId)
            )
            .fetchOne();
    }

}
