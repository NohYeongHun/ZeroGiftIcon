package com.zerogift.unit;

import static com.zerogift.batch.infrastructure.dto.ResultTitle.ERROR_MESSAGE;
import static com.zerogift.batch.infrastructure.dto.ResultTitle.EXECUTE_TIME;
import static com.zerogift.batch.infrastructure.dto.ResultTitle.LAUNCH_JOB_NAME;
import static com.zerogift.batch.infrastructure.dto.ResultTitle.STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import com.zerogift.batch.infrastructure.dto.ResultListenerDto;
import com.zerogift.batch.infrastructure.dto.Status;
import com.zerogift.batch.infrastructure.dto.SlackRequestDto;
import java.util.StringJoiner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SlackRequestDtoTest {

    @DisplayName("Job Result을 주면 SlackRequest로 변환 합니다.")
    @Test
    void executeTimeTest() {
        ResultListenerDto resultListenerDto = createResultDto();

        SlackRequestDto slackRequestDto = SlackRequestDto.from(resultListenerDto);

        assertThat(slackRequestDto.getText()).isEqualTo(getText(resultListenerDto));
    }

    private ResultListenerDto createResultDto() {
        return ResultListenerDto.builder()
            .status(Status.OK)
            .executeTime("1분/1초")
            .launchJobName("통계 잡")
            .errorMessage("커스텀 에러 발생")
            .build();
    }

    private String getText(ResultListenerDto resultListenerDto) {
        StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

        return stringJoiner.add(LAUNCH_JOB_NAME.getTitle() + resultListenerDto.getLaunchJobName())
            .add(STATUS.getTitle() + resultListenerDto.getStatus().name())
            .add(EXECUTE_TIME.getTitle() + resultListenerDto.getExecuteTime())
            .add(getErrorMessage(resultListenerDto))
            .toString();
    }

    private static String getErrorMessage(ResultListenerDto resultListenerDto) {
        if (resultListenerDto.isEmptyMessage()) {
            return "";
        }
        return ERROR_MESSAGE.getTitle() + resultListenerDto.getErrorMessage();
    }

}
