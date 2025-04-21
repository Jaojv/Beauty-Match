package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// @Data: Anotação do Lombok que gera automaticamente getters, setters, toString, equals e hashCode
// @Entity: Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados
// @Table: Especifica o nome da tabela no banco de dados
@Data
@Entity
@Table(name = "profissionais")
public class Profissional {

    // @Id: Indica que este é o campo chave primária
    // @GeneratedValue: Configura a geração automática do ID
    // strategy = GenerationType.IDENTITY: Usa auto-incremento do banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_profissional;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especialidade;

    @Column(length = 15, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    // @OneToMany: Indica relacionamento um-para-muitos com a entidade Agendamento
    // mappedBy = "profissional": Indica que o campo 'profissional' na classe Agendamento controla o relacionamento
    @OneToMany(mappedBy = "profissional")
    private List<Agendamento> agendamentos;

    @Override
    public String toString() {
        return "Profissional{" +
                "id_profissional=" + id_profissional +
                ", nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Profissional(){}

    public Profissional(String nome, String especialidade, String telefone, String email, List<Agendamento> agendamentos) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
        this.agendamentos = agendamentos;
    }

    public Profissional(String nome, String especialidade, String telefone, String email) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId_profissional() {
        return id_profissional;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public void setAgendamentos(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

}
