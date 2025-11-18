package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, String> {

}