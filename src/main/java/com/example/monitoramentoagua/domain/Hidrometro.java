package com.example.monitoramentoagua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore; 
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "hidrometro")
public class Hidrometro {

    @Id
    @Column(name = "num_serie_hidrometro", length = 50)
    private String numSerieHidrometro;

    private String marca;
    private String modelo;

    @JsonIgnore 
    @OneToMany(mappedBy = "hidrometro")
    private List<Leitura> leituras;
}