package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalaoService {

    @Autowired
    private SalaoRepository salaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Salao> listarSaloes() {
        return salaoRepository.findAll();
    }

    public Salao buscarSalao(Long id) {
        return salaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
    }

    @Transactional
    public Salao criarSalao(Salao salao, Long proprietarioId) {
        // Buscar o proprietário pelo ID
        Usuario proprietario = usuarioRepository.findById(proprietarioId)
            .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));
        salao.setProprietario(proprietario);

        validarSalao(salao);
        if (salaoRepository.existsByNomeAndEndereco(salao.getNome(), salao.getEndereco())) {
            throw new RuntimeException("Já existe um salão com este nome e endereço");
        }
        return salaoRepository.save(salao);
    }

    @Transactional
    public Salao atualizarSalao(Long id, Salao salaoAtualizado) {
        Salao salaoExistente = buscarSalao(id);
        validarSalao(salaoAtualizado);
        
        // Verifica se já existe outro salão com o mesmo nome e endereço
        if (!salaoExistente.getNome().equals(salaoAtualizado.getNome()) || 
            !salaoExistente.getEndereco().equals(salaoAtualizado.getEndereco())) {
            if (salaoRepository.existsByNomeAndEndereco(salaoAtualizado.getNome(), salaoAtualizado.getEndereco())) {
                throw new RuntimeException("Já existe um salão com este nome e endereço");
            }
        }

        // Atualiza os campos
        salaoExistente.setNome(salaoAtualizado.getNome());
        salaoExistente.setEndereco(salaoAtualizado.getEndereco());
        salaoExistente.setTelefone(salaoAtualizado.getTelefone());
        salaoExistente.setDescricao(salaoAtualizado.getDescricao());
        salaoExistente.setHorarioFuncionamento(salaoAtualizado.getHorarioFuncionamento());
        salaoExistente.setProprietario(salaoAtualizado.getProprietario());

        return salaoRepository.save(salaoExistente);
    }

    @Transactional
    public boolean deletarSalao(Long id) {
        if (salaoRepository.existsById(id)) {
            salaoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Salao> buscarSaloesPorProprietario(Long proprietarioId) {
        return salaoRepository.findByProprietarioIdUsuario(proprietarioId);
    }

    public Optional<Salao> buscarSalaoPorNomeEEndereco(String nome, String endereco) {
        return salaoRepository.findByNomeAndEndereco(nome, endereco);
    }

    private void validarSalao(Salao salao) {
        if (salao.getNome() == null || salao.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do salão é obrigatório");
        }
        if (salao.getEndereco() == null || salao.getEndereco().trim().isEmpty()) {
            throw new RuntimeException("Endereço do salão é obrigatório");
        }
        if (salao.getTelefone() == null || salao.getTelefone().trim().isEmpty()) {
            throw new RuntimeException("Telefone do salão é obrigatório");
        }
        if (salao.getHorarioFuncionamento() == null || salao.getHorarioFuncionamento().trim().isEmpty()) {
            throw new RuntimeException("Horário de funcionamento é obrigatório");
        }
        if (salao.getProprietario() == null) {
            throw new RuntimeException("Proprietário do salão é obrigatório");
        }
    }
} 