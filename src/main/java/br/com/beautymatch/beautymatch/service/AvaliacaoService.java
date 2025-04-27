package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Avaliacao;
import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.AgendamentoRepository;
import br.com.beautymatch.beautymatch.repository.AvaliacaoRepository;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    
    @Transactional
    public Avaliacao salvar(Avaliacao avaliacao) {
        // Verificar se o cliente existe
        Optional<Usuario> clienteOpt = usuarioRepository.findById(avaliacao.getCliente().getId());
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        // Verificar se o cliente está ativo
        Usuario cliente = clienteOpt.get();
        if (!cliente.isAtivo()) {
            throw new RuntimeException("O cliente não está ativo");
        }
        
        // Verificar se o serviço existe (se fornecido)
        if (avaliacao.getServico() != null) {
            Optional<Servico> servicoOpt = servicoRepository.findById(avaliacao.getServico().getId());
            if (servicoOpt.isEmpty()) {
                throw new RuntimeException("Serviço não encontrado");
            }
        }
        
        // Verificar se o profissional existe (se fornecido)
        if (avaliacao.getProfissional() != null) {
            Optional<Profissional> profissionalOpt = profissionalRepository.findById(avaliacao.getProfissional().getId());
            if (profissionalOpt.isEmpty()) {
                throw new RuntimeException("Profissional não encontrado");
            }
        }
        
        // Verificar se o agendamento existe (se fornecido)
        if (avaliacao.getAgendamento() != null) {
            Optional<Agendamento> agendamentoOpt = agendamentoRepository.findById(avaliacao.getAgendamento().getId());
            if (agendamentoOpt.isEmpty()) {
                throw new RuntimeException("Agendamento não encontrado");
            }
            
            // Verificar se o agendamento pertence ao cliente
            Agendamento agendamento = agendamentoOpt.get();
            if (!agendamento.getCliente().getId().equals(avaliacao.getCliente().getId())) {
                throw new RuntimeException("O agendamento não pertence ao cliente");
            }
            
            // Verificar se o agendamento está concluído
            if (agendamento.getStatus() != Agendamento.StatusAgendamento.CONCLUIDO) {
                throw new RuntimeException("Só é possível avaliar agendamentos concluídos");
            }
        }
        
        // Verificar se a nota está entre 1 e 5
        if (avaliacao.getNota() < 1 || avaliacao.getNota() > 5) {
            throw new RuntimeException("A nota deve estar entre 1 e 5");
        }
        
        return avaliacaoRepository.save(avaliacao);
    }
    
    public List<Avaliacao> buscarPorCliente(Long clienteId) {
        return avaliacaoRepository.findByClienteId(clienteId);
    }
    
    public List<Avaliacao> buscarPorServico(Long servicoId) {
        return avaliacaoRepository.findByServicoId(servicoId);
    }
    
    public List<Avaliacao> buscarPorProfissional(Long profissionalId) {
        return avaliacaoRepository.findByProfissionalId(profissionalId);
    }
    
    public List<Avaliacao> buscarPorAgendamento(Long agendamentoId) {
        return avaliacaoRepository.findByAgendamentoId(agendamentoId);
    }
    
    public Double getMediaAvaliacaoServico(Long servicoId) {
        return avaliacaoRepository.getMediaAvaliacaoServico(servicoId);
    }
    
    public Double getMediaAvaliacaoProfissional(Long profissionalId) {
        return avaliacaoRepository.getMediaAvaliacaoProfissional(profissionalId);
    }
    
    public Double getMediaAvaliacaoSalao(Long salaoId) {
        return avaliacaoRepository.getMediaAvaliacaoSalao(salaoId);
    }
    
    public Optional<Avaliacao> buscarPorId(Long id) {
        return avaliacaoRepository.findById(id);
    }
    
    public List<Avaliacao> buscarTodos() {
        return avaliacaoRepository.findAll();
    }
    
    @Transactional
    public void excluir(Long id) {
        avaliacaoRepository.deleteById(id);
    }
    
    @Transactional
    public Avaliacao responder(Long id, String resposta) {
        Optional<Avaliacao> avaliacaoOpt = avaliacaoRepository.findById(id);
        if (avaliacaoOpt.isEmpty()) {
            throw new RuntimeException("Avaliação não encontrada");
        }
        
        Avaliacao avaliacao = avaliacaoOpt.get();
        avaliacao.setResposta(resposta);
        
        return avaliacaoRepository.save(avaliacao);
    }
} 