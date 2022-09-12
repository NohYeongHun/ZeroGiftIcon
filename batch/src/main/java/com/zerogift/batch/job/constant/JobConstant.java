package com.zerogift.batch.job.constant;

import com.zerogift.batch.exception.InvalidJobnameException;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JobConstant {
    GENERAL_EMAIL_SEND_JOB("generalEmailSendJob", "이메일 보내는 배치"),
    STATISTIC_EMAIL_SEND_JOB("statisticEmailSendJob", "판매자에게 월별 판매 현황 이메일 보내는 배치"),

    SALE_STATISTICS_JOB("saleStatisticsJob", "월별 판매 현황 배치");

    private final String jobName;
    private final String description;

    private final static String ERROR_MESSAGE = "존재하지 않는 Job Name 입니다.";

    public static String of(String jobName) {
        return Arrays.stream(values())
            .filter(job -> job.getJobName().equals(jobName))
            .findFirst()
            .map(job -> job.getDescription())
            .orElseThrow(() -> new InvalidJobnameException(ERROR_MESSAGE));
    }

}
