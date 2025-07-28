package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Alternativa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO para alternativas do quiz
public class AlternativaDTO {
    
    private Long id;
    
    @NotBlank(message = "O texto da alternativa é obrigatório")
    @Size(max = 200, message = "O texto da alternativa deve ter no máximo 200 caracteres")
    private String texto;
    
    @NotNull(message = "O ID da pergunta é obrigatório")
    private Long perguntaId;
    
    private Boolean ativo;
    
    public AlternativaDTO() {}
    
    public AlternativaDTO(Long id, String texto, Long perguntaId, Boolean ativo) {
        this.id = id;
        this.texto = texto;
        this.perguntaId = perguntaId;
        this.ativo = ativo;
    }
    
    // Método para converter entidade para DTO
    public static AlternativaDTO fromEntity(Alternativa alternativa) {
        if (alternativa == null) {
            return null;
        }
        
        return new AlternativaDTO(
                alternativa.getId(),
                alternativa.getTexto(),
                alternativa.getPergunta() != null ? alternativa.getPergunta().getId() : null,
                alternativa.getAtivo()
        );
    }
    
    // Método para converter DTO para entidade
    public Alternativa toEntity() {
        Alternativa alternativa = new Alternativa();
        alternativa.setId(this.id);
        alternativa.setTexto(this.texto);
        alternativa.setAtivo(this.ativo != null ? this.ativo : true);
        return alternativa;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public Long getPerguntaId() {
        return perguntaId;
    }
    
    public Boolean getAtivo() {
        return ativo;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setTexto(String texto) {
        this.texto = texto;
    }
    
    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
} 