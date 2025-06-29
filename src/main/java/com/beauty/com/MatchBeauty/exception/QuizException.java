package com.beauty.com.MatchBeauty.exception;

/**
 * Exceção customizada para erros específicos do módulo Quiz
 */
public class QuizException extends RuntimeException {
    
    public QuizException(String message) {
        super(message);
    }
    
    public QuizException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public QuizException(Throwable cause) {
        super(cause);
    }
} 