package com.zerogift.support.auth.oauth.application;

import com.zerogift.member.domain.AuthType;
import com.zerogift.member.domain.Member;
import com.zerogift.member.repository.MemberRepository;
import com.zerogift.member.domain.MemberStatus;
import com.zerogift.member.domain.Role;
import com.zerogift.global.error.OAuthException;
import com.zerogift.global.error.dto.OAuthErrorCode;
import com.zerogift.security.infrastructure.WebClientApi;
import com.zerogift.support.auth.oauth.OAuth2UserInfo;
import com.zerogift.support.auth.oauth.OAuth2UserInfoFactory;
import com.zerogift.support.auth.oauth.OAuthTokenResponse;
import com.zerogift.support.auth.token.TokenDto;
import com.zerogift.support.auth.userdetails.MemberInfo;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final WebClientApi webClientApi;

    @Transactional
    public TokenDto login(String providerName, String code) {
        AuthType authType = AuthType.of(providerName);

        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OAuthTokenResponse tokenResponse = webClientApi.getToken(code, provider);

        Map<String, Object> userAttributes = webClientApi.getUserAttributes(tokenResponse, provider);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            authType, userAttributes);

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuthException(OAuthErrorCode.EMAIL_NOT_FOUND);
        }

        Member member = getMember(authType, oAuth2UserInfo);
        MemberInfo memberInfo = MemberInfo.of(member);

        return tokenService.issueAllToken(memberInfo);
    }

    private Member getMember(AuthType authType, OAuth2UserInfo oAuth2UserInfo) {
        Optional<Member> memberOptional =
            memberRepository.findByEmail(oAuth2UserInfo.getEmail());
        Member member;

        if (memberOptional.isPresent()) {
            member = memberOptional.get();
            validateRequestAuthTypeAndMemberAuthType(authType, member);
        } else {
            member = registerNewMember(oAuth2UserInfo);
        }
        return member;
    }

    private void validateRequestAuthTypeAndMemberAuthType(AuthType authType, Member member) {
        if (!authType.equals(member.getAuthType())) {
            throw new OAuthException(OAuthErrorCode.EMAIL_ALREADY_SIGNED_UP);
        }
    }

    private Member registerNewMember(OAuth2UserInfo oAuth2UserInfo) {
        Member member = Member.builder()
            .email(oAuth2UserInfo.getEmail())
            .nickname(oAuth2UserInfo.getName())
            .role(Role.ROLE_MEMBER)
            .status(MemberStatus.PERMITTED)
            .authType(oAuth2UserInfo.getAuthType())
            .authId(oAuth2UserInfo.getAuthId())
            .profileImageUrl(oAuth2UserInfo.getProfileImageUrl())
            .build();

        return memberRepository.save(member);
    }

}
