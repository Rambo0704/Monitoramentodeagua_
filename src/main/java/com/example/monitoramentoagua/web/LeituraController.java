package com.example.monitoramentoagua.web;

import com.example.monitoramentoagua.domain.Leitura;
import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import com.example.monitoramentoagua.service.LeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leituras") // Rota protegida pelo Spring Security
@RequiredArgsConstructor
public class LeituraController {

    private final LeituraService leituraService;

    // Endpoint: POST /api/leituras
    @PostMapping
    public ResponseEntity<Leitura> criarNovaLeitura(@RequestBody CriarLeituraDTO dto) {
        Leitura leituraCriada = leituraService.criarLeitura(dto);
        // Retorna 201 Created
        return ResponseEntity.status(201).body(leituraCriada); 
    }
}