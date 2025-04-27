package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

// @Data: Anotação do Lombok que gera automaticamente getters, setters, toString, equals e hashCode
// @Entity: Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados
// @Table: Especifica o nome da tabela no banco de dados
@Data
@Entity
@Table(name = "donos")
public class Dono {

    // @Id: Indica que este é o campo chave primária
    // @GeneratedValue: Configura a geração automática do ID
    // strategy = GenerationType.IDENTITY: Usa auto-incremento do banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dono;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Salao> saloes;

    @Override
    public String toString() {
        return "Dono{" +
                "id_dono=" + id_dono +
                ", usuario=" + usuario +
                '}';
    }

    public Dono() {
    }
    
    public Dono(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id_dono;
    }

    public void setId(Long id) {
        this.id_dono = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Salao> getSaloes() {
        return saloes;
    }

    public void setSaloes(List<Salao> saloes) {
        this.saloes = saloes;
    }
} 