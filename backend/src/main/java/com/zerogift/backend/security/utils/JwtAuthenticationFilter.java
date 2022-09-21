package com.zerogift.backend.security.utils;

import com.zerogift.backend.common.dto.ErrorResultDto;
import com.zerogift.backend.common.exception.code.JwtErrorCode;
import com.zerogift.backend.common.exception.member.JwtInvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zerogift.backend.security.utils.JwtInfo.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		authenticate(request, getJwtFrom(request));

		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request, String token) {
		if (StringUtils.hasText(token)) {
			try {
				JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
				Authentication authentication = authenticationManager.authenticate(
					jwtAuthenticationToken);
				authentication.getPrincipal();
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} catch (AuthenticationException e) {
				SecurityContextHolder.clearContext();

				JwtInvalidException jwtInvalidException = (JwtInvalidException) e;

				ErrorResultDto error = ErrorResultDto.builder()
					.errorCode(jwtInvalidException.getErrorCode().name())
					.errorDescription(jwtInvalidException.getErrorMessage())
					.build();

				request.setAttribute("errorResult", error);
			}
		}
	}

	private String getJwtFrom(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken)) {
			return checkBearerToken(bearerToken);
		}
		// 선물함의 바코드를 사용할때는 JWT가 필요없어 Null로 반환
		return null;
	}

	private String checkBearerToken(String bearerToken) {
		if(!bearerToken.startsWith(BEARER_PREFIX)) {
			throw new JwtInvalidException(JwtErrorCode.JWT_FORMAT_IS_WRONG);
		}
		return bearerToken.substring(BEARER_PREFIX.length());
	}


}
