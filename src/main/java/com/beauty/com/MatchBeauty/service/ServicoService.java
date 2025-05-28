package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    @Autowired
    private ServicoRepository servicoRepository;

    public List<Servico> listarServicos() {
        return servicoRepository.findAll();
    }

    public Servico buscarServico(Long id) {
        return servicoRepository.findById(id).orElse(null);
    }

    public Servico criarServico(Servico servico) {
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