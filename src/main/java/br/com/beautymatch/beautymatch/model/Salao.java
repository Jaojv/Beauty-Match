package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "saloes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_salao;
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "O CNPJ é obrigatório")
    @CNPJ(message = "CNPJ inválido")
    @Column(nullable = false, unique = true)
    private String cnpj;
    
    @NotBlank(message = "O telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;
    
    @NotBlank(message = "O logradouro é obrigatório")
    @Column(nullable = false)
    private String logradouro;
    
    @NotBlank(message = "O número é obrigatório")
    @Column(nullable = false)
    private String numero;
    
    private String complemento;
    
    @NotBlank(message = "O bairro é obrigatório")
    @Column(nullable = false)
    private String bairro;
    
    @NotBlank(message = "A cidade é obrigatória")
    @Column(nullable = false)
    private String cidade;
    
    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres")
    @Column(nullable = false)
    private String estado;
    
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido")
    @Column(nullable = false)
    private String cep;
    
    @NotBlank(message = "O horário de abertura é obrigatório")
    @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "Horário de abertura inválido")
    @Column(nullable = false)
    private String horarioAbertura;
    
    @NotBlank(message = "O horário de fechamento é obrigatório")
    @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "Horário de fechamento inválido")
    @Column(nullable = false)
    private String horarioFechamento;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dono_id", nullable = false)
    private Dono dono;
    
    @OneToMany(mappedBy = "salao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Profissional> profissionais = new ArrayList<>();
    
    @OneToMany(mappedBy = "salao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servico> servicos = new ArrayList<>();
    
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
    
    public Long getId() {
        return id_salao;
    }
    
    public void setId(Long id) {
        this.id_salao = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getLogradouro() {
        return logradouro;
    }
    
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getComplemento() {
        return complemento;
    }
    
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getCep() {
        return cep;
    }
    
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getHorarioAbertura() {
        return horarioAbertura;
    }
    
    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }
    
    public String getHorarioFechamento() {
        return horarioFechamento;
    }
    
    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    public Dono getDono() {
        return dono;
    }
    
    public void setDono(Dono dono) {
        this.dono = dono;
    }
    
    public List<Profissional> getProfissionais() {
        return profissionais;
    }
    
    public void setProfissionais(List<Profissional> profissionais) {
        this.profissionais = profissionais;
    }
    
    public List<Servico> getServicos() {
        return servicos;
    }
    
    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
} 