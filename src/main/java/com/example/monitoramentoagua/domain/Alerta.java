package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alerta")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_alerta")
    private Integer codAlerta;

    @Column(name = "data_hora_alerta", nullable = false)
    private LocalDateTime dataHoraAlerta;

    @Column(name = "tipo_alerta", nullable = false, length = 100)
    private String tipoAlerta;

    // Um alerta é gerado por UMA leitura específica
    @OneToOne
    @JoinColumn(name = "cod_leitura", nullable = false, referencedColumnName = "cod_leitura")
    private Leitura leitura;
}