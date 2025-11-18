package com.example.monitoramentoagua.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioDTO {
    private String codUsuario;
    private String nome;
    private String email;
    private LocalDateTime dataCadastro;
    private String tipoP;
    private String cpf;
    private String cnpj;
    private String razaoSocial;
    private java.time.LocalDate dataNasc;
}