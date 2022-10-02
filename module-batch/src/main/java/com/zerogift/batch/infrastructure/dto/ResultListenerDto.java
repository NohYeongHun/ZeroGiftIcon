package com.zerogift.batch.infrastructure.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class ResultListenerDto {

    private final Status status;
    private final String executeTime;
    private final String launchJobName;
    private final String errorMessage;

    @Builder
    public ResultListenerDto(Status status, String executeTime, String launchJobName,
        String errorMessage) {
        this.status = status;
        this.executeTime = executeTime;
        this.launchJobName = launchJobName;
        this.errorMessage = errorMessage;
    }

    public boolean isEmptyMessage() {
        return !StringUtils.hasText(errorMessage);
    }

}
