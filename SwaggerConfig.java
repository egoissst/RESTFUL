package com.example.demo;


import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@ComponentScan(basePackages = {"io.swagger", "org.openapitools.api" , "io.swagger.config"})
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());                                           
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfo(
          "Online Ordering  APIs", 
          "Some custom description of API.", 
          "api v1", 
          "Terms of service", 
          new Contact("Banuprakash C", "http://lucidatechnologies.com", "banu@lucidatechnologies.com"), 
          "License of API", 
          "API license URL", 
          Collections.emptyList());
   }
}
