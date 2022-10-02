package com.zerogift.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.zerogift.acceptance.AcceptanceTest;
import com.zerogift.member.application.MemberSearchService;
import com.zerogift.member.application.dto.MemberSearchDetail;
import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.member.repository.MemberSearchRepository;
import com.zerogift.support.auth.userdetails.MemberInfo;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberSearchServiceTest extends AcceptanceTest {

    @Autowired
    private MemberSearchRepository memberSearchRepository;

    @Autowired
    private MemberSearchService memberSearchService;

    @Autowired
    private MemberRepository memberRepository;

    Member member;
    MemberInfo memberInfo;

    @BeforeEach
    public void setUp() throws IOException {
        super.setUp();
    }


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