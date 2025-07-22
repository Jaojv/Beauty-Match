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

/**
 * ENTIDADE USUÁRIO - CLASSE BASE PARA TODOS OS USUÁRIOS DO SISTEMA
 * 
 * Esta é a entidade principal que representa todos os usuários do sistema Match Beauty.
 * Utiliza herança JOINED para criar tabelas separadas para cada tipo de usuário
 * (Cliente, Profissional, Proprietário, Admin), mas mantém dados comuns nesta tabela.
 * 
 * CARACTERÍSTICAS PRINCIPAIS:
 * - Implementa UserDetails do Spring Security para autenticação
 * - Usa herança JOINED para separar tipos de usuário
 * - Contém dados básicos como nome, email, senha, telefone
 * - Mantém timestamps de criação e atualização
 * - Define roles/permissões baseadas no tipo de usuário
 *
 */
@Data
@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {
    
    /**
     * ID ÚNICO DO USUÁRIO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    /**
     * NOME DE USUÁRIO ÚNICO
     * Usado para login no sistema
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * SENHA DO USUÁRIO
     * Será criptografada pelo Spring Security
     */
    @Column(nullable = false)
    private String password;

    /**
     * EMAIL ÚNICO DO USUÁRIO
     * Usado para comunicação e recuperação de conta
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * NOME COMPLETO DO USUÁRIO
     */
    @Column(nullable = false)
    private String nome;

    /**
     * TELEFONE DO USUÁRIO
     * Campo opcional para contato
     */
    private String telefone;

    /**
     * DATA E HORA DE CRIAÇÃO DO REGISTRO
     * Preenchido automaticamente no construtor
     */
    @Column(nullable = false)
    private LocalDateTime criadoEm;

    /**
     * DATA E HORA DA ÚLTIMA ATUALIZAÇÃO
     * Deve ser atualizado sempre que o registro for modificado
     */
    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    /**
     * TIPO DE USUÁRIO
     * Define o papel do usuário no sistema (ADMIN, CLIENTE, PROFISSIONAL, PROPRIETARIO)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    /**
     * CONSTRUTOR PADRÃO
     * Inicializa timestamps automaticamente
     */
    public Usuario() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    /**
     * CONSTRUTOR COM PARÂMETROS BÁSICOS
     * 
     * @param idUsuario ID do usuário
     * @param username Nome de usuário
     * @param password Senha
     * @param email Email
     * @param telefone Telefone
     * @param criadoEm Data de criação
     * @param atualizadoEm Data de atualização
     */
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

    /**
     * CONSTRUTOR COMPLETO COM NOME
     * 
     * @param idUsuario ID do usuário
     * @param username Nome de usuário
     * @param password Senha
     * @param email Email
     * @param nome Nome completo
     * @param telefone Telefone
     * @param criadoEm Data de criação
     * @param atualizadoEm Data de atualização
     */
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

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos da classe
    
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

    // ========== MÉTODOS DO SPRING SECURITY ==========
    // Implementação da interface UserDetails para autenticação

    /**
     * RETORNA AS AUTORIDADES/PERMISSÕES DO USUÁRIO
     * Converte o tipo de usuário em uma role do Spring Security
     * 
     * @return Lista de autoridades baseada no tipo de usuário
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.tipoUsuario.name()));
    }

    /**
     * VERIFICA SE A CONTA NÃO ESTÁ EXPIRADA
     * Por padrão, contas não expiram
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * VERIFICA SE A CONTA NÃO ESTÁ BLOQUEADA
     * Por padrão, contas não são bloqueadas
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * VERIFICA SE AS CREDENCIAIS NÃO ESTÃO EXPIRADAS
     * Por padrão, credenciais não expiram
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * VERIFICA SE A CONTA ESTÁ HABILITADA
     * Por padrão, contas estão habilitadas
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * ENUMERATION DOS TIPOS DE USUÁRIO
     * Define os diferentes papéis que um usuário pode ter no sistema
     */
    public enum TipoUsuario {
        ADMIN,          // Administrador do sistema
        CLIENTE,        // Cliente que agenda serviços
        PROFISSIONAL,   // Profissional que presta serviços
        PROPRIETARIO    // Proprietário de salão
    }
}