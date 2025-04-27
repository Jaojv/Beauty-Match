package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Agendamento.StatusAgendamento;
import br.com.beautymatch.beautymatch.model.Cliente;
import br.com.beautymatch.beautymatch.model.HorarioBloqueado;
import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.AgendamentoRepository;
import br.com.beautymatch.beautymatch.repository.ClienteRepository;
import br.com.beautymatch.beautymatch.repository.HorarioBloqueadoRepository;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private HorarioBloqueadoRepository horarioBloqueadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Transactional
    public Agendamento salvar(Agendamento agendamento) {
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.AGENDADO);
        }
        return agendamentoRepository.save(agendamento);
    }
    
    public List<Agendamento> buscarPorCliente(Long clienteId) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        Cliente cliente = clienteOpt.get();
        if (!cliente.getUsuario().isAtivo()) {
            throw new RuntimeException("Cliente inativo");
        }
        
        return agendamentoRepository.findByCliente(cliente);
    }
    
    public List<Agendamento> buscarPorProfissional(Long profissionalId) {
        return agendamentoRepository.findByProfissionalId(profissionalId);
    }
    
    public List<Agendamento> buscarPorServico(Long servicoId) {
        return agendamentoRepository.findByServicoId(servicoId);
    }
    
    public List<Agendamento> buscarPorStatus(Agendamento.StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status);
    }
    
    public List<Agendamento> buscarPorClienteEStatus(Long clienteId, Agendamento.StatusAgendamento status) {
        return agendamentoRepository.findByClienteIdAndStatus(clienteId, status);
    }
    
    public List<Agendamento> buscarPorProfissionalEStatus(Long profissionalId, Agendamento.StatusAgendamento status) {
        return agendamentoRepository.findByProfissionalIdAndStatus(profissionalId, status);
    }
    
    public List<Agendamento> buscarPorDataHoraEntre(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }
    
    public List<Agendamento> buscarTodos() {
        return agendamentoRepository.findAll();
    }
    
    public Optional<Agendamento> buscarPorId(Long id) {
        return agendamentoRepository.findById(id);
    }
    
    @Transactional
    public void excluir(Long id) {
        Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(id);
        if (agendamentoOpt.isEmpty()) {
            throw new RuntimeException("Agendamento não encontrado");
        }
        
        Agendamento agendamento = agendamentoOpt.get();
        
        // Só permite excluir agendamentos futuros
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Não é possível excluir agendamentos passados");
        }
        
        agendamentoRepository.deleteById(id);
    }
    
    @Transactional
    public Agendamento atualizarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RuntimeException("Não é possível alterar o status de um agendamento cancelado");
        }
        
        agendamento.setStatus(novoStatus);
        return agendamentoRepository.save(agendamento);
    }

    public Agendamento cancelar(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        
        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new RuntimeException("Agendamento já está cancelado");
        }
        
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return agendamentoRepository.save(agendamento);
    }
} 