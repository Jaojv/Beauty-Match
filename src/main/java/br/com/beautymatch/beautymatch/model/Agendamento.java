package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

// @Data: Anotação do Lombok que gera automaticamente getters, setters, toString, equals e hashCode
// @Entity: Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados
// @Table: Especifica o nome da tabela no banco de dados
@Data
@Entity
@Table(name = "agendamentos")
public class Agendamento {

    // @Id: Indica que este é o campo chave primária
    // @GeneratedValue: Configura a geração automática do ID
    // strategy = GenerationType.IDENTITY: Usa auto-incremento do banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_agendamento;

    // @Column: Define propriedades da coluna no banco de dados
    // nullable = false: O campo não pode ser nulo
    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime hora;

    // @ManyToOne: Indica relacionamento muitos-para-um com a entidade Cliente
    // @JoinColumn: Define a coluna de chave estrangeira
    // nullable = false: O relacionamento é obrigatório
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    // @ManyToOne: Indica relacionamento muitos-para-um com a entidade Profissional
    @ManyToOne
    @JoinColumn(name = "id_profissional", nullable = false)
    private Profissional profissional;

    // @ManyToOne: Indica relacionamento muitos-para-um com a entidade Servico
    @ManyToOne
    @JoinColumn(name = "id_servico", nullable = false)
    private Servico servico;
}