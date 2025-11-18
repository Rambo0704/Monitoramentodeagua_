package com.example.monitoramentoagua.dto;

import lombok.Data;

@Data
public class CriarUsuarioDTO {
    private String nome;
    private String email;
    private String senha;
    private String tipoP; 
    private String cpf;
    private java.time.LocalDate dataNasc;
    private String cnpj;
    private String razaoSocial;
}