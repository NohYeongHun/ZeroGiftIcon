package com.zerogift.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다."),
    PASSWORD_WRONG("비밀번호가 잘못되었습니다."),
    EMAIL_VERIFICATION_HAS_NOT_BEEN_COMPLETED("이메일 인증이 완료되지 않았습니다."),
    SUSPEND_USER("정지된 사용자입니다."),
    WITHDRAW_USER("탈퇴한 사용자입니다."),
    SAME_EMAIL_EXISTS("동일한 이메일이 존재합니다."),
    SAME_NICKNAME_EXISTS("동일한 닉네임이 존재합니다.");
    private String description;
}
