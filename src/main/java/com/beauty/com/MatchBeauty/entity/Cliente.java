package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
    // Pode adicionar métodos específicos de Cliente se necessário
} 