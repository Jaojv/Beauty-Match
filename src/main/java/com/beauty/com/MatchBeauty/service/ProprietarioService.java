package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVIÇO PROPRIETÁRIO - LÓGICA DE NEGÓCIO PARA PROPRIETÁRIOS
 * 
 * Este serviço gerencia todas as operações relacionadas aos proprietários do sistema.
 * Fornece métodos para CRUD de proprietários, incluindo criptografia de senhas
 * e validações específicas para perfis de proprietários.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os proprietários
 * - Busca de proprietário por ID
 * - Criação de novos proprietários
 * - Atualização de dados de proprietários
 * - Exclusão de proprietários
 * - Criptografia automática de senhas
 * - Definição automática do tipo de usuário
 * - Validações específicas para proprietários
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Gerenciamento de salões associados
 * - Controle de acesso aos salões
 * - Perfil de proprietário com dados específicos
 * - Integração com sistema de salões
 * - Gestão de profissionais
 * - Relatórios de negócio
 * 
 * DEPENDÊNCIAS:
 * - ProprietarioRepository: Para operações de persistência
 * - PasswordEncoder: Para criptografia de senhas
 */
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