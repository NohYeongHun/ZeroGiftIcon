package com.example.demo.security.service;

import com.example.demo.security.dto.MemberInfo;
import com.example.demo.common.exception.OAuthException;
import com.example.demo.common.exception.code.OAuthErrorCode;
import com.example.demo.common.type.AuthType;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.member.type.MemberStatus;
import com.example.demo.security.dto.OAuth2UserInfo;
import com.example.demo.security.dto.OAuth2UserInfoFactory;
import com.example.demo.security.dto.OAuthTokenResponse;
import com.example.demo.security.dto.TokenDto;
import com.example.demo.security.type.Role;
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

    public TokenDto login(String providerName, String code) {
        AuthType authType = AuthType.of(providerName);

        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OAuthTokenResponse tokenResponse = getToken(code, provider);

        Map<String, Object> userAttributes = getUserAttributes(tokenResponse, provider);

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

    private OAuthTokenResponse getToken(String code, ClientRegistration provider) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setBasicAuth(provider.getClientId(),provider.getClientSecret());
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_id", provider.getClientId());
        formData.add("client_secret",provider.getClientSecret());
        return formData;
    }

    private Map<String, Object> getUserAttributes(OAuthTokenResponse tokenResponse,
                                                  ClientRegistration provider) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccess_token()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
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