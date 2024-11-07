package com.eastnets.supervisorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SupervisorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupervisorServiceApplication.class, args);
	}

}
