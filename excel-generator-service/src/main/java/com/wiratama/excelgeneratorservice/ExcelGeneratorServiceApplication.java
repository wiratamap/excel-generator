package com.wiratama.excelgeneratorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.wiratama.excelgeneratorservice.properties")
public class ExcelGeneratorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExcelGeneratorServiceApplication.class, args);
	}

}
