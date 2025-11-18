package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, String> {
    // JpaRepository<Endereco, String> (pois o ID é o 'cep')
}