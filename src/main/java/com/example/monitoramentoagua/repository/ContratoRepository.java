package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    // JpaRepository<Contrato, Integer> (assumindo que cod_contrato é Integer)
}