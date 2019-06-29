package com.heraizen.dhi.tzen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@SpringBootApplication
@ComponentScan({"com.spaneos","com.heraizen"})
public class TzenApplication {
	
	

	public static void main(String[] args) {
		SpringApplication.run(TzenApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return (String... args) -> {

		};

	}
	@Autowired
	void setMapKeyDotReplacement(MappingMongoConverter mappingMongoConverter) {
	    mappingMongoConverter.setMapKeyDotReplacement("_");
	}

}
