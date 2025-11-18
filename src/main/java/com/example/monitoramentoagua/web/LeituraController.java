package com.example.monitoramentoagua.web;

import com.example.monitoramentoagua.domain.Leitura;
import com.example.monitoramentoagua.dto.AtualizarLeituraDTO;
import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import com.example.monitoramentoagua.service.LeituraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 
import java.util.List;
import com.example.monitoramentoagua.domain.ApiLog;
import com.example.monitoramentoagua.repository.mongo.ApiLogRepository;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
public class LeituraController {

    private final LeituraService leituraService;
    
    private final ApiLogRepository logRepository;

 
    @PostMapping
    public ResponseEntity<Leitura> criarNovaLeitura(@RequestBody CriarLeituraDTO dto) {
        Leitura leituraCriada = leituraService.criarLeitura(dto);
     
        try {
            logRepository.save(ApiLog.builder()
                .timestamp(LocalDateTime.now())
                .acao("CRIAR_LEITURA")
                .detalhes("Leitura " + leituraCriada.getCodLeitura() + " criada com valor " + dto.getValorMedido())
                .build());
        } catch (Exception e) {

            System.err.println("Falha ao salvar log no MongoDB: " + e.getMessage());
        }
        
        return ResponseEntity.status(201).body(leituraCriada); 
    }

    @GetMapping
    public ResponseEntity<List<Leitura>> listarTodasLeituras() {
        List<Leitura> leituras = leituraService.listarLeituras();
        return ResponseEntity.ok(leituras);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLeitura(@PathVariable String id) {
        leituraService.deletarLeitura(id);
 
         try {
             logRepository.save(ApiLog.builder()
                .timestamp(LocalDateTime.now())
                .acao("DELETAR_LEITURA")
                .detalhes("Leitura " + id + " deletada.")
                .build());
        } catch (Exception e) {
            System.err.println("Falha ao salvar log no MongoDB: " + e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Leitura> atualizarLeitura(
            @PathVariable String id,
            @RequestBody AtualizarLeituraDTO dto) {
        
        Leitura leituraAtualizada = leituraService.atualizarLeitura(id, dto);
        
         try {
             logRepository.save(ApiLog.builder()
                .timestamp(LocalDateTime.now())
                .acao("ATUALIZAR_LEITURA")
                .detalhes("Leitura " + id + " atualizada para valor " + dto.getValorMedido())
                .build());
        } catch (Exception e) {
            System.err.println("Falha ao salvar log no MongoDB: " + e.getMessage());
        }


        return ResponseEntity.ok(leituraAtualizada);
    }
}