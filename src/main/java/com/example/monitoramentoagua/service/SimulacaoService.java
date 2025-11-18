package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SimulacaoService {

    private static final Logger log = LoggerFactory.getLogger(SimulacaoService.class);

    private final LeituraService leituraService;

    private final Random random = new Random();

    @Scheduled(fixedRate = 10000)
    public void simularLeituraAutomatica() {
 
        double valorAleatorio = random.nextDouble() * 120.0;
        BigDecimal valorMedido = BigDecimal.valueOf(valorAleatorio).setScale(3, BigDecimal.ROUND_HALF_UP);

        CriarLeituraDTO dto = new CriarLeituraDTO();
        dto.setNumSerieHidrometro("HIDRO-002");
        dto.setValorMedido(valorMedido);
        dto.setDataHoraLeitura(LocalDateTime.now());

        try {
            leituraService.criarLeitura(dto);
            log.info(">>> SIMULAÇÃO: Leitura de {} m³ registrada para HIDRO-002", valorMedido);

            if (valorMedido.doubleValue() > 50.0) {
                 log.warn(">>> SIMULAÇÃO: Alerta de consumo alto gerado!");
            }

        } catch (Exception e) {
            log.error(">>> SIMULAÇÃO: Falha ao registrar leitura - {}", e.getMessage());
        }
    }
}