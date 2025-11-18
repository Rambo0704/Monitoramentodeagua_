package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.domain.Contrato;
import com.example.monitoramentoagua.domain.Hidrometro;
import com.example.monitoramentoagua.domain.Imovel;
import com.example.monitoramentoagua.domain.Usuario;
import com.example.monitoramentoagua.dto.ContratoDTO;
import com.example.monitoramentoagua.dto.CriarContratoDTO;
import com.example.monitoramentoagua.repository.ContratoRepository;
import com.example.monitoramentoagua.repository.HidrometroRepository;
import com.example.monitoramentoagua.repository.ImovelRepository;
import com.example.monitoramentoagua.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImovelRepository imovelRepository;
    private final HidrometroRepository hidrometroRepository;
    private final IdGenerator idGenerator;

    private ContratoDTO toDTO(Contrato contrato) {
        return ContratoDTO.builder()
                .codContrato(contrato.getCodContrato())
                .status(contrato.getStatus())
                .dataInicio(contrato.getDataInicio())
                .codUsuario(contrato.getUsuario().getCodUsuario())
                .nomeUsuario(contrato.getUsuario().getNome())
                .codImovel(contrato.getImovel().getCodImovel())
                .cepImovel(contrato.getImovel().getEndereco() != null ? contrato.getImovel().getEndereco().getCep() : "N/D")
                .numSerieHidrometro(contrato.getHidrometro().getNumSerieHidrometro())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ContratoDTO> listarContratos() {
        return contratoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void deletarContrato(String id) {
        if (!contratoRepository.existsById(id)) {
            throw new EntityNotFoundException("Contrato não encontrado: " + id);
        }
        contratoRepository.deleteById(id);
    }

    @Transactional
    public ContratoDTO criarContrato(CriarContratoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getCodUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + dto.getCodUsuario()));
        Imovel imovel = imovelRepository.findById(dto.getCodImovel())
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado: " + dto.getCodImovel()));
        Hidrometro hidrometro = hidrometroRepository.findById(dto.getNumSerieHidrometro())
                .orElseThrow(() -> new EntityNotFoundException("Hidrômetro não encontrado: " + dto.getNumSerieHidrometro()));

        Contrato contrato = new Contrato();
        contrato.setCodContrato(idGenerator.gerarId("C"));
        contrato.setDataInicio(LocalDateTime.now());
        contrato.setStatus(dto.getStatus());
        contrato.setUsuario(usuario);
        contrato.setImovel(imovel);
        contrato.setHidrometro(hidrometro);
        
        return toDTO(contratoRepository.save(contrato));
    }

    @Transactional
    public ContratoDTO atualizarContrato(String id, CriarContratoDTO dto) {
        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contrato não encontrado: " + id));

        Usuario usuario = usuarioRepository.findById(dto.getCodUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + dto.getCodUsuario()));
        Imovel imovel = imovelRepository.findById(dto.getCodImovel())
                .orElseThrow(() -> new EntityNotFoundException("Imóvel não encontrado: " + dto.getCodImovel()));
        Hidrometro hidrometro = hidrometroRepository.findById(dto.getNumSerieHidrometro())
                .orElseThrow(() -> new EntityNotFoundException("Hidrômetro não encontrado: " + dto.getNumSerieHidrometro()));

        contrato.setStatus(dto.getStatus());
        contrato.setUsuario(usuario);
        contrato.setImovel(imovel);
        contrato.setHidrometro(hidrometro);

        return toDTO(contratoRepository.save(contrato));
    }
}