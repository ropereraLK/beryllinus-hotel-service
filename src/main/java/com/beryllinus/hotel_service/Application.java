package com.beryllinus.hotel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		System.out.println("ENV: " + System.getenv("SPRING_DATA_MONGODB_URI"));
		System.out.println("PROP: " + System.getProperty("SPRING_DATA_MONGODB_URI"));
		SpringApplication.run(Application.class, args);


	}

}
