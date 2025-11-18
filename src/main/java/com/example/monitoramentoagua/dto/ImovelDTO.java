package com.example.monitoramentoagua.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImovelDTO {
    private String codImovel;
    private String tipoImovel;
    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
}