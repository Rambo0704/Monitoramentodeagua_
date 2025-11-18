package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Endereco")
@Getter
@Setter
public class Endereco {

    @Id
    @Column(length = 8)
    private String cep;

    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
}