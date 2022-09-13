package com.zerogift.batch.application.client.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultTitle {
    LAUNCH_JOB_NAME("실행 잡"),
    EXECUTE_TIME("실행 시간"),
    STATUS("실행결과"),
    ERROR_MESSAGE("에러 메시지");

    private String title;

    private static final String DELIMETER = ": ";

    public String getTitle() {
        return title + DELIMETER;
    }

}
