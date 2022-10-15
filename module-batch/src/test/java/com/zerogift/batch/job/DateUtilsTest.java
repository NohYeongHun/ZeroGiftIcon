package com.zerogift.batch.job;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

    @DisplayName("Date를 LocalDateTime으로 변환한다.")
    @Test
    void getLocalDateParseTest() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime localDateTime = DateUtils.getLocalDateParse(Timestamp.valueOf(now));

        assertThat(now).isEqualTo(localDateTime);
    }

    @DisplayName("실행시간을 반환한다.")
    @Test
    void executeTimeTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plusOneMinusAndOneSeconds = now.plusMinutes(1)
            .plusSeconds(1);

        String executeTime = DateUtils.executeTime(now, plusOneMinusAndOneSeconds);

        assertThat(executeTime).isEqualTo("1분/1초");
    }

}
