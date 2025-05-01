package com.wiratama.filewatcherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("com.wiratama.filewatcherservice.properties")
@EnableScheduling
public class FileWatcherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileWatcherServiceApplication.class, args);
	}

}
