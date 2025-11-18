package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Hidrometro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HidrometroRepository extends JpaRepository<Hidrometro, String> {
    // JpaRepository<Hidrometro, String> (pois o ID é 'numSerieHidrometro')
}