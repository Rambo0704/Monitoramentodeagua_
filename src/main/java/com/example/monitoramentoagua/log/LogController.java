package com.example.monitoramentoagua.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public List<LogEntry> listarTodos() {
        return logService.listarTodos();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<LogEntry> porUsuario(@PathVariable String usuarioId) {
        return logService.listarPorUsuario(usuarioId);
    }

    @PostMapping
    public LogEntry criarLog(@RequestBody Map<String, Object> body) {
        String usuarioId = (String) body.get("usuarioId");
        String acao = (String) body.get("acao");
        String status = (String) body.getOrDefault("status", "OK");
        Map<String, Object> detalhes = (Map<String, Object>) body.getOrDefault("detalhes", Map.of());
        return logService.registrarLog(usuarioId, acao, status, detalhes);
    }
}
