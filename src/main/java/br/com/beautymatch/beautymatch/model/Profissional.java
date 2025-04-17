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

    // @Column: Define propriedades da coluna no banco de dados
    // nullable = false: O campo não pode ser nulo
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especialidade;

    // length = 15: Define o tamanho máximo da coluna para armazenar números de telefone
    @Column(length = 15, unique = true)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    // @OneToMany: Indica relacionamento um-para-muitos com a entidade Agendamento
    // mappedBy = "profissional": Indica que o campo 'profissional' na classe Agendamento controla o relacionamento
    @OneToMany(mappedBy = "profissional")
    private List<Agendamento> agendamentos;
}
