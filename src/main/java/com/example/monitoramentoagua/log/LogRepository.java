package com.example.monitoramentoagua.log;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LogRepository extends MongoRepository<LogEntry, String> {
    List<LogEntry> findByUsuarioId(String usuarioId);
    List<LogEntry> findByAcao(String acao);
}
