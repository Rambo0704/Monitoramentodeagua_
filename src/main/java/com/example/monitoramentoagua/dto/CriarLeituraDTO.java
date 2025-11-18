package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CriarLeituraDTO {

    private BigDecimal valorMedido;
    private LocalDateTime dataHoraLeitura;
    private String numSerieHidrometro; 
}