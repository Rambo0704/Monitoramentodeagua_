package com.example.monitoramentoagua.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public LogEntry registrarLog(String usuarioId, String acao, String status, Map<String, Object> detalhes) {
        LogEntry log = new LogEntry(LocalDateTime.now(), usuarioId, acao, status, detalhes);
        return logRepository.save(log);
    }

    public List<LogEntry> listarTodos() {
        return logRepository.findAll();
    }

    public List<LogEntry> listarPorUsuario(String usuarioId) {
        return logRepository.findByUsuarioId(usuarioId);
    }

    public List<LogEntry> listarPorAcao(String acao) {
        return logRepository.findByAcao(acao);
    }
}
