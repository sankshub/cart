package com.sank.bookshop.frontlayer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class Swagger2UiConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false)
                                                      .apiInfo(metaInfo())
                                                      .select()
                                                      .apis(RequestHandlerSelectors.basePackage("com.sank.bookshop.frontlayer.controller"))
                                                      .build();
    }

    private ApiInfo metaInfo() {
        return new ApiInfo("Online Books Store API Services",
                "get available books and its discounted price while ordering ", "1.0", "Terms of Service", null,
                "Apache License Version 2.0", "https://www.apache.org/licesen.html", new ArrayList<>());
    }

}
