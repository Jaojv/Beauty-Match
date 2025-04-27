package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalaoService {

    @Autowired
    private SalaoRepository salaoRepository;
    
    @Transactional
    public Salao salvar(Salao salao) {
        // Verificar se já existe um salão com o mesmo CNPJ
        Optional<Salao> salaoExistente = salaoRepository.findByCnpj(salao.getCnpj());
        if (salaoExistente.isPresent() && !salaoExistente.get().getId().equals(salao.getId())) {
            throw new RuntimeException("Já existe um salão cadastrado com este CNPJ");
        }
        
        return salaoRepository.save(salao);
    }
    
    public List<Salao> buscarTodos() {
        return salaoRepository.findAll();
    }
    
    public List<Salao> buscarPorAtivo(boolean ativo) {
        return salaoRepository.findByAtivo(ativo);
    }
    
    public List<Salao> buscarPorNome(String nome) {
        return salaoRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Salao> buscarPorCidade(String cidade) {
        return salaoRepository.findByCidadeContainingIgnoreCase(cidade);
    }
    
    public List<Salao> buscarPorEstado(String estado) {
        return salaoRepository.findByEstadoContainingIgnoreCase(estado);
    }
    
    public Optional<Salao> buscarPorId(Long id) {
        return salaoRepository.findById(id);
    }
    
    public Optional<Salao> buscarPorCnpj(String cnpj) {
        return salaoRepository.findByCnpj(cnpj);
    }
    
    @Transactional
    public void excluir(Long id) {
        salaoRepository.deleteById(id);
    }
    
    @Transactional
    public Salao atualizarStatus(Long id, boolean ativo) {
        Optional<Salao> salaoOpt = salaoRepository.findById(id);
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        Salao salao = salaoOpt.get();
        salao.setAtivo(ativo);
        
        return salaoRepository.save(salao);
    }
    
    @Transactional
    public Salao atualizarHorarioFuncionamento(Long id, String horarioAbertura, String horarioFechamento) {
        Optional<Salao> salaoOpt = salaoRepository.findById(id);
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        Salao salao = salaoOpt.get();
        salao.setHorarioAbertura(horarioAbertura);
        salao.setHorarioFechamento(horarioFechamento);
        
        return salaoRepository.save(salao);
    }
    
    @Transactional
    public Salao atualizarEndereco(Long id, String logradouro, String numero, String complemento, 
                                 String bairro, String cidade, String estado, String cep) {
        Optional<Salao> salaoOpt = salaoRepository.findById(id);
        if (salaoOpt.isEmpty()) {
            throw new RuntimeException("Salão não encontrado");
        }
        
        Salao salao = salaoOpt.get();
        salao.setLogradouro(logradouro);
        salao.setNumero(numero);
        salao.setComplemento(complemento);
        salao.setBairro(bairro);
        salao.setCidade(cidade);
        salao.setEstado(estado);
        salao.setCep(cep);
        
        return salaoRepository.save(salao);
    }
} 