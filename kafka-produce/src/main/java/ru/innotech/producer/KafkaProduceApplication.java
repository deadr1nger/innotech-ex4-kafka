package ru.innotech.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaProduceApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProduceApplication.class, args);
	}

}
