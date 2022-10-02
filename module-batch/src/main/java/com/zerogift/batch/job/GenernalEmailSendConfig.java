package com.zerogift.batch.job;

import static com.zerogift.batch.infrastructure.dto.JobConstant.GENERAL_EMAIL_SEND_JOB;
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
public class GenernalEmailSendConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Bean
    public Job generalEmailSendJob(Step generalEmailSendStep,
        LoggerJobListener loggerJobListener) {
        return jobBuilderFactory.get(GENERAL_EMAIL_SEND_JOB.getJobName())
            .incrementer(new RunIdIncrementer())
            .start(generalEmailSendStep)
            .listener(loggerJobListener)
            .build();
    }

    @JobScope
    @Bean
    public Step generalEmailSendStep(
        QuerydslNoOffsetPagingItemReader<EmailMessage> generalEmailSendStepItemReader,
        ItemWriter<EmailMessage> generalEmailSendStepItemWriter) {
        return stepBuilderFactory.get("generalEmailSendStep")
            .<EmailMessage, EmailMessage>chunk(10)
            .reader(generalEmailSendStepItemReader)
            .writer(generalEmailSendStepItemWriter)
            .build();
    }

    @StepScope
    @Bean
    public QuerydslNoOffsetPagingItemReader<EmailMessage> generalEmailSendStepItemReader() {
        QuerydslNoOffsetNumberOptions<EmailMessage, Long> options =
            new QuerydslNoOffsetNumberOptions<>(emailMessage.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, 10, options, queryFactory -> queryFactory
            .selectFrom(emailMessage)
            .where(emailMessage.send.eq(false)
                .and(emailMessage.status.eq(MessageStatus.GENERAL)))
        );
    }

    @Bean
    public ItemWriter<EmailMessage> generalEmailSendStepItemWriter(SendMailService sendMailService) {
        return emails -> emails.forEach(sendMailService::sendMail);
    }


}
