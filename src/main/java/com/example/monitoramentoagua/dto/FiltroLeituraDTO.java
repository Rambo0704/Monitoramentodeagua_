package com.example.monitoramentoagua.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FiltroLeituraDTO {

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer contratoId;
    private String numSerieHidrometro;
}