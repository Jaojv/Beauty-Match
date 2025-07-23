package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.repository.ServicoRepository;
import com.beauty.com.MatchBeauty.dto.ServicoDTO;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SERVIÇO SERVIÇO - LÓGICA DE NEGÓCIO PARA SERVIÇOS
 * 
 * Este serviço gerencia todas as operações relacionadas aos serviços do sistema.
 * Fornece métodos para CRUD de serviços, consultas por salão, preços
 * e integração com profissionais e agendamentos.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os serviços
 * - Busca de serviço por ID
 * - Criação de novos serviços
 * - Atualização de dados de serviços
 * - Exclusão de serviços
 * - Consulta por salão
 * - Filtragem por tipo de serviço
 * - Busca por faixa de preço
 * - Validações específicas para serviços
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Gerenciamento de informações do serviço
 * - Associação com salão
 * - Preços e duração
 * - Descrições detalhadas
 * - Categorias de serviços
 * - Disponibilidade de profissionais
 * - Integração com agendamentos
 * 
 * VALIDAÇÕES:
 * - Verificação de dados obrigatórios
 * - Validação de preços válidos
 * - Verificação de salão existente
 * - Validação de duração
 * 
 * DEPENDÊNCIAS:
 * - ServicoRepository: Para operações de persistência
 * - SalaoRepository: Para consulta de salões
 */
@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private SalaoRepository salaoRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;


    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    public Optional<Servico> buscarServico(Long id) {
        return servicoRepository.findById(id);
    }

    public Servico criarServico(ServicoDTO dto) {
        Servico servico = new Servico();
        servico.setNome(dto.getNome());
        servico.setDescricao(dto.getDescricao());
        servico.setPreco(dto.getPreco());
        servico.setDuracaoMinutos(dto.getDuracaoMinutos());

        // Buscar e setar o salão
        Salao salao = salaoRepository.findById(dto.getSalaoId())
            .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
        servico.setSalao(salao);

        return servicoRepository.save(servico);
    }

    public Servico atualizarServico(Servico servico) {
        if (servicoRepository.existsById(servico.getId())) {
            return servicoRepository.save(servico);
        }
        return null;
    }

    public boolean deletarServico(Long id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Servico> buscarServicosPorSalao(Long salaoId) {
        return servicoRepository.findBySalaoId(salaoId);
    }

    public List<Servico> buscarServicosPorProfissional(Long profissionalId) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
            
        if (profissional.getSalao() == null) {
            throw new RuntimeException("Profissional não está vinculado a um salão");
        }
        
        return servicoRepository.findBySalaoId(profissional.getSalao().getId());
    }

    public Servico buscarServicoPorNomeESalao(String nome, Long salaoId) {
        return servicoRepository.findByNomeAndSalaoId(nome, salaoId).orElse(null);
    }

    public boolean existeServicoComNomeESalao(String nome, Long salaoId) {
        return servicoRepository.existsByNomeAndSalaoId(nome, salaoId);
    }
} 