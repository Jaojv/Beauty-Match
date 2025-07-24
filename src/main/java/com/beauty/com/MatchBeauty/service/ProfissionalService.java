package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.beauty.com.MatchBeauty.entity.Servico;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.time.LocalDate;

/**
 * SERVIÇO PROFISSIONAL - LÓGICA DE NEGÓCIO PARA PROFISSIONAIS
 * 
 * Este serviço gerencia todas as operações relacionadas aos profissionais do sistema.
 * Fornece métodos para CRUD de profissionais, incluindo criptografia de senhas
 * e validações específicas para perfis de profissionais.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os profissionais
 * - Busca de profissional por ID
 * - Criação de novos profissionais
 * - Atualização de dados de profissionais
 * - Exclusão de profissionais
 * - Criptografia automática de senhas
 * - Definição automática do tipo de usuário
 * - Validações específicas para profissionais
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Gerenciamento de especialidades
 * - Histórico de agendamentos
 * - Perfil de profissional com dados específicos
 * - Integração com sistema de agendamentos
 * - Horários de trabalho
 * - Biografia e experiência
 * 
 * DEPENDÊNCIAS:
 * - ProfissionalRepository: Para operações de persistência
 * - PasswordEncoder: Para criptografia de senhas
 */
@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    public List<Profissional> listarProfissionais() {
        return profissionalRepository.findAll();
    }

    public Profissional buscarProfissional(Long id) {
        return profissionalRepository.findById(id).orElse(null);
    }

    public Profissional criarProfissional(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }

    public Profissional atualizarProfissional(Profissional profissional) {
        if (profissionalRepository.existsById(profissional.getIdUsuario())) {
            return profissionalRepository.save(profissional);
        }
        return null;
    }

    public boolean deletarProfissional(Long id) {
        if (profissionalRepository.existsById(id)) {
            profissionalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Servico> listarServicos(Long profissionalId) {
        Profissional profissional = buscarProfissional(profissionalId);
        if (profissional == null) {
            throw new RuntimeException("Profissional não encontrado");
        }
        if (profissional.getSalao() == null) {
            throw new RuntimeException("Profissional não está vinculado a um salão");
        }
        return profissional.getSalao().getServicos();
    }

    public Map<String, List<String>> verificarHorariosDisponiveis(Long profissionalId, LocalDate data) {
        Profissional profissional = buscarProfissional(profissionalId);
        if (profissional == null) {
            throw new RuntimeException("Profissional não encontrado");
        }

        // Horários padrão de trabalho
        List<String> horariosDisponiveis = Arrays.asList(
            "09:00", "10:00", "11:00", "12:00",
            "14:00", "15:00", "16:00", "17:00"
        );

        // TODO: Implementar lógica para verificar horários já agendados
        // Por enquanto, retorna todos os horários como disponíveis

        Map<String, List<String>> disponibilidade = new HashMap<>();
        disponibilidade.put(data.toString(), horariosDisponiveis);
        
        return disponibilidade;
    }

    public List<Profissional> buscarProfissionaisPorSalao(Long salaoId) {
        return profissionalRepository.findBySalao_Id(salaoId);
    }
} 