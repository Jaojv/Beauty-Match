package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ENTIDADE PROPRIETÁRIO - REPRESENTA UM PROPRIETÁRIO DE SALÃO
 * 
 * Esta entidade representa um proprietário de salão no sistema Match Beauty.
 * Herda da classe Usuario e adiciona informações específicas de proprietários
 * como CNPJ, razão social, endereço e horário de funcionamento.
 * 
 * HERANÇA:
 * - Estende a classe Usuario (herança JOINED)
 * - Herda todos os campos básicos: id, username, password, email, etc.
 * - Adiciona campos específicos de proprietário
 * 
 * RELACIONAMENTOS:
 * - Um proprietário pode ter vários salões (OneToMany via Salao)
 * - Um proprietário gerencia profissionais e serviços
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - CNPJ para identificação empresarial
 * - Razão social para documentos oficiais
 * - Endereço do estabelecimento
 * - Horário de funcionamento geral
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "proprietario")
public class Proprietario extends Usuario {
    
    /**
     * CNPJ DO PROPRIETÁRIO
     * Número único de identificação empresarial
     * Campo obrigatório para proprietários de empresas
     */
    @Column(name = "cnpj")
    private String cnpj;
    
    /**
     * RAZÃO SOCIAL DO PROPRIETÁRIO
     * Nome oficial da empresa para documentos
     */
    @Column(name = "razao_social")
    private String razaoSocial;
    
    /**
     * ENDEREÇO DO PROPRIETÁRIO
     * Endereço do estabelecimento principal
     */
    @Column(name = "endereco")
    private String endereco;
    
    /**
     * HORÁRIO DE FUNCIONAMENTO
     * Horário geral de funcionamento do estabelecimento
     */
    @Column(name = "horario_funcionamento")
    private String horarioFuncionamento;
    
    /**
     * CONSTRUTOR PADRÃO
     * Chama o construtor da classe pai (Usuario)
     */
    public Proprietario() {
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
    public Proprietario(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos específicos de Proprietario

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    // ========== MÉTODOS ESPECÍFICOS ==========
    // Métodos específicos do Proprietario

    /**
     * ATUALIZA DADOS BÁSICOS DO PROPRIETÁRIO
     * Atualiza nome, email, telefone e timestamp de atualização
     * 
     * @param nome Novo nome do proprietário
     * @param email Novo email do proprietário
     * @param telefone Novo telefone do proprietário
     */
    public void atualizarDados(String nome, String email, String telefone) {
        this.setNome(nome);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setAtualizadoEm(LocalDateTime.now());
    }
} 