package com.zerogift.batch.job.statistics;

import static com.zerogift.batch.core.entity.member.QMember.member;
import static com.zerogift.batch.core.entity.pay.QPayHistory.payHistory;
import static com.zerogift.batch.job.constant.JobConstant.SALE_STATISTICS_JOB;

import com.zerogift.batch.core.entity.email.EmailMessage;
import com.zerogift.batch.core.entity.member.Member;
import com.zerogift.batch.core.service.statistics.SaleStatisticsService;
import com.zerogift.batch.job.listener.SlackJobListener;
import java.time.LocalDateTime;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.querydsl.reader.QuerydslNoOffsetPagingItemReader;
import org.springframework.batch.item.querydsl.reader.expression.Expression;
import org.springframework.batch.item.querydsl.reader.options.QuerydslNoOffsetNumberOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class SaleStatisticsConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;

    @Bean
    public Job saleStatisticsJob(Step saleStatisticsStep,
        SlackJobListener slackJobListener) {
        return jobBuilderFactory.get(SALE_STATISTICS_JOB.getJobName())
            .incrementer(new RunIdIncrementer())
            .start(saleStatisticsStep)
            .listener(slackJobListener)
            .build();
    }

    @JobScope
    @Bean
    public Step saleStatisticsStep(
        QuerydslNoOffsetPagingItemReader<Member> saleStatisticsStepItemReader,
        ItemProcessor<Member, EmailMessage> saleStatisticsStepItemProcessor,
        ItemWriter<EmailMessage> payHistoryWriter,
        TaskExecutor taskExecutor) {
        return stepBuilderFactory.get("saleStatisticsStep")
            .<Member, EmailMessage>chunk(10)
            .reader(saleStatisticsStepItemReader)
            .processor(saleStatisticsStepItemProcessor)
            .writer(payHistoryWriter)
            .taskExecutor(taskExecutor)
            .build();
    }


    @StepScope
    @Bean
    public QuerydslNoOffsetPagingItemReader<Member> saleStatisticsStepItemReader() {
        QuerydslNoOffsetNumberOptions<Member, Long> options =
            new QuerydslNoOffsetNumberOptions<>(member.id, Expression.ASC);

        LocalDateTime beforeMonthDay = LocalDateTime.now().minusMonths(1);

        return new QuerydslNoOffsetPagingItemReader<>(emf, 10, options, queryFactory -> queryFactory
            .select(member)
            .from(payHistory)
            .innerJoin(payHistory.seller, member)
            .where(payHistory.payDate.between(
                LocalDateTime.of(beforeMonthDay.getYear(), beforeMonthDay.getMonth(), 1, 0, 0),
                LocalDateTime.of(beforeMonthDay.getYear(), beforeMonthDay.getMonth(),
                    beforeMonthDay.toLocalDate().lengthOfMonth(), 23, 59)
            ))
            .groupBy(member)

        );
    }

    @StepScope
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("async-thread");
        return taskExecutor;
    }

    @StepScope
    @Bean
    public ItemProcessor<Member, EmailMessage> saleStatisticsStepItemProcessor(
        SaleStatisticsService saleStatisticsService) {
        return saleStatisticsService::convertEmailTemplate;
    }

    @Bean
    public ItemWriter<EmailMessage> payHistoryWriter(SaleStatisticsService saleStatisticsService) {
        return emails -> emails.forEach(saleStatisticsService::saveMessageQueue);
    }

}
