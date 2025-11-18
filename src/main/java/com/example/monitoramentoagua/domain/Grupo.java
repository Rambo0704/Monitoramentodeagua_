package com.example.monitoramentoagua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_grupo")
    private Integer codGrupo;

    @Column(name = "nome_grupo", nullable = false, unique = true)
    private String nomeGrupo;

    private String descricao;

    // A anotação "mappedBy" DEVE apontar para o nome do campo
    // que criamos na classe Usuario.java ("grupos")
    @ManyToMany(mappedBy = "grupos")
    @JsonIgnore // Importante para não causar loops de JSON
    private Set<Usuario> usuarios = new HashSet<>();
}