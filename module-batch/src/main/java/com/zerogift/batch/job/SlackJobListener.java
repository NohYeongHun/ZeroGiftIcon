package com.zerogift.batch.job;

import com.zerogift.batch.infrastructure.dto.JobConstant;
import com.zerogift.batch.infrastructure.dto.ResultListenerDto;
import com.zerogift.batch.infrastructure.dto.Status;
import com.zerogift.batch.infrastructure.SlackEvent;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackJobListener implements JobExecutionListener {
    private final SlackEvent slackEvent;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("[START] =====> {} ", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String launchJob = jobExecution.getJobInstance().getJobName();

        ResultListenerDto resultListenerDto = ResultListenerDto.builder()
            .status(getStatus(jobExecution))
            .executeTime(getExecuteTime(jobExecution))
            .errorMessage(jobExecution.getExitStatus().getExitDescription())
            .launchJobName(JobConstant.of(launchJob))
            .build();
        slackEvent.send(resultListenerDto);

        log.info("[END] ======> {} ", jobExecution.getJobInstance().getJobName());
    }

    private Status getStatus(JobExecution jobExecution) {
        return jobExecution.getStatus() == BatchStatus.COMPLETED ? Status.OK : Status.FAIL;
    }

    private String getExecuteTime(JobExecution jobExecution) {
        LocalDateTime startDate = DateUtils.getLocalDateParse(jobExecution.getStartTime());
        LocalDateTime endDate = DateUtils.getLocalDateParse(jobExecution.getEndTime());

        return DateUtils.executeTime(startDate, endDate);
    }

}
