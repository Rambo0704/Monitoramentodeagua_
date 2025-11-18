package com.example.monitoramentoagua.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Cria um construtor com todos os argumentos
public class LoginResponse {
    private String token;
    private String nomeUsuario;
}