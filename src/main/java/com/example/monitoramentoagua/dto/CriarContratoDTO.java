package com.example.monitoramentoagua.dto;

import lombok.Data;

@Data
public class CriarContratoDTO {
    private String codUsuario;
    private String codImovel;
    private String numSerieHidrometro;
    private String status;
}