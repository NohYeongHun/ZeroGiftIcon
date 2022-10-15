package com.zerogift.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeErrorCode {
    NOTICE_NOT_FOUND(" 해당 알림을 찾을 수 없습니다. ");

    private final String description;
}
