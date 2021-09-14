package com.adobe.prj.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.adobe.prj.service.NotFoundException;

@Configuration
@Aspect
public class LogAspect {
	Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	//https://docs.spring.io/spring-framework/docs/2.0.x/reference/aop.html
	@Before("execution(* com.adobe.prj.service.*.*(..))")
	public void doLogBefore(JoinPoint jp) {
		logger.info("Called :" + jp.getSignature());
		Object[] args = jp.getArgs();
		for(Object arg : args) {
			logger.info("argument : " + arg);
		}
	}
	
	@After("execution(* com.adobe.prj.service.*.*(..))")
	public void doLogAfter(JoinPoint jp) {
		logger.info("*********************");
	}
	
}
