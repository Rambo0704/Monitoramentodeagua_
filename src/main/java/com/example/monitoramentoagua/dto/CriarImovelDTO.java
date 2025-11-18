package com.example.monitoramentoagua.dto;

import lombok.Data;

@Data
public class CriarImovelDTO {
    private String tipoImovel;
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
}