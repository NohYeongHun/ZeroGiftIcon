package com.zerogift.backend.config;

import com.zerogift.backend.common.dto.MyPageableDto;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


import java.util.Collections;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
                                typeResolver.resolve(MyPageableDto.class))
                )
                .useDefaultResponseMessages(true)
                .securitySchemes(Collections.singletonList(apiKey()))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zerogift.backend"))
                .paths(PathSelectors.any())
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Zero GiftIcon API Docs")
                .description("API")
                .version("0.1")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private final TypeResolver typeResolver = new TypeResolver();

}