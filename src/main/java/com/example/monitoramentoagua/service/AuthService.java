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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        var usuario = (Usuario) usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow();

       
        String token = jwtUtil.generateToken(usuario);

        return new LoginResponse(token, usuario.getNome());
    }
}