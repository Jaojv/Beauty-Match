package com.beauty.com.MatchBeauty.entity;

/**
 * ENUM STATUS AGENDAMENTO - DEFINE OS ESTADOS DE UM AGENDAMENTO
 * 
 * Este enum define os diferentes estados que um agendamento pode ter no sistema.
 * Cada status representa uma fase específica do ciclo de vida do agendamento.
 * 
 * STATUS DISPONÍVEIS:
 * - AGENDADO: Agendamento confirmado e aguardando realização
 * - CONCLUIDO: Serviço realizado com sucesso
 * - CANCELADO: Agendamento cancelado pelo cliente ou profissional
 * - FALTANTE: Cliente não compareceu ao agendamento
 *
 */
public enum StatusAgendamento {
    AGENDADO,    // Agendamento confirmado
    CONCLUIDO,   // Serviço realizado
    CANCELADO,   // Agendamento cancelado
    FALTANTE     // Cliente não compareceu
} 