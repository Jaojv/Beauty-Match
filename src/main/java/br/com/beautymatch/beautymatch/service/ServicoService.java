package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.CategoriaServico;
import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.repository.CategoriaServicoRepository;
import br.com.beautymatch.beautymatch.repository.SalaoRepository;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;
    
    @Autowired
    private SalaoRepository salaoRepository;
    
    @Autowired
    private CategoriaServicoRepository categoriaServicoRepository;
    
    @Transactional
    public Servico salvar(Servico servico) {
        // Verificar se o salão existe
        Optional<Salao> salaoOpt = salaoRepository.findById(servico.getSalao().getId());
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        // Verificar se o salão está ativo
        Salao salao = salaoOpt.get();
        if (!salao.isAtivo()) {
            throw new RuntimeException("O salão não está ativo");
        }
        
        // Se tiver categoria, verificar se existe e está ativa
        if (servico.getCategoria() != null) {
            Optional<CategoriaServico> categoriaOpt = categoriaServicoRepository.findById(servico.getCategoria().getId());
            if (categoriaOpt.isEmpty()) {
                throw new RuntimeException("Categoria não encontrada");
            }
            
            CategoriaServico categoria = categoriaOpt.get();
            if (!categoria.isAtivo()) {
                throw new RuntimeException("A categoria não está ativa");
            }
        }
        
        // Verificar se o preço é válido
        if (servico.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O preço deve ser maior que zero");
        }
        
        // Verificar se a duração é válida
        if (servico.getDuracaoMinutos() <= 0) {
            throw new RuntimeException("A duração deve ser maior que zero");
        }
        
        return servicoRepository.save(servico);
    }
    
    public List<Servico> buscarTodos() {
        return servicoRepository.findAll();
    }
    
    public List<Servico> buscarPorSalao(Long salaoId) {
        return servicoRepository.findBySalaoId(salaoId);
    }
    
    public List<Servico> buscarPorSalaoEAtivo(Long salaoId, boolean ativo) {
        return servicoRepository.findBySalaoIdAndAtivo(salaoId, ativo);
    }
    
    public List<Servico> buscarPorCategoria(Long categoriaId) {
        return servicoRepository.findByCategoriaId(categoriaId);
    }
    
    public List<Servico> buscarPorSalaoENome(Long salaoId, String nome) {
        return servicoRepository.findBySalaoIdAndNomeContainingIgnoreCase(salaoId, nome);
    }
    
    public List<Servico> buscarPorSalaoEPrecoEntre(Long salaoId, BigDecimal precoMin, BigDecimal precoMax) {
        return servicoRepository.findBySalaoIdAndPrecoBetween(salaoId, precoMin, precoMax);
    }
    
    public Optional<Servico> buscarPorId(Long id) {
        return servicoRepository.findById(id);
    }
    
    @Transactional
    public void excluir(Long id) {
        servicoRepository.deleteById(id);
    }
    
    @Transactional
    public Servico atualizarStatus(Long id, boolean ativo) {
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        if (servicoOpt.isEmpty()) {
            throw new RuntimeException("Serviço não encontrado");
        }
        
        Servico servico = servicoOpt.get();
        servico.setAtivo(ativo);
        
        return servicoRepository.save(servico);
    }
    
    @Transactional
    public Servico atualizarPreco(Long id, BigDecimal novoPreco) {
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        if (servicoOpt.isEmpty()) {
            throw new RuntimeException("Serviço não encontrado");
        }
        
        if (novoPreco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O preço deve ser maior que zero");
        }
        
        Servico servico = servicoOpt.get();
        servico.setPreco(novoPreco);
        
        return servicoRepository.save(servico);
    }
    
    @Transactional
    public Servico atualizarDuracao(Long id, int novaDuracaoMinutos) {
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        if (servicoOpt.isEmpty()) {
            throw new RuntimeException("Serviço não encontrado");
        }
        
        if (novaDuracaoMinutos <= 0) {
            throw new RuntimeException("A duração deve ser maior que zero");
        }
        
        Servico servico = servicoOpt.get();
        servico.setDuracaoMinutos(novaDuracaoMinutos);
        
        return servicoRepository.save(servico);
    }
} 