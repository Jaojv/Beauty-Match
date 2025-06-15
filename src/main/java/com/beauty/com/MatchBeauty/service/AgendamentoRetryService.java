package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AgendamentoRetryService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    // Mapa para armazenar as tentativas de cada agendamento
    private final ConcurrentHashMap<Long, AtomicInteger> tentativasPorAgendamento = new ConcurrentHashMap<>();
    
    // Número máximo de tentativas
    private static final int MAX_TENTATIVAS = 3;
    
    // Lista de agendamentos que falharam após todas as tentativas
    private final List<Agendamento> agendamentosComFalha = new ArrayList<>();

    /**
     * Tenta concluir um agendamento com mecanismo de retry
     * @param agendamento Agendamento a ser concluído
     * @return true se conseguiu concluir, false se falhou após todas as tentativas
     */
    public boolean tentarConcluirAgendamento(Agendamento agendamento) {
        // Inicializa ou incrementa o contador de tentativas
        AtomicInteger tentativas = tentativasPorAgendamento.computeIfAbsent(
            agendamento.getId(),
            k -> new AtomicInteger(0)
        );
        
        int tentativaAtual = tentativas.incrementAndGet();
        
        try {
            // Tenta concluir o agendamento
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);
            agendamentoRepository.save(agendamento);
            
            // Se conseguiu, remove do mapa de tentativas
            tentativasPorAgendamento.remove(agendamento.getId());
            return true;
            
        } catch (Exception e) {
            // Se falhou e ainda não atingiu o máximo de tentativas
            if (tentativaAtual < MAX_TENTATIVAS) {
                return false; // Retorna false para tentar novamente
            }
            
            // Se atingiu o máximo de tentativas
            agendamentosComFalha.add(agendamento);
            tentativasPorAgendamento.remove(agendamento.getId());
            return false;
        }
    }

    /**
     * Retorna a lista de agendamentos que falharam após todas as tentativas
     * @return Lista de agendamentos com falha
     */
    public List<Agendamento> getAgendamentosComFalha() {
        return new ArrayList<>(agendamentosComFalha);
    }

    /**
     * Limpa a lista de agendamentos com falha
     */
    public void limparAgendamentosComFalha() {
        agendamentosComFalha.clear();
    }

    /**
     * Retorna o número de tentativas de um agendamento
     * @param agendamentoId ID do agendamento
     * @return Número de tentativas
     */
    public int getNumeroTentativas(Long agendamentoId) {
        AtomicInteger tentativas = tentativasPorAgendamento.get(agendamentoId);
        return tentativas != null ? tentativas.get() : 0;
    }
} 