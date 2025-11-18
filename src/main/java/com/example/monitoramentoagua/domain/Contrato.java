package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contrato")
@Getter
@Setter
public class Contrato {

    @Id
    @Column(name = "cod_contrato", length = 20)
    private String codContrato; 

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_imovel", nullable = false)
    private Imovel imovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_serie_hidrometro", nullable = false)
    private Hidrometro hidrometro;
}