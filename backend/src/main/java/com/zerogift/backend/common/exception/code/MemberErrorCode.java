package com.zerogift.backend.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND("멤버를 찾을 수 없습니다.");
    private String description;
}
