package com.beauty.com.MatchBeauty.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PROPRIETARIO")
public class Proprietario extends Usuario {
    // Pode adicionar métodos específicos de Proprietario se necessário
} 