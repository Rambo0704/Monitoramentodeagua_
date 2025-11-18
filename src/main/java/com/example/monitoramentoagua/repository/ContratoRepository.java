package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, String> {

}