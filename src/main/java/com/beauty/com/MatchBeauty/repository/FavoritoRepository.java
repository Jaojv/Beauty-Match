package com.beauty.com.MatchBeauty.repository;

import com.beauty.com.MatchBeauty.entity.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY FAVORITO - ACESSO A DADOS DE FAVORITOS
 * 
 * Interface para operações de banco de dados relacionadas aos favoritos.
 * Fornece métodos para buscar, salvar e deletar favoritos.
 * 
 * @author João [Seu Nome]
 * @version 1.0
 * @since 2024
 */
@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    
    /**
     * BUSCAR FAVORITOS POR CLIENTE
     * Retorna todos os favoritos de um cliente específico
     * 
     * @param clienteId ID do cliente
     * @return Lista de favoritos do cliente
     */
    @Query("SELECT f FROM Favorito f WHERE f.cliente.idUsuario = :clienteId ORDER BY f.dataFavoritado DESC")
    List<Favorito> findByClienteId(@Param("clienteId") Long clienteId);
    
    /**
     * VERIFICAR SE SALÃO ESTÁ FAVORITADO PELO CLIENTE
     * Verifica se um cliente específico favoritou um salão específico
     * 
     * @param clienteId ID do cliente
     * @param salaoId ID do salão
     * @return Optional contendo o favorito se existir
     */
    @Query("SELECT f FROM Favorito f WHERE f.cliente.idUsuario = :clienteId AND f.salao.id = :salaoId")
    Optional<Favorito> findByClienteIdAndSalaoId(@Param("clienteId") Long clienteId, @Param("salaoId") Long salaoId);
    
    /**
     * CONTAR FAVORITOS POR CLIENTE
     * Retorna a quantidade de favoritos de um cliente
     * 
     * @param clienteId ID do cliente
     * @return Quantidade de favoritos
     */
    @Query("SELECT COUNT(f) FROM Favorito f WHERE f.cliente.idUsuario = :clienteId")
    Long countByClienteId(@Param("clienteId") Long clienteId);
} 