package com.example.currenciestesttask.currenciestesttask;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CurrenciesTestTaskApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(CurrenciesTestTaskApplication.class, args);
	}

}
