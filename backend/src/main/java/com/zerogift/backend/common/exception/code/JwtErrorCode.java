package com.zerogift.backend.common.exception.code;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {
    INVALID_JWT_SIGNATURE("사용할 수 없는 토큰입니다."),
    EXPIRED_JWT("토큰의 유효기간이 만료되었습니다."),
    MEMBER_STATUS_BANNED("정지된 회원입니다."),
    MEMBER_STATUS_WAIT("닉네임 설정이 필요한 회원입니다."),
    MEMBER_STATUS_WITHDRAWAL("정지된 회원입니다.");

    private final String description;
}
