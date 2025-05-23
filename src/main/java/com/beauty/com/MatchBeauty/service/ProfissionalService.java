package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
} 