package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "cod_usuario")
public class PessoaFisica extends Usuario {

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(name = "data_nasc")
    private java.time.LocalDate dataNasc;
}