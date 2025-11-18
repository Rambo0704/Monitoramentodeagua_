package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "Imovel")
@Getter
@Setter
public class Imovel {

    @Id
    @Column(name = "cod_imovel", length = 20)
    private String codImovel;

    @Column(name = "tipo_imovel", length = 100)
    private String tipoImovel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cep_endereco")
    private Endereco endereco;

    @OneToMany(mappedBy = "imovel")
    private Set<Contrato> contratos;
}