package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, String> {

    Optional<Alerta> findByLeitura_CodLeitura(String codLeitura);
}