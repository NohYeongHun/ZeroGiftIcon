package com.zerogift.backend.security.utils;


import com.zerogift.backend.security.dto.AdminInfo;
import com.zerogift.backend.security.dto.MemberInfo;
import com.zerogift.backend.common.exception.JwtInvalidException;
import com.zerogift.backend.common.exception.code.JwtErrorCode;
import com.zerogift.backend.security.service.TokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;


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

		if (claims.get(JwtInfo.KEY_ROLES,String.class).equals("ROLE_ADMIN")) {

			AdminInfo info = AdminInfo.of(claims);

			return new JwtAuthenticationToken(AdminInfo.of(claims), "",
					Arrays.asList(new SimpleGrantedAuthority(info.getRole())));
		}

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
						Arrays.asList(new SimpleGrantedAuthority(info.getRole())));
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
