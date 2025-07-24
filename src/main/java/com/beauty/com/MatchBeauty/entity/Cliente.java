package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

/**
 * ENTIDADE CLIENTE - REPRESENTA UM CLIENTE DO SISTEMA
 * 
 * Esta entidade representa um cliente no sistema Match Beauty.
 * Herda da classe Usuario e adiciona informações específicas de clientes
 * como CPF, data de nascimento, endereço e preferências.
 * 
 * HERANÇA:
 * - Estende a classe Usuario (herança JOINED)
 * - Herda todos os campos básicos: id, username, password, email, etc.
 * - Adiciona campos específicos de cliente
 * 
 * RELACIONAMENTOS:
 * - Um cliente pode ter vários agendamentos (OneToMany via Agendamento)
 * - Um cliente pode ter várias respostas de quiz (OneToMany via RespostaQuiz)
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - CPF para identificação única
 * - Data de nascimento para personalização de serviços
 * - Endereço para entrega de produtos (se aplicável)
 * - Preferências para recomendações personalizadas
 * 
 * @author João [Seu Nome]
 * @version 1.0
 * @since 2024
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {
    
    /**
     * CPF DO CLIENTE
     * Número único de identificação fiscal
     * Campo obrigatório para clientes brasileiros
     */
    @Column(name = "cpf")
    private String cpf;
    
    /**
     * DATA DE NASCIMENTO DO CLIENTE
     * Usada para personalização de serviços e promoções
     */
    @Column(name = "data_nascimento")
    private String dataNascimento;
    
    /**
     * ENDEREÇO DO CLIENTE
     * Endereço completo para entrega ou referência
     */
    @Column(name = "endereco")
    private String endereco;
    
    /**
     * PREFERÊNCIAS DO CLIENTE
     * Informações sobre preferências de estilo, cores, etc.
     * Usado para recomendações personalizadas
     */
    @Column(name = "preferencias")
    private String preferencias;
    
    /**
     * CONSTRUTOR PADRÃO
     * Chama o construtor da classe pai (Usuario)
     */
    public Cliente() {
        super();
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
    public Cliente(Long idUsuario, String username, String password, String email, String telefone, 
                  LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos específicos de Cliente

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

} 