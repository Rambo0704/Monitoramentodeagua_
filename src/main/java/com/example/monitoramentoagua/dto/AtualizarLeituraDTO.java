package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AtualizarLeituraDTO {

    private BigDecimal valorMedido;
    private LocalDateTime dataHoraLeitura;
}