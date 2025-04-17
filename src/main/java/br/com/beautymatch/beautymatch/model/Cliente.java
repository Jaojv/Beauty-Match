package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// @Data: Anotação do Lombok que gera automaticamente getters, setters, toString, equals e hashCode
// @Entity: Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados
// @Table: Especifica o nome da tabela no banco de dados
@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    // @Id: Indica que este é o campo chave primária
    // @GeneratedValue: Configura a geração automática do ID
    // strategy = GenerationType.IDENTITY: Usa auto-incremento do banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    // @Column: Define propriedades da coluna no banco de dados
    // nullable = false: O campo não pode ser nulo
    @Column(nullable = false)
    private String nome;

    // length = 14: Define o tamanho máximo da coluna para armazenar CPF (formato: 123.456.789-00)
    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    // length = 15: Define o tamanho máximo da coluna para armazenar números de telefone
    @Column(length = 15, nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    // @OneToMany: Indica relacionamento um-para-muitos com a entidade Agendamento
    // mappedBy = "cliente": Indica que o campo 'cliente' na classe Agendamento controla o relacionamento
    @OneToMany(mappedBy = "cliente")
    private List<Agendamento> agendamentos;

    @Override
    public String toString() {
        return "Cliente{" +
                "id_cliente=" + id_cliente +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Cliente() {
    }
    public Cliente(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
}
