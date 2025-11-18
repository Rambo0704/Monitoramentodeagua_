package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, String> {
    // JpaRepository<PessoaJuridica, String> (pois o ID é 'codUsuario', que é String)
}