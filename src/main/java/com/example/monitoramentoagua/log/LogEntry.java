package com.example.monitoramentoagua.log;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "logs")
public class LogEntry {

    @Id
    private String id;

    private LocalDateTime timestamp;
    private String usuarioId;
    private String acao;
    private String status;
    private Map<String, Object> detalhes;

    public LogEntry() {}

    public LogEntry(LocalDateTime timestamp, String usuarioId, String acao, String status, Map<String, Object> detalhes) {
        this.timestamp = timestamp;
        this.usuarioId = usuarioId;
        this.acao = acao;
        this.status = status;
        this.detalhes = detalhes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Object> getDetalhes() { return detalhes; }
    public void setDetalhes(Map<String, Object> detalhes) { this.detalhes = detalhes; }
}
