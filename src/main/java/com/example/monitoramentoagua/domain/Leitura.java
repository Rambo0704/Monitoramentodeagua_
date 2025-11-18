package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leitura")
public class Leitura {

    @Id
    @Column(name = "cod_leitura", length = 20) 
    private String codLeitura;

    @Column(name = "valor_medido", nullable = false, precision = 10, scale = 3)
    private BigDecimal valorMedido;

    @Column(name = "data_hora_leitura", nullable = false)
    private LocalDateTime dataHoraLeitura;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "num_serie_hidrometro", nullable = false)
    private Hidrometro hidrometro;
}