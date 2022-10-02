package com.zerogift.batch.job;

import static com.zerogift.batch.infrastructure.dto.JobConstant.STATISTIC_EMAIL_SEND_JOB;
import static com.zerogift.email.domain.QEmailMessage.emailMessage;

import com.zerogift.batch.application.SendMailService;
import com.zerogift.email.domain.EmailMessage;
import com.zerogift.email.domain.MessageStatus;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.querydsl.reader.QuerydslNoOffsetPagingItemReader;
import org.springframework.batch.item.querydsl.reader.expression.Expression;
import org.springframework.batch.item.querydsl.reader.options.QuerydslNoOffsetNumberOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StatisticEmailSendConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Bean
    public Job statisticEmailSendJob(Step statisticEmailSendStep,
        SlackJobListener slackJobListener) {
        return jobBuilderFactory.get(STATISTIC_EMAIL_SEND_JOB.getJobName())
            .incrementer(new RunIdIncrementer())
            .start(statisticEmailSendStep)
            .listener(slackJobListener)
            .build();
    }

    @JobScope
    @Bean
    public Step statisticEmailSendStep(
        QuerydslNoOffsetPagingItemReader<EmailMessage> statisticEmailSendStepItemReader,
        ItemWriter<EmailMessage> statisticEmailSendStepItemWriter) {
        return stepBuilderFactory.get("statisticEmailSendStep")
            .<EmailMessage, EmailMessage>chunk(10)
            .reader(statisticEmailSendStepItemReader)
            .writer(statisticEmailSendStepItemWriter)
            .build();
    }

    @StepScope
    @Bean
    public QuerydslNoOffsetPagingItemReader<EmailMessage> statisticEmailSendStepItemReader() {
        QuerydslNoOffsetNumberOptions<EmailMessage, Long> options =
            new QuerydslNoOffsetNumberOptions<>(emailMessage.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, 10, options, queryFactory -> queryFactory
            .selectFrom(emailMessage)
            .where(emailMessage.send.eq(false)
                .and(emailMessage.status.eq(MessageStatus.STATISTIC)))
        );
    }

    @Bean
    public ItemWriter<EmailMessage> statisticEmailSendStepItemWriter(SendMailService sendMailService) {
        return emails -> emails.forEach(sendMailService::sendMailTemplate);
    }


}
