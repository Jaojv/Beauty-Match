package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

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