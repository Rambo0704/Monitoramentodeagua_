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

    @ManyToMany(mappedBy = "grupos")
    @JsonIgnore 
    private Set<Usuario> usuarios = new HashSet<>();
}