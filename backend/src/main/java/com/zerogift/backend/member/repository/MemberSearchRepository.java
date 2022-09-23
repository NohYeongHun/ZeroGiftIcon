package com.zerogift.backend.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerogift.backend.common.dto.MyPageableDto;
import com.zerogift.backend.member.dto.MemberSearchOutputDto;
import com.zerogift.backend.member.entity.Member;

import com.zerogift.backend.member.repository.condition.MemberSearchCondition;
import com.zerogift.backend.member.type.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.zerogift.backend.member.entity.QMember.member;
import static org.springframework.util.StringUtils.hasText;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberSearchRepository {

    private final JPAQueryFactory queryFactory;

    public Page<MemberSearchOutputDto> searchByWhere(MemberSearchCondition condition, MyPageableDto myPageableDto){

        List<MemberSearchOutputDto> searchMembers = queryFactory
                .select(Projections.constructor(MemberSearchOutputDto.class, member.id,
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
        Pageable pageable = PageRequest.of(myPageableDto.getPage(), myPageableDto.getSize());

        return new PageImpl<>(searchMembers, pageable, searchMembers.size());
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

    private Long getTotalPage(MemberSearchCondition condition){
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
}
