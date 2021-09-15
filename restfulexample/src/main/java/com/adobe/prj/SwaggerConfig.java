package com.adobe.prj;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {    
	 public static final String PRODUCT_TAG = "product service";

    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.regex("/api/.*"))                          
          .build()
          .tags(new Tag(PRODUCT_TAG, "the product API with description api tag"))
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
 