package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.domain.Hidrometro;
import com.example.monitoramentoagua.domain.Leitura;
import com.example.monitoramentoagua.dto.AtualizarLeituraDTO;
import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import com.example.monitoramentoagua.repository.HidrometroRepository;
import com.example.monitoramentoagua.repository.LeituraRepository;
import com.example.monitoramentoagua.repository.AlertaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeituraService {

    private final LeituraRepository leituraRepository;
    private final HidrometroRepository hidrometroRepository;
    private final AlertaRepository alertaRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public Leitura criarLeitura(CriarLeituraDTO dto) {
        Hidrometro hidrometro = hidrometroRepository.findById(dto.getNumSerieHidrometro())
                .orElseThrow(() -> new EntityNotFoundException("Hidrômetro não encontrado: " + dto.getNumSerieHidrometro()));

        Leitura novaLeitura = new Leitura();
        novaLeitura.setCodLeitura(idGenerator.gerarId("L"));
        novaLeitura.setHidrometro(hidrometro);
        novaLeitura.setValorMedido(dto.getValorMedido());
        novaLeitura.setDataHoraLeitura(LocalDateTime.now());

        Leitura leituraSalva = leituraRepository.save(novaLeitura);
    
        
        return leituraSalva;
    }

    @Transactional
    public Leitura atualizarLeitura(String id, AtualizarLeituraDTO dto) {
        Leitura leitura = leituraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura não encontrada: " + id));

        leitura.setValorMedido(dto.getValorMedido());
        return leituraRepository.save(leitura);
    }

    @Transactional(readOnly = true)
    public Leitura buscarLeitura(String id) {
        return leituraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<Leitura> listarLeituras() {
        return leituraRepository.findAll();
    }

    @Transactional
    public void deletarLeitura(String id) {
      
        if (!leituraRepository.existsById(id)) {
            throw new EntityNotFoundException("Leitura não encontrada: " + id);
        }

        alertaRepository.findByLeitura_CodLeitura(id).ifPresent(alerta -> {
            alertaRepository.delete(alerta);
        });

        leituraRepository.deleteById(id);
      
    }
}