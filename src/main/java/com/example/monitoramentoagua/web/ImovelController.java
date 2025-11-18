package com.example.monitoramentoagua.web;

import com.example.monitoramentoagua.dto.CriarImovelDTO;
import com.example.monitoramentoagua.dto.ImovelDTO;
import com.example.monitoramentoagua.service.ImovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/imoveis")
@RequiredArgsConstructor
public class ImovelController {

    private final ImovelService imovelService;

    @PostMapping
    public ResponseEntity<ImovelDTO> criarImovel(@RequestBody CriarImovelDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imovelService.criarImovel(dto));
    }

    @GetMapping
    public ResponseEntity<List<ImovelDTO>> listarImoveis() {
        return ResponseEntity.ok(imovelService.listarImoveis());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelDTO> atualizarImovel(@PathVariable String id, @RequestBody CriarImovelDTO dto) {
        return ResponseEntity.ok(imovelService.atualizarImovel(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable String id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}