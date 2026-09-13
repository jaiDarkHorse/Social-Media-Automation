package com.projectdarkhope.SocialMediaAutomationApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SocialMediaAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaAutomationApplication.class, args);
	}

}
