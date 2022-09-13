package com.zerogift.batch.application.client.dto;

import static com.zerogift.batch.application.client.dto.ResultTitle.ERROR_MESSAGE;
import static com.zerogift.batch.application.client.dto.ResultTitle.EXECUTE_TIME;
import static com.zerogift.batch.application.client.dto.ResultTitle.LAUNCH_JOB_NAME;
import static com.zerogift.batch.application.client.dto.ResultTitle.STATUS;

import com.zerogift.batch.core.dto.ResultListenerDto;
import java.util.StringJoiner;
import lombok.Getter;

@Getter
public class SlackRequestDto {
    private final String text;

    private final static String EMPTY = "";

    public SlackRequestDto(String text) {
        this.text = text;
    }

    public static SlackRequestDto from(ResultListenerDto resultListenerDto) {
        return new SlackRequestDto(getSlackText(resultListenerDto));
    }

    private static String getSlackText(ResultListenerDto resultListenerDto) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

        return stringJoiner.add(LAUNCH_JOB_NAME.getTitle() + resultListenerDto.getLaunchJobName())
            .add(STATUS.getTitle() + resultListenerDto.getStatus().name())
            .add(EXECUTE_TIME.getTitle() + resultListenerDto.getExecuteTime())
            .add(getErrorMessage(resultListenerDto))
            .toString();
    }

    private static String getErrorMessage(ResultListenerDto resultListenerDto) {
        if(resultListenerDto.isEmptyMessage()) {
            return EMPTY;
        }
        return ERROR_MESSAGE.getTitle() + resultListenerDto.getErrorMessage();
    }

}
