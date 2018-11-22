package com.example.securyti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.securyti.entity.User;

@EnableJpaRepositories
@ComponentScan(basePackages = "com.example.securyti")
@EntityScan(basePackages = "com.example.securyti")
@SpringBootApplication
public class SecurytiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurytiApplication.class, args);
		//User usr = new User("abc","abc","abc");
		
	}
	
}
