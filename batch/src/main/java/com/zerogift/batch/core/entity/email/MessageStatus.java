package com.zerogift.batch.core.entity.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatus {
    STATISTIC("월별 정산"),
    GENERAL("일반");

    private final String description;

}
