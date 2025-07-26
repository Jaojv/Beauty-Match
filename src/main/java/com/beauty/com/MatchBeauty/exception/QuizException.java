package com.beauty.com.MatchBeauty.exception;

// Exceção customizada para erros específicos do módulo Quiz
// Usada para tratar erros relacionados a perguntas, respostas e recomendações
public class QuizException extends RuntimeException {
    
    // Construtor com mensagem de erro
    // Usado quando há um erro específico no quiz
    public QuizException(String message) {
        super(message);
    }
    
    // Construtor com mensagem e causa do erro
    // Usado quando há um erro no quiz causado por outra exceção
    public QuizException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Construtor apenas com causa do erro
    // Usado quando há um erro no quiz causado por outra exceção sem mensagem específica
    public QuizException(Throwable cause) {
        super(cause);
    }
} 