package com.nexym.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.nexym.clinic")
public class ClinicManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicManagementServiceApplication.class, args);
	}

}
