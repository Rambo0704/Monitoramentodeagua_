package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AtualizarLeituraDTO {
    // Apenas os campos que podem ser atualizados
    private BigDecimal valorMedido;
    private LocalDateTime dataHoraLeitura;
}