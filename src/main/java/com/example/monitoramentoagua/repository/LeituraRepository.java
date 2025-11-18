package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Leitura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeituraRepository extends JpaRepository<Leitura, Integer> {
    // JpaRepository<Leitura, Integer> (pois o ID é 'codLeitura')
}