package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.beauty.com.MatchBeauty.entity.Salao;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * ENTIDADE PROFISSIONAL - REPRESENTA UM PROFISSIONAL DO SISTEMA
 * 
 * Esta entidade representa um profissional de beleza no sistema Match Beauty.
 * Herda da classe Usuario e adiciona informações específicas de profissionais
 * como CPF, especialidade, biografia e salão de trabalho.
 * 
 * HERANÇA:
 * - Estende a classe Usuario (herança JOINED)
 * - Herda todos os campos básicos: id, username, password, email, etc.
 * - Adiciona campos específicos de profissional
 * 
 * RELACIONAMENTOS:
 * - Um profissional trabalha em um salão (ManyToOne com Salao)
 * - Um profissional pode ter vários horários de trabalho (OneToMany via HorarioTrabalho)
 * - Um profissional pode ter vários agendamentos (OneToMany via Agendamento)
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - CPF para identificação única
 * - Especialidade para categorização de serviços
 * - Biografia para apresentação aos clientes
 * - Vínculo com salão de trabalho
 *
 */
@Entity
@Table(name = "profissional")
@DiscriminatorValue("PROFISSIONAL")
public class Profissional extends Usuario {
    
    /**
     * CPF DO PROFISSIONAL
     * Número único de identificação fiscal
     * Campo obrigatório para profissionais brasileiros
     */
    @Column(name = "cpf")
    private String cpf;
    
    /**
     * ESPECIALIDADE DO PROFISSIONAL
     * Área de atuação ou especialização
     * Ex: "Cabeleireiro", "Manicure", "Esteticista", "Maquiador"
     */
    @Column(name = "especialidade")
    private String especialidade;
    
    /**
     * BIOGRAFIA DO PROFISSIONAL
     * Texto de apresentação com experiência, formação, etc.
     * Exibida para clientes na escolha do profissional
     */
    @Column(name = "biografia")
    private String biografia;
    
    /**
     * SALÃO ONDE O PROFISSIONAL TRABALHA
     * Relacionamento ManyToOne com a entidade Salao
     * Carregamento LAZY para otimizar performance
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salao_id")
    private Salao salao;
    
    /**
     * CONSTRUTOR PADRÃO
     * Chama o construtor da classe pai (Usuario)
     */
    public Profissional() {
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
    public Profissional(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    // ========== GETTERS E SETTERS ==========
    // Métodos para acessar e modificar os atributos específicos de Profissional

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }

} 