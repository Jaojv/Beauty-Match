package com.beauty.com.MatchBeauty.exception;

// Exceção customizada para erros específicos do módulo de Favoritos
// Usada para tratar erros relacionados a adição, remoção e consulta de favoritos
public class FavoritoException extends RuntimeException {
    
    // Construtor com mensagem de erro
    // Usado quando há um erro específico nos favoritos
    public FavoritoException(String message) {
        super(message);
    }
    
    // Construtor com mensagem e causa do erro
    // Usado quando há um erro nos favoritos causado por outra exceção
    public FavoritoException(String message, Throwable cause) {
        super(message, cause);
    }
} 