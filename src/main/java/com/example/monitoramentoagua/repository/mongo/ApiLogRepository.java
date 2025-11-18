package com.example.monitoramentoagua.repository.mongo;

import com.example.monitoramentoagua.domain.ApiLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends MongoRepository<ApiLog, String> {
}