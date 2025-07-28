package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Classe que implementa UserDetails do Spring Security
// Representa os detalhes do usuário autenticado com suas permissões
public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String nome;
    private String email;
    private String tipoUsuario;
    private Collection<? extends GrantedAuthority> authorities;

    // Construtor para criar UserPrincipal com dados do usuário
    // Configura as autoridades baseadas no tipo de usuário
    public UserPrincipal(Long id, String username, String password, String nome, String email, String tipoUsuario) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
        this.tipoUsuario = tipoUsuario;
        // Converte ADMINISTRADOR para ADMIN para compatibilidade com roles
        String role = tipoUsuario.equals("ADMINISTRADOR") ? "ADMIN" : tipoUsuario;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    // Método estático para criar UserPrincipal a partir de um Usuario
    // Facilita a conversão de entidade para UserDetails
    public static UserPrincipal create(Usuario usuario) {
        return new UserPrincipal(
            usuario.getIdUsuario(),
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getTipoUsuario().name()
        );
    }

    // Getters para acessar os dados do usuário
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    // Retorna as autoridades/permissões do usuário
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Retorna a senha do usuário
    @Override
    public String getPassword() {
        return password;
    }

    // Retorna o username do usuário
    @Override
    public String getUsername() {
        return username;
    }

    // Verifica se a conta não expirou (sempre true neste sistema)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Verifica se a conta não está bloqueada (sempre true neste sistema)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Verifica se as credenciais não expiraram (sempre true neste sistema)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Verifica se a conta está habilitada (sempre true neste sistema)
    @Override
    public boolean isEnabled() {
        return true;
    }
} 