package com.zerogift.batch;

import com.zerogift.batch.config.QuerydslConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
@Import(QuerydslConfiguration.class)
public class BatchTestConfig {
}
