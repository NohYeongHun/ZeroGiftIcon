package com.zerogift.backend.security.service;

import com.zerogift.backend.security.api.WebClientApi;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.common.exception.member.OAuthException;
import com.zerogift.backend.common.exception.code.OAuthErrorCode;
import com.zerogift.backend.common.type.AuthType;
import com.zerogift.backend.member.entity.Member;
import com.zerogift.backend.member.repository.MemberRepository;
import com.zerogift.backend.member.type.MemberStatus;
import com.zerogift.backend.security.dto.OAuth2UserInfo;
import com.zerogift.backend.security.dto.OAuth2UserInfoFactory;
import com.zerogift.backend.security.dto.OAuthTokenResponse;
import com.zerogift.backend.security.dto.TokenDto;
import com.zerogift.backend.security.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final WebClientApi webClientApi;

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

    @Transactional
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