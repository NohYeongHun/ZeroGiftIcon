package com.zerogift;

import com.zerogift.global.config.FeignConfiguration;
import com.zerogift.global.config.JpaAuditConfig;
import com.zerogift.global.config.QuerydslConfig;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@Import({QuerydslConfig.class, JpaAuditConfig.class, FeignConfiguration.class})
public class BatchTestConfig {
}
