package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; // Importe o Logger
import org.slf4j.LoggerFactory; // Importe o Logger
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SimulacaoService {

    // Pega o logger para vermos a simulação no console
    private static final Logger log = LoggerFactory.getLogger(SimulacaoService.class);

    // Injeta o LeituraService que JÁ TEM a lógica de criar alertas
    private final LeituraService leituraService;

    // Objeto para gerar números aleatórios
    private final Random random = new Random();

    /**
     * Este método vai rodar automaticamente a cada 10 segundos.
     * (10000 milissegundos = 10 segundos)
     * * Mude o 'fixedRate' para o tempo que você quiser:
     * 5000 = 5 segundos
     * 60000 = 1 minuto
     */
    @Scheduled(fixedRate = 10000)
    public void simularLeituraAutomatica() {
        
        // --- 1. Gera um Valor Aleatório ---
        // Gera um valor entre 0.0 e 119.9
        double valorAleatorio = random.nextDouble() * 120.0;
        BigDecimal valorMedido = BigDecimal.valueOf(valorAleatorio).setScale(3, BigDecimal.ROUND_HALF_UP);

        // --- 2. Monta o DTO (o "pacote" de dados) ---
        // Vamos simular dados para o hidrômetro que já testamos
        CriarLeituraDTO dto = new CriarLeituraDTO();
        dto.setNumSerieHidrometro("HIDRO-001"); // O hidrômetro que sabemos que existe
        dto.setValorMedido(valorMedido);
        dto.setDataHoraLeitura(LocalDateTime.now());

        // --- 3. Envia para o Serviço ---
        // Aqui, chamamos o serviço de leitura.
        // Se o 'valorMedido' for > 50.0, o LeituraService VAI criar o alerta!
        try {
            leituraService.criarLeitura(dto);
            log.info(">>> SIMULAÇÃO: Leitura de {} m³ registrada para HIDRO-001", valorMedido);

            if (valorMedido.doubleValue() > 50.0) {
                 log.warn(">>> SIMULAÇÃO: Alerta de consumo alto gerado!");
            }

        } catch (Exception e) {
            log.error(">>> SIMULAÇÃO: Falha ao registrar leitura - {}", e.getMessage());
        }
    }
}