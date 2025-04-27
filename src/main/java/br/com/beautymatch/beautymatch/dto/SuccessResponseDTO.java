package br.com.beautymatch.beautymatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDTO<T> {
    
    private String message;
    private T data;
} 