package com.beauty.com.MatchBeauty.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Classe de configuração de agendamento do sistema
// Habilita a execução de tarefas programadas
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // Configuração para habilitar o agendamento de tarefas
    // Permite usar @Scheduled em métodos para execução automática
} 