package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.domain.Hidrometro;
import com.example.monitoramentoagua.domain.Leitura;
import com.example.monitoramentoagua.dto.AtualizarLeituraDTO;
import com.example.monitoramentoagua.dto.CriarLeituraDTO;
import com.example.monitoramentoagua.repository.HidrometroRepository;
import com.example.monitoramentoagua.repository.LeituraRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeituraService {

    @Autowired
    private LeituraRepository leituraRepository;

    @Autowired
    private HidrometroRepository hidrometroRepository;

    @Transactional
    public Leitura criarLeitura(CriarLeituraDTO dto) {
        // Valida se o hidrômetro existe
        Hidrometro hidrometro = hidrometroRepository.findById(dto.getNumSerieHidrometro())
                .orElseThrow(() -> new EntityNotFoundException("Hidrômetro não encontrado: " + dto.getNumSerieHidrometro()));

        Leitura novaLeitura = new Leitura();
        novaLeitura.setHidrometro(hidrometro);
        novaLeitura.setValorMedido(dto.getValorMedido());
        
        // A sua trigger trg_update_leitura só roda no UPDATE.
        // No INSERT, precisamos setar a data manualmente, como o schema 'not null' exige.
        novaLeitura.setDataHoraLeitura(LocalDateTime.now());

        Leitura leituraSalva = leituraRepository.save(novaLeitura);
        
        // Os triggers 'trg_alerta_consumo_alto' e 'trg_alerta_valor_invalido'
        // vão rodar no banco DEPOIS deste 'save' (AFTER INSERT).
        
        return leituraSalva;
    }

    @Transactional
    public Leitura atualizarLeitura(Integer id, AtualizarLeituraDTO dto) {
        Leitura leitura = leituraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura não encontrada: " + id));

        leitura.setValorMedido(dto.getValorMedido());
        
        // Ao salvar, o trigger 'trg_update_leitura' (BEFORE UPDATE)
        // vai atualizar a data_hora_leitura automaticamente.
        return leituraRepository.save(leitura);
    }

    @Transactional(readOnly = true)
    public Leitura buscarLeitura(Integer id) {
        return leituraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Leitura não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<Leitura> listarLeituras() {
        return leituraRepository.findAll();
    }

    @Transactional
    public void deletarLeitura(Integer id) {
        if (!leituraRepository.existsById(id)) {
            throw new EntityNotFoundException("Leitura não encontrada: " + id);
        }
        leituraRepository.deleteById(id);
    }
}