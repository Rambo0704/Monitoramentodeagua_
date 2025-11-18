package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CriarLeituraDTO {
    // Este é o JSON que o Postman vai enviar
    private BigDecimal valorMedido;
    private LocalDateTime dataHoraLeitura;
    private String numSerieHidrometro; // O ID do hidrômetro
}