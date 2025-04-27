package br.com.beautymatch.beautymatch.repository;

// Importações necessárias
import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Agendamento.StatusAgendamento;
import br.com.beautymatch.beautymatch.model.Cliente;
import br.com.beautymatch.beautymatch.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// @Repository: Indica que esta interface é um componente de repositório do Spring
// JpaRepository: Interface que fornece métodos CRUD básicos e paginação
// Agendamento: A entidade que este repositório gerencia
// Long: O tipo do ID da entidade Agendamento
@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Esta interface herda automaticamente vários métodos úteis como:
    // save(), findById(), findAll(), delete(), etc.

    // Método para encontrar todos os agendamentos de um profissional
    List<Agendamento> findByProfissional(Profissional profissional);
    
    // Método para encontrar todos os agendamentos de um profissional em uma data específica
    @Query("SELECT a FROM Agendamento a WHERE a.profissional = :profissional AND DATE(a.dataHora) = :data")
    List<Agendamento> findByProfissionalAndData(@Param("profissional") Profissional profissional, @Param("data") LocalDate data);
    
    // Método para encontrar todos os agendamentos de um cliente
    List<Agendamento> findByCliente(Cliente cliente);
    
    // Método para encontrar todos os agendamentos de um cliente em uma data específica
    @Query("SELECT a FROM Agendamento a WHERE a.cliente = :cliente AND DATE(a.dataHora) = :data")
    List<Agendamento> findByClienteAndData(@Param("cliente") Cliente cliente, @Param("data") LocalDate data);

    List<Agendamento> findByClienteId(Long clienteId);
    
    List<Agendamento> findByProfissionalId(Long profissionalId);
    
    List<Agendamento> findByServicoId(Long servicoId);
    
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    List<Agendamento> findByClienteIdAndStatus(Long clienteId, StatusAgendamento status);
    
    List<Agendamento> findByProfissionalIdAndStatus(Long profissionalId, StatusAgendamento status);
    
    List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataHora BETWEEN :dataInicio AND :dataFim")
    List<Agendamento> findByProfissionalIdAndDataHoraBetween(
            @Param("profissionalId") Long profissionalId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND a.dataHora = :dataHora AND a.status != 'CANCELADO'")
    List<Agendamento> findConflitosAgendamento(
            @Param("profissionalId") Long profissionalId,
            @Param("dataHora") LocalDateTime dataHora);
    
    @Query("SELECT a FROM Agendamento a WHERE a.profissional.id = :profissionalId AND " +
           "((a.dataHora <= :dataHoraInicio AND a.dataHoraFim > :dataHoraInicio) OR " +
           "(a.dataHora < :dataHoraFim AND a.dataHoraFim >= :dataHoraFim) OR " +
           "(a.dataHora >= :dataHoraInicio AND a.dataHoraFim <= :dataHoraFim)) AND " +
           "a.status != 'CANCELADO'")
    List<Agendamento> findConflitosAgendamentoIntervalo(
            @Param("profissionalId") Long profissionalId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim);
}
