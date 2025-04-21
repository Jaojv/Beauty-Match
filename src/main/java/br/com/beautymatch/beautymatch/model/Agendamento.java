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

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime hora;

    // @ManyToOne: Indica relacionamento muitos-para-um com a entidade Cliente
    // @JoinColumn: Define a coluna de chave estrangeira
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

    public Agendamento(LocalDate data, LocalTime hora, Cliente cliente, Profissional profissional, Servico servico) {
        this.data = data;
        this.hora = hora;
        this.cliente = cliente;
        this.profissional = profissional;
        this.servico = servico;
    }
    public Agendamento(){}

    public Long getId_agendamento() {
        return id_agendamento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id_agendamento=" + id_agendamento +
                ", data=" + data +
                ", hora=" + hora +
                ", cliente=" + cliente +
                ", profissional=" + profissional +
                ", servico=" + servico +
                '}';
    }
}