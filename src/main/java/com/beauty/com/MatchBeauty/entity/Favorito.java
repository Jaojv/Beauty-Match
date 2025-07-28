package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * ENTIDADE FAVORITO - REPRESENTA UM SALÃO FAVORITADO POR UM CLIENTE
 * 
 * Esta entidade representa a relação entre um cliente e um salão favoritado.
 * Permite que clientes marquem salões como favoritos para acesso rápido.
 * 
 * RELACIONAMENTOS:
 * - Um favorito pertence a um cliente (ManyToOne)
 * - Um favorito referencia um salão (ManyToOne)
 * 
 * CARACTERÍSTICAS:
 * - Data de favoritado para histórico
 * - Relacionamento único entre cliente e salão
 * 
 * @author João [Seu Nome]
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "favorito")
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {
    
    /**
     * ID ÚNICO DO FAVORITO
     * Chave primária auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long id;
    
    /**
     * CLIENTE QUE FAVORITOU
     * Relacionamento ManyToOne com a entidade Cliente
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    /**
     * SALÃO FAVORITADO
     * Relacionamento ManyToOne com a entidade Salao
     */
    @ManyToOne
    @JoinColumn(name = "salao_id", nullable = false)
    private Salao salao;
    
    /**
     * DATA EM QUE FOI FAVORITADO
     * Data e hora do momento em que o cliente favoritou o salão
     */
    @Column(name = "data_favoritado", nullable = false)
    private LocalDateTime dataFavoritado;
    
    /**
     * CONSTRUTOR COM PARÂMETROS BÁSICOS
     * 
     * @param cliente Cliente que favoritou
     * @param salao Salão favoritado
     */
    public Favorito(Cliente cliente, Salao salao) {
        this.cliente = cliente;
        this.salao = salao;
        this.dataFavoritado = LocalDateTime.now();
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public Salao getSalao() {
        return salao;
    }
    
    public void setSalao(Salao salao) {
        this.salao = salao;
    }
    
    public LocalDateTime getDataFavoritado() {
        return dataFavoritado;
    }
    
    public void setDataFavoritado(LocalDateTime dataFavoritado) {
        this.dataFavoritado = dataFavoritado;
    }
} 