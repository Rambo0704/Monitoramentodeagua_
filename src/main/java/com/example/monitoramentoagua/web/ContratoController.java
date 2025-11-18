package com.example.monitoramentoagua.web;

import com.example.monitoramentoagua.dto.ContratoDTO;
import com.example.monitoramentoagua.dto.CriarContratoDTO;
import com.example.monitoramentoagua.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    @PostMapping
    public ResponseEntity<ContratoDTO> criarContrato(@RequestBody CriarContratoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contratoService.criarContrato(dto));
    }

    @GetMapping
    public ResponseEntity<List<ContratoDTO>> listarContratos() {
        return ResponseEntity.ok(contratoService.listarContratos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoDTO> atualizarContrato(@PathVariable String id, @RequestBody CriarContratoDTO dto) {
        return ResponseEntity.ok(contratoService.atualizarContrato(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarContrato(@PathVariable String id) {
        contratoService.deletarContrato(id);
        return ResponseEntity.noContent().build();
    }
}