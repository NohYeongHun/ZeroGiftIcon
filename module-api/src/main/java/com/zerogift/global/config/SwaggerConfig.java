package com.zerogift.global.config;

import com.fasterxml.classmate.TypeResolver;
import com.zerogift.support.auth.authorization.AuthenticationPrincipal;
import com.zerogift.support.dto.MyPageableDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .alternateTypeRules(
                AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class),
                    typeResolver.resolve(MyPageableDto.class))
            )
            .ignoredParameterTypes(
                AuthenticationPrincipal.class)   // AuthenticationPrincipal 파라미터 제외
            .useDefaultResponseMessages(true)
            .securitySchemes(Collections.singletonList(apiKey()))
            .securityContexts(Collections.singletonList(securityContext()))
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.zerogift"))
            .paths(PathSelectors.any())
            .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Zero GiftIcon API Docs")
            .description("API")
            .version("0.2")
            .build();
    }

    private SecurityContext securityContext() {
        return springfox
            .documentation
            .spi.service
            .contexts
            .SecurityContext
            .builder()
            .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global",
            "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private final TypeResolver typeResolver = new TypeResolver();

}
