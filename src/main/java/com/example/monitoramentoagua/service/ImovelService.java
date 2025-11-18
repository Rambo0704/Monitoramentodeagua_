package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.domain.Endereco;
import com.example.monitoramentoagua.domain.Imovel;
import com.example.monitoramentoagua.dto.CriarImovelDTO;
import com.example.monitoramentoagua.dto.ImovelDTO;
import com.example.monitoramentoagua.repository.EnderecoRepository;
import com.example.monitoramentoagua.repository.ImovelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final EnderecoRepository enderecoRepository;
    private final IdGenerator idGenerator;

    private ImovelDTO toDTO(Imovel imovel) {
        Endereco e = imovel.getEndereco();
        return ImovelDTO.builder()
                .codImovel(imovel.getCodImovel())
                .tipoImovel(imovel.getTipoImovel())
                .cep(e != null ? e.getCep() : null)
                .rua(e != null ? e.getRua() : null)
                .bairro(e != null ? e.getBairro() : null)
                .cidade(e != null ? e.getCidade() : null)
                .estado(e != null ? e.getEstado() : null)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ImovelDTO> listarImoveis() {
        return imovelRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ImovelDTO buscarImovelPorId(String id) {
        return imovelRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado: " + id));
    }

    @Transactional
    public void deletarImovel(String id) {
        if (!imovelRepository.existsById(id)) {
            throw new EntityNotFoundException("Imóvel não encontrado: " + id);
        }
        imovelRepository.deleteById(id);
    }

    private Endereco findOrCreateEndereco(CriarImovelDTO dto) {
        return enderecoRepository.findById(dto.getCep())
                .orElseGet(() -> {
                    Endereco novoEndereco = new Endereco();
                    novoEndereco.setCep(dto.getCep());
                    novoEndereco.setRua(dto.getRua());
                    novoEndereco.setBairro(dto.getBairro());
                    novoEndereco.setCidade(dto.getCidade());
                    novoEndereco.setEstado(dto.getEstado());
                    return enderecoRepository.save(novoEndereco);
                });
    }

    @Transactional
    public ImovelDTO criarImovel(CriarImovelDTO dto) {
        Endereco endereco = findOrCreateEndereco(dto);

        Imovel imovel = new Imovel();
        imovel.setCodImovel(idGenerator.gerarId("I"));
        imovel.setTipoImovel(dto.getTipoImovel());
        imovel.setEndereco(endereco);

        return toDTO(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelDTO atualizarImovel(String id, CriarImovelDTO dto) {
        Imovel imovel = imovelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado: " + id));

        Endereco endereco = findOrCreateEndereco(dto);
        
        imovel.setTipoImovel(dto.getTipoImovel());
        imovel.setEndereco(endereco);

        return toDTO(imovelRepository.save(imovel));
    }
}