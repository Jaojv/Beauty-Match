package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Data
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    public Usuario() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    public Usuario(Long idUsuario, String username, String password, String email,
                   String telefone, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telefone = telefone;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Usuario(Long idUsuario, String username, String password, String email,
                   String nome, String telefone, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Getters e setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum TipoUsuario {
        ADMIN,
        CLIENTE,
        PROFISSIONAL,
        PROPRIETARIO
    }
}