package com.beryllinus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableAsync
@SpringBootApplication
@EnableCaching
@EnableScheduling
@ConfigurationPropertiesScan
public class Application {

	public static void main(String[] args) {

//		System.out.println("ENV: " + System.getenv("SPRING_DATA_MONGODB_URI"));
//		System.out.println("PROP: " + System.getProperty("SPRING_DATA_MONGODB_URI"));
		SpringApplication.run(Application.class, args);


	}

}
