package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.repository.ServicoRepository;
import com.beauty.com.MatchBeauty.dto.ServicoDTO;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Servico buscarServico(Long id) {
        return servicoRepository.findById(id).orElse(null);
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

        // Buscar e setar os profissionais (se houver)
        if (dto.getProfissionaisIds() != null && !dto.getProfissionaisIds().isEmpty()) {
            List<Profissional> profissionais = profissionalRepository.findAllById(dto.getProfissionaisIds());
            // Corrigir: Servico espera List<Usuario>, então faz cast
            servico.setProfissionais(new java.util.ArrayList<>(profissionais));
        }

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
        return servicoRepository.findByProfissionaisIdUsuario(profissionalId);
    }

    public Servico buscarServicoPorNomeESalao(String nome, Long salaoId) {
        return servicoRepository.findByNomeAndSalaoId(nome, salaoId).orElse(null);
    }

    public boolean existeServicoComNomeESalao(String nome, Long salaoId) {
        return servicoRepository.existsByNomeAndSalaoId(nome, salaoId);
    }
} 