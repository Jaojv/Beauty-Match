package br.com.beautymatch.beautymatch.model;

import jakarta.persistence.*;
import lombok.Data;

// @Data: Anotação do Lombok que gera automaticamente getters, setters, toString, equals e hashCode
// @Entity: Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados
// @Table: Especifica o nome da tabela no banco de dados
@Data
@Entity
@Table(name = "admins")
public class Admin {

    // @Id: Indica que este é o campo chave primária
    // @GeneratedValue: Configura a geração automática do ID
    // strategy = GenerationType.IDENTITY: Usa auto-incremento do banco de dados
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_admin;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Override
    public String toString() {
        return "Admin{" +
                "id_admin=" + id_admin +
                ", usuario=" + usuario +
                '}';
    }

    public Admin() {
    }
    
    public Admin(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id_admin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
} 