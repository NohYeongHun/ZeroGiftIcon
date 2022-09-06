package com.example.demo.security.jwt;


import com.example.demo.dto.common.MemberInfo;
import com.example.demo.exception.common.JwtInvalidException;
import com.example.demo.exception.common.code.JwtErrorCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final TokenService tokenService;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		Claims claims =
				tokenService.parseAccessToken(((JwtAuthenticationToken) authentication).getJwt());

		MemberInfo info = MemberInfo.of(claims);

		switch (info.getStatus()) {
			case "BANNED":
				throw new JwtInvalidException(JwtErrorCode.MEMBER_STATUS_BANNED);
			case "WITHDRAWAL":
				throw new JwtInvalidException(JwtErrorCode.MEMBER_STATUS_WITHDRAWAL);
			case "WAIT":
				throw new JwtInvalidException(JwtErrorCode.MEMBER_STATUS_WAIT);
			default:
				return new JwtAuthenticationToken(info, "",
						Collections.singletonList(new SimpleGrantedAuthority(info.getRole())));
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
