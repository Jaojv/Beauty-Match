package br.com.beautymatch.beautymatch.repository;

// Importações necessárias
import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Cliente;
import br.com.beautymatch.beautymatch.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    List<Agendamento> findByProfissionalAndData(Profissional profissional, LocalDate data);
    
    // Método para encontrar todos os agendamentos de um cliente
    List<Agendamento> findByCliente(Cliente cliente);
    
    // Método para encontrar todos os agendamentos de um cliente em uma data específica
    List<Agendamento> findByClienteAndData(Cliente cliente, LocalDate data);
}
