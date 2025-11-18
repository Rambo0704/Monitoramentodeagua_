package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pessoa_juridica")
@PrimaryKeyJoinColumn(name = "cod_usuario")
public class PessoaJuridica extends Usuario {

    @Column(unique = true, nullable = false, length = 14)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;
}