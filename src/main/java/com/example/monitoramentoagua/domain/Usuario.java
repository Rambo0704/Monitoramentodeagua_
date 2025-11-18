package com.example.monitoramentoagua.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet; // <-- IMPORT NECESSÁRIO
import java.util.List;
import java.util.Set; // <-- IMPORT NECESSÁRIO

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED) // Estratégia de Herança
public class Usuario implements UserDetails {

    @Id
    @Column(name = "cod_usuario", length = 20)
    private String codUsuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String senha;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "tipo_p")
    private String tipoP;

    // --- INÍCIO DA CORREÇÃO ---
    // Este é o campo "grupos" que o Grupo.java (com mappedBy) estava procurando.
    @ManyToMany
    @JoinTable(
      name = "grupos_usuarios", // Nome da sua tabela de junção
      joinColumns = @JoinColumn(name = "cod_usuario"), // Chave desta classe
      inverseJoinColumns = @JoinColumn(name = "cod_grupo") // Chave da outra classe
    )
    private Set<Grupo> grupos = new HashSet<>();
    // --- FIM DA CORREÇÃO ---
    

    // Métodos do UserDetails (Spring Security)
    // Para simplificar, estamos dizendo que todo usuário está ativo e é 'USER'
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER"); 
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}