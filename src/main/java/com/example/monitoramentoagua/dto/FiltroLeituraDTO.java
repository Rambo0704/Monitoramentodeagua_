package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FiltroLeituraDTO {
    // DTO para ser usado em parâmetros de busca
    // Ex: GET /api/leituras?dataInicio=...&contratoId=...
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer contratoId;
    private String numSerieHidrometro;
}