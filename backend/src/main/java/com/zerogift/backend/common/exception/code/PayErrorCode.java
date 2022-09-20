package com.zerogift.backend.common.exception.code;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PayErrorCode {
    NOT_ENOUGH_POINT("사용 포인트가 보유 포인트보다 많습니다.");

    private final String description;
}
