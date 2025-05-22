package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "admin")
public class Admin extends Usuario {
    public Admin() {
        super();
    }

    public Admin(Long idUsuario, String username, String password, String email, String telefone, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }
    // Pode adicionar métodos específicos de Admin se necessário
} 