package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.RespostaQuiz;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RespostaQuizDTO {
    
    private Long id;
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotEmpty(message = "As respostas são obrigatórias")
    private Map<String, String> respostas = new HashMap<>();
    
    private String criterioGerado;
    
    public RespostaQuizDTO() {}
    
    public RespostaQuizDTO(Long id, Long clienteId, Map<String, String> respostas, String criterioGerado) {
        this.id = id;
        this.clienteId = clienteId;
        this.respostas = respostas;
        this.criterioGerado = criterioGerado;
    }
    
    // Método para converter entidade para DTO
    public static RespostaQuizDTO fromEntity(RespostaQuiz respostaQuiz) {
        if (respostaQuiz == null) {
            return null;
        }
        
        return new RespostaQuizDTO(
                respostaQuiz.getId(),
                respostaQuiz.getCliente() != null ? respostaQuiz.getCliente().getIdUsuario() : null,
                respostaQuiz.getRespostas(),
                respostaQuiz.getCriterioGerado()
        );
    }
    
    // Método para converter DTO para entidade
    public RespostaQuiz toEntity() {
        RespostaQuiz respostaQuiz = new RespostaQuiz();
        respostaQuiz.setId(this.id);
        respostaQuiz.setRespostas(this.respostas);
        respostaQuiz.setCriterioGerado(this.criterioGerado);
        return respostaQuiz;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public Map<String, String> getRespostas() {
        return respostas;
    }
    
    public String getCriterioGerado() {
        return criterioGerado;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public void setRespostas(Map<String, String> respostas) {
        this.respostas = respostas;
    }
    
    public void setCriterioGerado(String criterioGerado) {
        this.criterioGerado = criterioGerado;
    }
} 