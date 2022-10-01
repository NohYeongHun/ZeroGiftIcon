package com.zerogift.backend.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum NoticeErrorCode {
    NOTICE_NOT_FOUND(" 해당 알림을 찾을 수 없습니다. ");

    private final String description;
}
