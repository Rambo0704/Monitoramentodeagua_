package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.PessoaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, String> {

}