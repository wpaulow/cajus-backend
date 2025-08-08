package com.cajusrh.recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
	    "com.cajusrh.recruitment.core",
	    "com.cajusrh.recruitment.application",
	    "com.cajusrh.recruitment.infrastructure",
	    "com.cajusrh.recruitment.api",
	    "com.cajusrh.recruitment.config"
})
public class RecruitmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentApplication.class, args);
	}

}
