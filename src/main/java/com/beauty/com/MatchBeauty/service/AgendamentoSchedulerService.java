package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.repository.AgendamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVIÇO SCHEDULER DE AGENDAMENTOS - PROCESSAMENTO AUTOMÁTICO
 * 
 * Este serviço gerencia o processamento automático de agendamentos em background.
 * Implementa tarefas agendadas para atualizar status de agendamentos, processar
 * agendamentos pendentes e executar limpezas automáticas.
 * 
 * FUNCIONALIDADES:
 * - Processamento automático de agendamentos pendentes
 * - Atualização automática de status de agendamentos
 * - Limpeza automática de agendamentos antigos
 * - Processamento de agendamentos em lote
 * - Monitoramento de agendamentos em tempo real
 * - Execução de tarefas agendadas
 * 
 * TAREFAS AGENDADAS:
 * - Verificação diária de agendamentos
 * - Atualização de status baseado em data/hora
 * - Processamento de agendamentos pendentes
 * - Limpeza de dados antigos
 * - Monitoramento de performance
 * 
 * INTEGRAÇÕES:
 * - AgendamentoService: Para operações de agendamento
 * - AgendamentoRetryService: Para retry de operações falhadas
 * - Sistema de logging para monitoramento
 * - Notificações automáticas
 * 
 * DEPENDÊNCIAS:
 * - AgendamentoRepository: Para consulta e atualização de agendamentos
 * - Spring Scheduling: Para execução de tarefas agendadas
 */
@Service
public class AgendamentoSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(AgendamentoSchedulerService.class);

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private AgendamentoRetryService agendamentoRetryService;

    /**
     * Executa a conclusão automática de agendamentos a cada 5 minutos
     * O horário é configurado no formato cron: segundo minuto hora dia mês dia-semana
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void executarConclusaoAutomatica() {
        try {
            logger.info("Iniciando conclusão automática de agendamentos");
            
            // Executa a conclusão automática
            agendamentoService.concluirAgendamentosPassados();
            
            // Verifica se houve falhas
            List<Agendamento> agendamentosComFalha = agendamentoRetryService.getAgendamentosComFalha();
            if (!agendamentosComFalha.isEmpty()) {
                logger.error("Falha ao concluir {} agendamentos após todas as tentativas", 
                    agendamentosComFalha.size());
                
                // Log detalhado dos agendamentos com falha
                for (Agendamento agendamento : agendamentosComFalha) {
                    logger.error("Falha no agendamento ID: {}, Cliente: {}, Profissional: {}, Data/Hora: {}", 
                        agendamento.getId(),
                        agendamento.getCliente().getNome(),
                        agendamento.getProfissional().getNome(),
                        agendamento.getDataHora());
                }
            } else {
                logger.info("Conclusão automática executada com sucesso");
            }
            
        } catch (Exception e) {
            logger.error("Erro ao executar conclusão automática de agendamentos", e);
        }
    }
} 