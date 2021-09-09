package com.adobe.demo.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.adobe.demo.service.EmailService;

@Configuration
public class MyConfig {
	
	@Value("${url}")
	private String url;
	
	@Bean
	public EmailService getEmailService() {
		return new EmailService(url, 120);
	}
}
