package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PROFISSIONAL")
public class Profissional extends Usuario {
    // Pode adicionar métodos específicos de Profissional se necessário
} 