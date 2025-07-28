package com.beauty.com.MatchBeauty.exception;

// Exceção customizada para erros específicos do módulo de Agendamento
// Usada para tratar erros relacionados a criação, atualização e validação de agendamentos
public class AgendamentoException extends RuntimeException {
    
    // Construtor com mensagem de erro
    // Usado quando há um erro específico no agendamento
    public AgendamentoException(String message) {
        super(message);
    }
    
    // Construtor com mensagem e causa do erro
    // Usado quando há um erro no agendamento causado por outra exceção
    public AgendamentoException(String message, Throwable cause) {
        super(message, cause);
    }
} 