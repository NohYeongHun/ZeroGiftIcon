package com.zerogift.backend.member.service;

import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.member.dto.MemberSearchDetail;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.repository.MemberSearchRepository;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.dto.AdminInfo;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.security.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberSearchServiceTest {

    @Autowired
    private MemberSearchRepository memberSearchRepository;

    @Autowired
    private MemberSearchService memberSearchService;

    @Autowired
    private MemberRepository memberRepository;

    Member member;
    MemberInfo memberInfo;

    @DisplayName("회원 정보 조회 - email, nickname, point")
    @Test
    void findByMemberDetail(){
        //given
        member = memberRepository.save(Member.builder()
                .email("example@example.com")
                .nickname("example")
                .status(MemberStatus.PERMITTED)
                .authType(AuthType.KAKAO)
                .authId("test_id")
                .profileImageUrl("https://test.com")
                .role(Role.ROLE_ADMIN)
                .build());


        memberInfo = MemberInfo.builder()
                .id(member.getId())
                .role(member.getRole().name())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .status(member.getStatus().name())
                .build();

        //when
        MemberSearchDetail memberSearchDetail = memberSearchService.searchMemberDetail(memberInfo.getId());
        MemberSearchDetail compareSearchDetail = MemberSearchDetail.builder()
                .id(1L)
                .email("example@example.com")
                .nickname("example")
                .point(0)
                .build();

        //then
        assertThat(memberSearchDetail).isEqualTo(compareSearchDetail);
    }

}