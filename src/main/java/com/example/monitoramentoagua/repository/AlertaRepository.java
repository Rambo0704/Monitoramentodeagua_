package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Integer> {
    // JpaRepository<TipoDaEntidade, TipoDoId>
}