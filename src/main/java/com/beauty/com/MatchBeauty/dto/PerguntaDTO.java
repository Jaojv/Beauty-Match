package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Pergunta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// DTO para perguntas do quiz
public class PerguntaDTO {
    
    private Long id;
    
    @NotBlank(message = "O texto da pergunta é obrigatório")
    @Size(max = 500, message = "O texto da pergunta deve ter no máximo 500 caracteres")
    private String texto;
    
    @NotNull(message = "A ordem da pergunta é obrigatória")
    private Integer ordem;
    
    private List<AlternativaDTO> alternativas = new ArrayList<>();
    
    private Boolean ativo;
    
    public PerguntaDTO() {}
    
    public PerguntaDTO(Long id, String texto, Integer ordem, List<AlternativaDTO> alternativas, Boolean ativo) {
        this.id = id;
        this.texto = texto;
        this.ordem = ordem;
        this.alternativas = alternativas;
        this.ativo = ativo;
    }
    
    // Método para converter entidade para DTO
    public static PerguntaDTO fromEntity(Pergunta pergunta) {
        if (pergunta == null) {
            return null;
        }
        
        List<AlternativaDTO> alternativasDTO = pergunta.getAlternativas().stream()
                .map(AlternativaDTO::fromEntity)
                .collect(Collectors.toList());
        
        return new PerguntaDTO(
                pergunta.getId(),
                pergunta.getTexto(),
                pergunta.getOrdem(),
                alternativasDTO,
                pergunta.getAtivo()
        );
    }
    
    // Método para converter DTO para entidade
    public Pergunta toEntity() {
        Pergunta pergunta = new Pergunta();
        pergunta.setId(this.id);
        pergunta.setTexto(this.texto);
        pergunta.setOrdem(this.ordem);
        pergunta.setAtivo(this.ativo != null ? this.ativo : true);
        return pergunta;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getTexto() {
        return texto;
    }
    
    public Integer getOrdem() {
        return ordem;
    }
    
    public List<AlternativaDTO> getAlternativas() {
        return alternativas;
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
    
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    public void setAlternativas(List<AlternativaDTO> alternativas) {
        this.alternativas = alternativas;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
} 