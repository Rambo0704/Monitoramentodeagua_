package com.example.monitoramentoagua.service;

import com.example.monitoramentoagua.config.security.JwtUtil;
import com.example.monitoramentoagua.domain.Usuario;
import com.example.monitoramentoagua.dto.LoginRequest;
import com.example.monitoramentoagua.dto.LoginResponse;
import com.example.monitoramentoagua.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        // 1. Autentica o usuário (Spring Security)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        // 2. Se a autenticação funcionou, busca o usuário
        var usuario = (Usuario) usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Não deve falhar se a auth passou

        // 3. Gera o token JWT
        String token = jwtUtil.generateToken(usuario);

        // 4. Retorna a resposta
        return new LoginResponse(token, usuario.getNome());
    }
}