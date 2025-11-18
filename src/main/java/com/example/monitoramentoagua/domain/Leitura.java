package com.example.monitoramentoagua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "leitura")
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_leitura")
    private Integer codLeitura;

    @Column(name = "valor_medido", nullable = false, precision = 10, scale = 3)
    private BigDecimal valorMedido;

    @Column(name = "data_hora_leitura", nullable = false)
    private LocalDateTime dataHoraLeitura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "num_serie_hidrometro", nullable = false)
    @JsonIgnore // Evita loops infinitos ao serializar para JSON
    private Hidrometro hidrometro;
}