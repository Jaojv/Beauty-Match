package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.CategoriaServico;
import br.com.beautymatch.beautymatch.repository.CategoriaServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServicoService {

    @Autowired
    private CategoriaServicoRepository categoriaServicoRepository;
    
    @Transactional
    public CategoriaServico salvar(CategoriaServico categoriaServico) {
        // Verificar se já existe uma categoria com o mesmo nome
        Optional<CategoriaServico> categoriaExistente = categoriaServicoRepository.findByNome(categoriaServico.getNome());
        if (categoriaExistente.isPresent() && !categoriaExistente.get().getId().equals(categoriaServico.getId())) {
            throw new RuntimeException("Já existe uma categoria com este nome");
        }
        
        return categoriaServicoRepository.save(categoriaServico);
    }
    
    public List<CategoriaServico> buscarTodos() {
        return categoriaServicoRepository.findAll();
    }
    
    public List<CategoriaServico> buscarPorAtivo(boolean ativo) {
        return categoriaServicoRepository.findByAtivo(ativo);
    }
    
    public List<CategoriaServico> buscarPorNome(String nome) {
        return categoriaServicoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Optional<CategoriaServico> buscarPorId(Long id) {
        return categoriaServicoRepository.findById(id);
    }
    
    @Transactional
    public void excluir(Long id) {
        categoriaServicoRepository.deleteById(id);
    }
    
    @Transactional
    public CategoriaServico atualizarStatus(Long id, boolean ativo) {
        Optional<CategoriaServico> categoriaOpt = categoriaServicoRepository.findById(id);
        if (categoriaOpt.isEmpty()) {
            throw new RuntimeException("Categoria não encontrada");
        }
        
        CategoriaServico categoria = categoriaOpt.get();
        categoria.setAtivo(ativo);
        
        return categoriaServicoRepository.save(categoria);
    }
} 