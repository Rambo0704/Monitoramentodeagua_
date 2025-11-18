package com.example.monitoramentoagua.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "logs")
public class ApiLog {
    
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String acao;
    private String detalhes;
}