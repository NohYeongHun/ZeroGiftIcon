package com.zerogift.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.zerogift.batch.infrastructure")
public class FeignConfiguration {
}
