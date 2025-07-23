package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Favorito;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO FAVORITO - TRANSFERÊNCIA DE DADOS PARA FAVORITOS
 * 
 * Este DTO é usado para transferir dados de favoritos entre o frontend e backend.
 * Contém classes Request (para receber dados) e Response (para enviar dados).
 * 
 */
public class FavoritoDTO {
    
    /**
     * REQUEST - DADOS ENVIADOS PELO FRONTEND
     * Usado para adicionar um novo favorito
     */

    public static class Request {
        private Long salaoId;
        
        // Construtores
        public Request() {}
        
        public Request(Long salaoId) {
            this.salaoId = salaoId;
        }
        
        // Getters e Setters
        public Long getSalaoId() {
            return salaoId;
        }
        
        public void setSalaoId(Long salaoId) {
            this.salaoId = salaoId;
        }
    }
    
    
    /**
     * RESPONSE SIMPLES - APENAS INFORMAÇÕES BÁSICAS
     * Usado para listar favoritos de forma simplificada
     */
    public static class ResponseSimples {
        private Long id;
        private Long salaoId;
        private String nomeSalao;
        private String enderecoSalao;
        private String imagemUrlSalao;
        private LocalDateTime dataFavoritado;
        
        // Construtores
        public ResponseSimples() {}
        
        public ResponseSimples(Long id, Long salaoId, String nomeSalao, String enderecoSalao, String imagemUrlSalao, LocalDateTime dataFavoritado) {
            this.id = id;
            this.salaoId = salaoId;
            this.nomeSalao = nomeSalao;
            this.enderecoSalao = enderecoSalao;
            this.imagemUrlSalao = imagemUrlSalao;
            this.dataFavoritado = dataFavoritado;
        }
        
        /**
         * CONSTRUTOR A PARTIR DA ENTIDADE
         * 
         * @param favorito Entidade Favorito
         */
        public ResponseSimples(Favorito favorito) {
            this.id = favorito.getId();
            this.salaoId = favorito.getSalao().getId();
            this.nomeSalao = favorito.getSalao().getNome();
            this.enderecoSalao = favorito.getSalao().getEndereco();
            this.imagemUrlSalao = favorito.getSalao().getImagemUrl();
            this.dataFavoritado = favorito.getDataFavoritado();
        }
        
        // Getters e Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public Long getSalaoId() {
            return salaoId;
        }
        
        public void setSalaoId(Long salaoId) {
            this.salaoId = salaoId;
        }
        
        public String getNomeSalao() {
            return nomeSalao;
        }
        
        public void setNomeSalao(String nomeSalao) {
            this.nomeSalao = nomeSalao;
        }
        
        public String getEnderecoSalao() {
            return enderecoSalao;
        }
        
        public void setEnderecoSalao(String enderecoSalao) {
            this.enderecoSalao = enderecoSalao;
        }
        
        public String getImagemUrlSalao() {
            return imagemUrlSalao;
        }
        
        public void setImagemUrlSalao(String imagemUrlSalao) {
            this.imagemUrlSalao = imagemUrlSalao;
        }
        
        public LocalDateTime getDataFavoritado() {
            return dataFavoritado;
        }
        
        public void setDataFavoritado(LocalDateTime dataFavoritado) {
            this.dataFavoritado = dataFavoritado;
        }
    }
} 