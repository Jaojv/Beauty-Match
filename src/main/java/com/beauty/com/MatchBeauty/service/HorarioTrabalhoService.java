package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.HorarioTrabalho;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.exception.AgendamentoException;
import com.beauty.com.MatchBeauty.repository.AgendamentoRepository;
import com.beauty.com.MatchBeauty.repository.HorarioTrabalhoRepository;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class HorarioTrabalhoService {

    @Autowired
    private HorarioTrabalhoRepository horarioTrabalhoRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private HorarioFuncionamentoSalaoService horarioFuncionamentoService;
    
    @Autowired
    private ProfissionalRepository profissionalRepository;

    public List<HorarioTrabalho> buscarHorariosTrabalhoProfissional(Long profissionalId) {
        return horarioTrabalhoRepository.findByProfissionalIdUsuarioAndAtivoTrue(profissionalId);
    }

    public List<HorarioTrabalho> buscarHorariosTrabalhoProfissionalPorDia(Long profissionalId, DayOfWeek diaSemana) {
        return horarioTrabalhoRepository.findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(profissionalId, diaSemana);
    }

    public HorarioTrabalho criarHorarioTrabalho(HorarioTrabalho horarioTrabalho) {
        return horarioTrabalhoRepository.save(horarioTrabalho);
    }

    public HorarioTrabalho atualizarHorarioTrabalho(HorarioTrabalho horarioTrabalho) {
        if (horarioTrabalhoRepository.existsById(horarioTrabalho.getId())) {
            return horarioTrabalhoRepository.save(horarioTrabalho);
        }
        return null;
    }

    public boolean deletarHorarioTrabalho(Long id) {
        if (horarioTrabalhoRepository.existsById(id)) {
            horarioTrabalhoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Verifica se um profissional está disponível em um determinado horário
     * Agora usa os horários de funcionamento do salão
     * @param profissional Profissional a ser verificado
     * @param dataHora Data e hora do agendamento
     * @return true se o profissional estiver disponível, false caso contrário
     */
    public boolean verificarDisponibilidadeHorarioTrabalho(Usuario profissional, LocalDateTime dataHora) {
        if (!(profissional instanceof Profissional)) {
            // Lança uma exceção ou retorna falso se o usuário não for um profissional
            throw new IllegalArgumentException("O usuário fornecido não é um profissional.");
        }
        Profissional prof = (Profissional) profissional;

        if (prof.getSalao() == null) {
            return false; // Profissional não está associado a nenhum salão
        }
        
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime hora = dataHora.toLocalTime();
        
        // Verifica se o horário está dentro do horário de funcionamento do salão
        return horarioFuncionamentoService.isHorarioFuncionamento(
            prof.getSalao().getId(), 
            diaSemana, 
            hora
        );
    }

    /**
     * Verifica se um horário está bloqueado para um profissional
     * @param profissional Profissional a ser verificado
     * @param dataHora Data e hora do agendamento
     * @return true se o horário estiver bloqueado, false caso contrário
     */
    public boolean verificarHorarioBloqueado(Usuario profissional, LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime hora = dataHora.toLocalTime();
        
        List<HorarioTrabalho> horarios = horarioTrabalhoRepository
            .findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(profissional.getIdUsuario(), diaSemana);
        
        // Se não houver horários cadastrados para o dia, o horário está bloqueado
        if (horarios.isEmpty()) {
            return true;
        }
        
        // Verifica se o horário está bloqueado em algum dos períodos
        return horarios.stream()
            .anyMatch(horario -> 
                horario.isBloqueado() &&
                !hora.isBefore(horario.getHoraInicio()) && 
                !hora.isAfter(horario.getHoraFim())
            );
    }

    /**
     * Verifica se existe conflito com agendamentos existentes
     * @param profissional Profissional a ser verificado
     * @param dataHora Data e hora do bloqueio
     * @return true se existe conflito, false caso contrário
     */
    private boolean existeConflitoAgendamentos(Usuario profissional, LocalDateTime dataHora) {
        // Buscar todos os agendamentos do profissional no mesmo dia
        LocalDateTime inicioDia = dataHora.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime fimDia = inicioDia.plusDays(1);
        
        List<Agendamento> agendamentosDoDia = agendamentoRepository
            .findByProfissionalIdUsuarioAndDataHoraBetween(
                profissional.getIdUsuario(),
                inicioDia,
                fimDia
            );
        
        // Verificar conflitos
        return agendamentosDoDia.stream()
            .filter(agendamento -> 
                agendamento.getStatus() != Agendamento.StatusAgendamento.CANCELADO &&
                agendamento.getStatus() != Agendamento.StatusAgendamento.FALTANTE
            )
            .anyMatch(agendamento -> {
                LocalDateTime fimAgendamento = agendamento.getDataHora()
                    .plusMinutes(agendamento.getServico().getDuracaoMinutos());
                
                return temSobreposicaoHorario(
                    dataHora,
                    dataHora.plusHours(1), // Bloqueio padrão de 1 hora
                    agendamento.getDataHora(),
                    fimAgendamento
                );
            });
    }

    /**
     * Verifica se há sobreposição de horários
     */
    private boolean temSobreposicaoHorario(
        LocalDateTime inicio1,
        LocalDateTime fim1,
        LocalDateTime inicio2,
        LocalDateTime fim2
    ) {
        return !inicio1.isAfter(fim2) && !inicio2.isAfter(fim1);
    }

    /**
     * Bloqueia um horário para um profissional
     * @param profissional Profissional que terá o horário bloqueado
     * @param dataHora Data e hora do bloqueio
     * @return HorarioTrabalho atualizado
     * @throws AgendamentoException se houver conflito com agendamentos existentes
     */
    public HorarioTrabalho bloquearHorario(Usuario profissional, LocalDateTime dataHora) {
        // Verificar se existe conflito com agendamentos existentes
        if (existeConflitoAgendamentos(profissional, dataHora)) {
            throw new AgendamentoException("Existe conflito com agendamentos existentes neste horário");
        }

        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime hora = dataHora.toLocalTime();
        
        List<HorarioTrabalho> horarios = horarioTrabalhoRepository
            .findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(profissional.getIdUsuario(), diaSemana);
        
        // Se não houver horários cadastrados para o dia, cria um novo
        if (horarios.isEmpty()) {
            HorarioTrabalho novoHorario = new HorarioTrabalho(
                profissional,
                diaSemana,
                hora,
                hora.plusHours(1)
            );
            novoHorario.setBloqueado(true);
            novoHorario.setAtivo(true);
            return horarioTrabalhoRepository.save(novoHorario);
        }
        
        // Atualiza o horário existente
        HorarioTrabalho horario = horarios.stream()
            .filter(h -> !hora.isBefore(h.getHoraInicio()) && !hora.isAfter(h.getHoraFim()))
            .findFirst()
            .orElse(null);
            
        if (horario != null) {
            horario.setBloqueado(true);
            return horarioTrabalhoRepository.save(horario);
        }
        
        return null;
    }

    /**
     * Desbloqueia um horário para um profissional
     * @param profissional Profissional que terá o horário desbloqueado
     * @param dataHora Data e hora do desbloqueio
     * @return HorarioTrabalho atualizado
     */
    public HorarioTrabalho desbloquearHorario(Usuario profissional, LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        LocalTime hora = dataHora.toLocalTime();
        
        List<HorarioTrabalho> horarios = horarioTrabalhoRepository
            .findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(profissional.getIdUsuario(), diaSemana);
        
        // Se não houver horários cadastrados para o dia, retorna null
        if (horarios.isEmpty()) {
            return null;
        }
        
        // Atualiza o horário existente
        HorarioTrabalho horario = horarios.stream()
            .filter(h -> !hora.isBefore(h.getHoraInicio()) && !hora.isAfter(h.getHoraFim()))
            .findFirst()
            .orElse(null);
            
        if (horario != null) {
            horario.setBloqueado(false);
            return horarioTrabalhoRepository.save(horario);
        }
        
        return null;
    }
} 