package com.example.monitoramentoagua.service;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class IdGenerator {

    private final Random random = new Random();
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String gerarId(String prefixo) {
        StringBuilder sb = new StringBuilder(prefixo + "_");
        for (int i = 0; i < 10; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}