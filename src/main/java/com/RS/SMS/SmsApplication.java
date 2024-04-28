package com.RS.SMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.RS.SMS.Repository")
@EntityScan("com.RS.SMS.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")

public class SmsApplication {
public static void main(String[] args) {
		SpringApplication.run(SmsApplication.class, args);

	}
}
