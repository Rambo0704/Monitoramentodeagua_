package com.example.monitoramentoagua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableScheduling
@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.example.monitoramentoagua.repository")

@EnableMongoRepositories(basePackages = "com.example.monitoramentoagua.repository.mongo")
public class MonitoramentoAguaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoramentoAguaApplication.class, args);
	}

}