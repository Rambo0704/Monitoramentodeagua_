package com.example.monitoramentoagua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- 1. IMPORTE

@EnableScheduling // <-- 2. ADICIONE ESTA LINHA
@SpringBootApplication
public class MonitoramentoAguaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoramentoAguaApplication.class, args);
	}

}