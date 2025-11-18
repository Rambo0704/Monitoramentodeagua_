package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.domain.PessoaFisica;
import com.example.monitoramentoagua.domain.PessoaJuridica;
import com.example.monitoramentoagua.domain.Usuario;
import com.example.monitoramentoagua.dto.CriarUsuarioDTO;
import com.example.monitoramentoagua.dto.UsuarioDTO;
import com.example.monitoramentoagua.repository.PessoaFisicaRepository;
import com.example.monitoramentoagua.repository.PessoaJuridicaRepository;
import com.example.monitoramentoagua.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    private UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO.UsuarioDTOBuilder builder = UsuarioDTO.builder()
                .codUsuario(usuario.getCodUsuario())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataCadastro(usuario.getDataCadastro())
                .tipoP(usuario.getTipoP());

        if (usuario instanceof PessoaFisica pf) {
            builder.cpf(pf.getCpf()).dataNasc(pf.getDataNasc());
        } else if (usuario instanceof PessoaJuridica pj) {
            builder.cnpj(pj.getCnpj()).razaoSocial(pj.getRazaoSocial());
        }
        return builder.build();
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarUsuarioPorId(String id) {
        return usuarioRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    @Transactional
    public void deletarUsuario(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public UsuarioDTO criarUsuario(CriarUsuarioDTO dto) {
        String tipo = dto.getTipoP().toUpperCase();
        String id = idGenerator.gerarId("U");

        if ("FISICA".equals(tipo)) {
            PessoaFisica pf = new PessoaFisica();
            pf.setCodUsuario(id);
            pf.setNome(dto.getNome());
            pf.setEmail(dto.getEmail());
            pf.setSenha(passwordEncoder.encode(dto.getSenha()));
            pf.setTipoP("FISICA");
            pf.setDataCadastro(LocalDateTime.now());
            pf.setCpf(dto.getCpf());
            pf.setDataNasc(dto.getDataNasc());
            return toDTO(pessoaFisicaRepository.save(pf));
        } else if ("JURIDICA".equals(tipo)) {
            PessoaJuridica pj = new PessoaJuridica();
            pj.setCodUsuario(id);
            pj.setNome(dto.getNome());
            pj.setEmail(dto.getEmail());
            pj.setSenha(passwordEncoder.encode(dto.getSenha()));
            pj.setTipoP("JURIDICA");
            pj.setDataCadastro(LocalDateTime.now());
            pj.setCnpj(dto.getCnpj());
            pj.setRazaoSocial(dto.getRazaoSocial());
            return toDTO(pessoaJuridicaRepository.save(pj));
        } else {
            throw new IllegalArgumentException("Tipo de pessoa inválido: " + dto.getTipoP());
        }
    }

    @Transactional
    public UsuarioDTO atualizarUsuario(String id, CriarUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (usuario instanceof PessoaFisica pf) {
            pf.setCpf(dto.getCpf());
            pf.setDataNasc(dto.getDataNasc());
            return toDTO(pessoaFisicaRepository.save(pf));
        } else if (usuario instanceof PessoaJuridica pj) {
            pj.setCnpj(dto.getCnpj());
            pj.setRazaoSocial(dto.getRazaoSocial());
            return toDTO(pessoaJuridicaRepository.save(pj));
        }
        
        return toDTO(usuarioRepository.save(usuario));
    }
}