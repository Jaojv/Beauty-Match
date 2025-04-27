package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import br.com.beautymatch.beautymatch.repository.SalaoRepository;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Autowired
    private SalaoRepository salaoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public Profissional salvar(Profissional profissional) {
        // Verificar se o salão existe
        Optional<Salao> salaoOpt = salaoRepository.findById(profissional.getSalao().getId());
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        // Verificar se o salão está ativo
        Salao salao = salaoOpt.get();
        if (!salao.isAtivo()) {
            throw new RuntimeException("O salão não está ativo");
        }
        
        // Se tiver usuário associado, verificar se existe
        if (profissional.getUsuario() != null) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(profissional.getUsuario().getId());
            if (usuarioOpt.isEmpty()) {
                throw new RuntimeException("Usuário não encontrado");
            }
            
            // Verificar se o usuário é do tipo PROFISSIONAL
            Usuario usuario = usuarioOpt.get();
            if (usuario.getTipo() != Usuario.TipoUsuario.PROFISSIONAL) {
                throw new RuntimeException("O usuário não é um profissional");
            }
        }
        
        return profissionalRepository.save(profissional);
    }
    
    public List<Profissional> buscarTodos() {
        return profissionalRepository.findAll();
    }
    
    public List<Profissional> buscarPorSalao(Long salaoId) {
        return profissionalRepository.findBySalaoId(salaoId);
    }
    
    public List<Profissional> buscarPorSalaoEAtivo(Long salaoId, boolean ativo) {
        return profissionalRepository.findBySalaoIdAndAtivo(salaoId, ativo);
    }
    
    public List<Profissional> buscarPorEspecialidade(Long salaoId, String especialidade) {
        return profissionalRepository.findBySalaoIdAndEspecialidade(salaoId, especialidade);
    }
    
    public List<Profissional> buscarPorNome(String nome) {
        return profissionalRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Profissional> buscarPorEspecialidade(String especialidade) {
        return profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }
    
    public Optional<Profissional> buscarPorId(Long id) {
        return profissionalRepository.findById(id);
    }
    
    public Optional<Profissional> buscarPorUsuarioId(Long usuarioId) {
        return profissionalRepository.findByUsuarioId(usuarioId);
    }
    
    @Transactional
    public void excluir(Long id) {
        profissionalRepository.deleteById(id);
    }
    
    @Transactional
    public Profissional atualizarStatus(Long id, boolean ativo) {
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(id);
        if (profissionalOpt.isEmpty()) {
            throw new RuntimeException("Profissional não encontrado");
        }
        
        Profissional profissional = profissionalOpt.get();
        profissional.setAtivo(ativo);
        
        return profissionalRepository.save(profissional);
    }
    
    @Transactional
    public Profissional atualizarSalao(Long profissionalId, Long novoSalaoId) {
        // Verificar se o profissional existe
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        if (profissionalOpt.isEmpty()) {
            throw new RuntimeException("Profissional não encontrado");
        }
        
        // Verificar se o novo salão existe
        Optional<Salao> salaoOpt = salaoRepository.findById(novoSalaoId);
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        // Verificar se o novo salão está ativo
        Salao novoSalao = salaoOpt.get();
        if (!novoSalao.isAtivo()) {
            throw new RuntimeException("O salão não está ativo");
        }
        
        // Atualizar o salão do profissional
        Profissional profissional = profissionalOpt.get();
        profissional.setSalao(novoSalao);
        
        return profissionalRepository.save(profissional);
    }
} 