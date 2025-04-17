package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Data
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_servico;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Duration duracao;

    @OneToMany(mappedBy = "servico")
    private List<Agendamento> agendamentos;
}
