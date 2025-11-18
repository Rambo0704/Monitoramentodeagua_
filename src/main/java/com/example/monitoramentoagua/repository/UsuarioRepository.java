package com.example.monitoramentoagua.repository;

import com.example.monitoramentoagua.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // JpaRepository<Usuario, String> (pois o ID é 'codUsuario')

    /**
     * Método customizado para o Spring Security (UserDetailsService)
     * O Spring Data JPA cria a query automaticamente pelo nome do método.
     */
    Optional<Usuario> findByEmail(String email);
}