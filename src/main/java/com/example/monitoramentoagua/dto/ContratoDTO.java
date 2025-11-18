package com.example.monitoramentoagua.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ContratoDTO {
    private String codContrato;
    private String status;
    private LocalDateTime dataInicio;
    private String codUsuario;
    private String nomeUsuario;
    private String codImovel;
    private String cepImovel;
    private String numSerieHidrometro;
}