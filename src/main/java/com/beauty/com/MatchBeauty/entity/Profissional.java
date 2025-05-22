package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "profissional")
public class Profissional extends Usuario {
    
    public Profissional() {
        super();
    }

    public Profissional(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }
    // Pode adicionar métodos específicos de Profissional se necessário
} 