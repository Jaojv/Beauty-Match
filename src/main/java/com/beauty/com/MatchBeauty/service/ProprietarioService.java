package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Proprietario> listarTodos() {
        return proprietarioRepository.findAll();
    }

    public Proprietario buscarPorId(Long id) {
        return proprietarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proprietário não encontrado"));
    }

    public Proprietario criar(Proprietario proprietario) {
        proprietario.setCriadoEm(LocalDateTime.now());
        proprietario.setAtualizadoEm(LocalDateTime.now());
        proprietario.setPassword(passwordEncoder.encode(proprietario.getPassword()));
        return proprietarioRepository.save(proprietario);
    }

    public Proprietario atualizar(Long id, Proprietario proprietarioAtualizado) {
        Proprietario proprietario = buscarPorId(id);
        proprietario.setUsername(proprietarioAtualizado.getUsername());
        proprietario.setNome(proprietarioAtualizado.getNome());
        proprietario.setEmail(proprietarioAtualizado.getEmail());
        proprietario.setTelefone(proprietarioAtualizado.getTelefone());
        proprietario.setAtualizadoEm(LocalDateTime.now());
        return proprietarioRepository.save(proprietario);
    }

    public void deletar(Long id) {
        Proprietario proprietario = buscarPorId(id);
        proprietarioRepository.delete(proprietario);
    }
} 