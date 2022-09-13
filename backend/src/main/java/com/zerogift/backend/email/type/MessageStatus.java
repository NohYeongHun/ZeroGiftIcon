package com.zerogift.backend.email.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {
    STATISTIC("월별 정산"),
    GENERAL("일반");

    private final String description;
}
