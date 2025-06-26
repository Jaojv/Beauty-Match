package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.exception.AgendamentoException;
import com.beauty.com.MatchBeauty.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private HorarioTrabalhoService horarioTrabalhoService;

    @Autowired
    private SalaoService salaoService;

    @Autowired
    private AgendamentoRetryService agendamentoRetryService;

    public List<Agendamento> listarAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public Agendamento buscarAgendamento(Long id) {
        return agendamentoRepository.findById(id).orElse(null);
    }

    public Agendamento criarAgendamento(Agendamento agendamento) {
        // Validar disponibilidade do profissional
        if (!horarioTrabalhoService.verificarDisponibilidadeHorarioTrabalho(agendamento.getProfissional(), agendamento.getDataHora())) {
            throw new AgendamentoException("Profissional não está disponível no horário selecionado");
        }

        // Validar horários bloqueados
        if (horarioTrabalhoService.verificarHorarioBloqueado(agendamento.getProfissional(), agendamento.getDataHora())) {
            throw new AgendamentoException("Horário está bloqueado");
        }

        // Validar conflitos de horário
        if (existeConflitoHorario(agendamento)) {
            throw new AgendamentoException("Existe conflito de horário com outro agendamento");
        }

        agendamento.setStatus(StatusAgendamento.AGENDADO);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento atualizarAgendamento(Agendamento agendamento) {
        if (agendamentoRepository.existsById(agendamento.getId())) {
            // Validar disponibilidade do profissional
            if (!horarioTrabalhoService.verificarDisponibilidadeHorarioTrabalho(agendamento.getProfissional(), agendamento.getDataHora())) {
                throw new AgendamentoException("Profissional não está disponível no horário selecionado");
            }

            // Validar horários bloqueados
            if (horarioTrabalhoService.verificarHorarioBloqueado(agendamento.getProfissional(), agendamento.getDataHora())) {
                throw new AgendamentoException("Horário está bloqueado");
            }

            // Validar conflitos de horário (excluindo o próprio agendamento)
            if (existeConflitoHorario(agendamento)) {
                throw new AgendamentoException("Existe conflito de horário com outro agendamento");
            }

            return agendamentoRepository.save(agendamento);
        }
        return null;
    }

    public boolean deletarAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Agendamento> buscarAgendamentosPorCliente(Long clienteId) {
        return agendamentoRepository.findByClienteIdUsuario(clienteId);
    }

    public List<Agendamento> buscarAgendamentosPorProfissional(Long profissionalId) {
        return agendamentoRepository.findByProfissionalIdUsuario(profissionalId);
    }

    public List<Agendamento> buscarAgendamentosPorSalao(Long salaoId) {
        return agendamentoRepository.findBySalaoId(salaoId);
    }

    public List<Agendamento> buscarAgendamentosPorStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status);
    }

    public List<Agendamento> buscarAgendamentosPorClienteEPeriodo(
        Long clienteId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findByClienteIdUsuarioAndDataHoraBetween(clienteId, inicio, fim);
    }

    public List<Agendamento> buscarAgendamentosPorProfissionalEPeriodo(
        Long profissionalId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findByProfissionalIdUsuarioAndDataHoraBetween(profissionalId, inicio, fim);
    }

    public List<Agendamento> buscarAgendamentosPorSalaoEPeriodo(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return agendamentoRepository.findBySalaoIdAndDataHoraBetween(salaoId, inicio, fim);
    }

    public Agendamento cancelarAgendamento(Long id) {
        Agendamento agendamento = buscarAgendamento(id);
        if (agendamento != null) {
            agendamento.setStatus(StatusAgendamento.CANCELADO);
            return agendamentoRepository.save(agendamento);
        }
        return null;
    }
    
    public Agendamento concluirAgendamento(Long id) {
        Agendamento agendamento = buscarAgendamento(id);
        if (agendamento != null) {
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);
            return agendamentoRepository.save(agendamento);
        }
        return null;
    }

    /**
     * Conclui automaticamente os agendamentos que já passaram do horário
     * Considera a duração do serviço para determinar se o agendamento já terminou
     * Usa mecanismo de retry para casos de falha
     */
    public void concluirAgendamentosPassados() {
        LocalDateTime agora = LocalDateTime.now();
        
        // Buscar todos os agendamentos agendados
        List<Agendamento> agendamentosAgendados = agendamentoRepository.findByStatus(StatusAgendamento.AGENDADO);
        
        for (Agendamento agendamento : agendamentosAgendados) {
            // Calcula o horário de término do agendamento (data/hora + duração do serviço)
            LocalDateTime horarioTermino = agendamento.getDataHora()
                .plusMinutes(agendamento.getServico().getDuracaoMinutos());
            
            // Se o horário atual for posterior ao horário de término, tenta concluir o agendamento
            if (agora.isAfter(horarioTermino)) {
                agendamentoRetryService.tentarConcluirAgendamento(agendamento);
            }
        }
        
        // Verifica se há agendamentos que falharam após todas as tentativas
        List<Agendamento> agendamentosComFalha = agendamentoRetryService.getAgendamentosComFalha();
        if (!agendamentosComFalha.isEmpty()) {
            // TODO: Implementar notificação para administrador sobre falhas
            // Por exemplo, enviar email, criar log de erro, etc.
            
            // Limpa a lista de falhas após processar
            agendamentoRetryService.limparAgendamentosComFalha();
        }
    }

    private boolean existeConflitoHorario(Agendamento agendamento) {
        // Buscar todos os agendamentos do salão no mesmo dia
        LocalDateTime inicioDia = agendamento.getDataHora().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime fimDia = inicioDia.plusDays(1);
        
        List<Agendamento> agendamentosDoDia = agendamentoRepository
            .findBySalaoIdAndDataHoraBetween(
                agendamento.getSalao().getId(),
                inicioDia,
                fimDia
            );

        // Verificar conflitos
        for (Agendamento agendamentoExistente : agendamentosDoDia) {
            if (agendamentoExistente.getId().equals(agendamento.getId())) {
                continue; // Ignora o próprio agendamento em caso de atualização
            }

            // Verifica se o status permite considerar como conflito
            if (agendamentoExistente.getStatus() == StatusAgendamento.CANCELADO ||
                agendamentoExistente.getStatus() == StatusAgendamento.FALTANTE) {
                continue;
            }

            // Verifica se há sobreposição de horários
            if (temSobreposicaoHorario(
                agendamento.getDataHora(),
                agendamento.getDataHora().plusMinutes(agendamento.getServico().getDuracaoMinutos()),
                agendamentoExistente.getDataHora(),
                agendamentoExistente.getDataHora().plusMinutes(agendamentoExistente.getServico().getDuracaoMinutos())
            )) {
                return true;
            }
        }

        return false;
    }

    private boolean temSobreposicaoHorario(
        LocalDateTime inicio1,
        LocalDateTime fim1,
        LocalDateTime inicio2,
        LocalDateTime fim2
    ) {
        return !inicio1.isAfter(fim2) && !inicio2.isAfter(fim1);
    }

    /**
     * Busca o histórico de agendamentos de um cliente
     * @param clienteId ID do cliente
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Lista de agendamentos concluídos no período
     */
    public List<Agendamento> buscarHistoricoCliente(Long clienteId, LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findByClienteIdUsuarioAndDataHoraBetween(
            clienteId,
            inicio,
            fim
        );
        return agendamentos.stream()
            .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
            .collect(Collectors.toList());
    }

    /**
     * Busca o histórico de agendamentos de um profissional
     * @param profissionalId ID do profissional
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Lista de agendamentos concluídos no período
     */
    public List<Agendamento> buscarHistoricoProfissional(Long profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByProfissionalIdUsuarioAndStatusAndDataHoraBetween(
            profissionalId,
            StatusAgendamento.CONCLUIDO,
            inicio,
            fim
        );
    }

    /**
     * Busca o histórico de agendamentos de um salão
     * @param salaoId ID do salão
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Lista de agendamentos concluídos no período
     */
    public List<Agendamento> buscarHistoricoSalao(Long salaoId, LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findBySalaoIdAndStatusAndDataHoraBetween(
            salaoId,
            StatusAgendamento.CONCLUIDO,
            inicio,
            fim
        );
    }

    /**
     * Busca estatísticas de agendamentos de um cliente
     * @param clienteId ID do cliente
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Estatísticas dos agendamentos no período
     */
    public AgendamentoEstatisticas buscarEstatisticasCliente(Long clienteId, LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findByClienteIdUsuarioAndDataHoraBetween(
            clienteId,
            inicio,
            fim
        );

        return calcularEstatisticas(agendamentos);
    }

    /**
     * Busca estatísticas de agendamentos de um profissional
     * @param profissionalId ID do profissional
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Estatísticas dos agendamentos no período
     */
    public AgendamentoEstatisticas buscarEstatisticasProfissional(Long profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findByProfissionalIdUsuarioAndDataHoraBetween(
            profissionalId,
            inicio,
            fim
        );

        return calcularEstatisticas(agendamentos);
    }

    /**
     * Busca estatísticas de agendamentos de um salão
     * @param salaoId ID do salão
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Estatísticas dos agendamentos no período
     */
    public AgendamentoEstatisticas buscarEstatisticasSalao(Long salaoId, LocalDateTime inicio, LocalDateTime fim) {
        List<Agendamento> agendamentos = agendamentoRepository.findBySalaoIdAndDataHoraBetween(
            salaoId,
            inicio,
            fim
        );

        return calcularEstatisticas(agendamentos);
    }

    /**
     * Calcula estatísticas a partir de uma lista de agendamentos
     * @param agendamentos Lista de agendamentos
     * @return Estatísticas calculadas
     */
    private AgendamentoEstatisticas calcularEstatisticas(List<Agendamento> agendamentos) {
        AgendamentoEstatisticas estatisticas = new AgendamentoEstatisticas();
        
        estatisticas.setTotalAgendamentos(agendamentos.size());
        
        estatisticas.setAgendamentosConcluidos(
            (int) agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .count()
        );
        
        estatisticas.setAgendamentosCancelados(
            (int) agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CANCELADO)
                .count()
        );
        
        estatisticas.setAgendamentosFaltantes(
            (int) agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.FALTANTE)
                .count()
        );
        
        estatisticas.setValorTotal(
            agendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.CONCLUIDO)
                .mapToDouble(a -> a.getServico().getPreco().doubleValue())
                .sum()
        );
        
        return estatisticas;
    }

    /**
     * Classe interna para armazenar estatísticas de agendamentos
     */
    public static class AgendamentoEstatisticas {
        private int totalAgendamentos;
        private int agendamentosConcluidos;
        private int agendamentosCancelados;
        private int agendamentosFaltantes;
        private double valorTotal;

        public int getTotalAgendamentos() {
            return totalAgendamentos;
        }

        public void setTotalAgendamentos(int totalAgendamentos) {
            this.totalAgendamentos = totalAgendamentos;
        }

        public int getAgendamentosConcluidos() {
            return agendamentosConcluidos;
        }

        public void setAgendamentosConcluidos(int agendamentosConcluidos) {
            this.agendamentosConcluidos = agendamentosConcluidos;
        }

        public int getAgendamentosCancelados() {
            return agendamentosCancelados;
        }

        public void setAgendamentosCancelados(int agendamentosCancelados) {
            this.agendamentosCancelados = agendamentosCancelados;
        }

        public int getAgendamentosFaltantes() {
            return agendamentosFaltantes;
        }

        public void setAgendamentosFaltantes(int agendamentosFaltantes) {
            this.agendamentosFaltantes = agendamentosFaltantes;
        }

        public double getValorTotal() {
            return valorTotal;
        }

        public void setValorTotal(double valorTotal) {
            this.valorTotal = valorTotal;
        }
    }

    /**
     * Busca horários ocupados por um profissional em uma data específica
     */
    public List<LocalTime> buscarHorariosOcupados(Long profissionalId, LocalDate data) {
        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.plusDays(1).atStartOfDay();
        
        List<Agendamento> agendamentosDoDia = agendamentoRepository
            .findByProfissionalIdUsuarioAndDataHoraBetween(profissionalId, inicioDia, fimDia);
        
        return agendamentosDoDia.stream()
            .filter(agendamento -> 
                agendamento.getStatus() != StatusAgendamento.CANCELADO &&
                agendamento.getStatus() != StatusAgendamento.FALTANTE
            )
            .map(agendamento -> agendamento.getDataHora().toLocalTime())
            .collect(Collectors.toList());
    }
} 