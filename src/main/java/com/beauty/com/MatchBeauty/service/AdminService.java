package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SERVIÇO ADMIN - LÓGICA DE NEGÓCIO PARA ADMINISTRADORES
 * 
 * Este serviço gerencia todas as operações relacionadas aos administradores do sistema.
 * Fornece métodos para CRUD de administradores, incluindo criptografia de senhas
 * e validações de segurança.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os administradores
 * - Busca de administrador por ID
 * - Criação de novos administradores
 * - Atualização de dados de administradores
 * - Exclusão de administradores
 * - Criptografia automática de senhas
 * - Definição automática do tipo de usuário
 * 
 * DEPENDÊNCIAS:
 * - AdminRepository: Para operações de persistência
 * - PasswordEncoder: Para criptografia de senhas
 */
@Service
public class AdminService {

    /**
     * REPOSITÓRIO DE ADMINISTRADORES
     * Responsável por operações de persistência no banco de dados
     */
    @Autowired
    private AdminRepository adminRepository;

    /**
     * ENCRIPTADOR DE SENHAS
     * Utilizado para criptografar senhas antes de salvar no banco
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * LISTA TODOS OS ADMINISTRADORES
     * 
     * @return Lista com todos os administradores cadastrados no sistema
     */
    public List<Admin> listarAdmins() {
        return adminRepository.findAll();
    }

    /**
     * BUSCA ADMINISTRADOR POR ID
     * 
     * @param id ID único do administrador
     * @return Administrador encontrado ou null se não existir
     */
    public Admin buscarAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    /**
     * CRIA NOVO ADMINISTRADOR
     * 
     * Criptografa a senha e define automaticamente o tipo de usuário como ADMIN
     * 
     * @param admin Dados do administrador a ser criado
     * @return Administrador criado com senha criptografada
     */
    public Admin criarAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setTipoUsuario(TipoUsuario.ADMIN);
        return adminRepository.save(admin);
    }

    /**
     * ATUALIZA DADOS DE ADMINISTRADOR
     * 
     * Verifica se o administrador existe antes de atualizar.
     * Re-criptografa a senha e mantém o tipo de usuário como ADMIN.
     * 
     * @param admin Dados atualizados do administrador
     * @return Administrador atualizado ou null se não existir
     */
    public Admin atualizarAdmin(Admin admin) {
        if (adminRepository.existsById(admin.getIdUsuario())) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            return adminRepository.save(admin);
        }
        return null;
    }

    /**
     * EXCLUI ADMINISTRADOR
     * 
     * Verifica se o administrador existe antes de excluir.
     * 
     * @param id ID do administrador a ser excluído
     * @return true se excluído com sucesso, false se não existir
     */
    public boolean deletarAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 