package com.example.monitoramentoagua.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Adiciona Getters, Setters, toString, etc.
@NoArgsConstructor // Adiciona um construtor vazio
public class LoginRequest {
    private String email;
    private String senha;
}