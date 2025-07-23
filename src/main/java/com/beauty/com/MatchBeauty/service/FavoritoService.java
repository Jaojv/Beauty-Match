package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.dto.FavoritoDTO;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Favorito;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import com.beauty.com.MatchBeauty.repository.FavoritoRepository;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE FAVORITO - LÓGICA DE NEGÓCIO PARA FAVORITOS
 * 
 * Classe responsável pela lógica de negócio relacionada aos favoritos.
 * Gerencia operações de adicionar, remover e listar favoritos.
 * 
 * @author João [Seu Nome]
 * @version 1.0
 * @since 2024
 */
@Service
public class FavoritoService {
    
    @Autowired
    private FavoritoRepository favoritoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private SalaoRepository salaoRepository;
    
    /**
     * LISTAR FAVORITOS DO CLIENTE
     * Retorna todos os favoritos de um cliente específico
     * 
     * @param clienteId ID do cliente
     * @return Lista de DTOs de favoritos
     */
    public List<FavoritoDTO.ResponseSimples> listarFavoritosCliente(Long clienteId) {
        List<Favorito> favoritos = favoritoRepository.findByClienteId(clienteId);
        return favoritos.stream()
                .map(FavoritoDTO.ResponseSimples::new)
                .collect(Collectors.toList());
    }
    
    /**
     * ADICIONAR FAVORITO
     * Adiciona um salão aos favoritos de um cliente
     * 
     * @param clienteId ID do cliente
     * @param salaoId ID do salão
     * @return DTO do favorito criado
     */
    public FavoritoDTO.ResponseSimples adicionarFavorito(Long clienteId, Long salaoId) {
        // Verificar se o cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // Verificar se o salão existe
        Salao salao = salaoRepository.findById(salaoId)
                .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
        
        // Verificar se já existe favorito
        if (favoritoRepository.findByClienteIdAndSalaoId(clienteId, salaoId).isPresent()) {
            throw new RuntimeException("Salão já está nos favoritos");
        }
        
        // Criar novo favorito
        Favorito favorito = new Favorito(cliente, salao);
        Favorito favoritoSalvo = favoritoRepository.save(favorito);
        
        return new FavoritoDTO.ResponseSimples(favoritoSalvo);
    }
    
    /**
     * REMOVER FAVORITO
     * Remove um salão dos favoritos de um cliente
     * 
     * @param clienteId ID do cliente
     * @param salaoId ID do salão
     * @return true se removido com sucesso
     */
    public boolean removerFavorito(Long clienteId, Long salaoId) {
        // Verificar se o favorito existe
        Favorito favorito = favoritoRepository.findByClienteIdAndSalaoId(clienteId, salaoId)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));
        
        // Remover favorito
        favoritoRepository.delete(favorito);
        return true;
    }
    
    /**
     * VERIFICAR SE SALÃO ESTÁ FAVORITADO
     * Verifica se um cliente favoritou um salão específico
     * 
     * @param clienteId ID do cliente
     * @param salaoId ID do salão
     * @return true se favoritado, false caso contrário
     */
    public boolean verificarFavorito(Long clienteId, Long salaoId) {
        return favoritoRepository.findByClienteIdAndSalaoId(clienteId, salaoId).isPresent();
    }
    
    /**
     * CONTAR FAVORITOS DO CLIENTE
     * Retorna a quantidade de favoritos de um cliente
     * 
     * @param clienteId ID do cliente
     * @return Quantidade de favoritos
     */
    public Long contarFavoritosCliente(Long clienteId) {
        return favoritoRepository.countByClienteId(clienteId);
    }
} 