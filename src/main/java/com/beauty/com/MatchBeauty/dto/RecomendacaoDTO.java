package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Recomendacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO para recomendações do quiz
public class RecomendacaoDTO {
    
    private Long id;
    
    @NotBlank(message = "O critério é obrigatório")
    @Size(max = 100, message = "O critério deve ter no máximo 100 caracteres")
    private String criterio;
    
    @NotBlank(message = "A descrição da recomendação é obrigatória")
    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    private String descricao;
    
    private Boolean ativo;
    
    public RecomendacaoDTO() {}
    
    public RecomendacaoDTO(Long id, String criterio, String descricao, Boolean ativo) {
        this.id = id;
        this.criterio = criterio;
        this.descricao = descricao;
        this.ativo = ativo;
    }
    
    // Método para converter entidade para DTO
    public static RecomendacaoDTO fromEntity(Recomendacao recomendacao) {
        if (recomendacao == null) {
            return null;
        }
        
        return new RecomendacaoDTO(
                recomendacao.getId(),
                recomendacao.getCriterio(),
                recomendacao.getDescricao(),
                recomendacao.getAtivo()
        );
    }
    
    // Método para converter DTO para entidade
    public Recomendacao toEntity() {
        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setId(this.id);
        recomendacao.setCriterio(this.criterio);
        recomendacao.setDescricao(this.descricao);
        recomendacao.setAtivo(this.ativo != null ? this.ativo : true);
        return recomendacao;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getCriterio() {
        return criterio;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
} 