package com.example.accessingdatamysql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

//@SpringBootApplication(scanBasePackages="com.example.accessingdatamysql")

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages="com.example.accessingdatamysql.controller;com.example.accessingdatamysql.service")
public class MainApplication {


	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainApplication.class);
        final ConfigurableApplicationContext context = app.run(args);

		System.out.println("--------------- BEAN DEFINITION ----------------");
        for (String beanName: context.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }	
		
		System.out.println("----------------- ENVIRONMENT ------------------");
		Environment env = context.getEnvironment();
		System.out.println("Started on port: " + env.getProperty("server.port"));
        	
	}

	@Bean
	public ResourceBundleMessageSource messageSource1() {  
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
		messageSource.setBasename("lang/msg");  
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;  
	} 

}
