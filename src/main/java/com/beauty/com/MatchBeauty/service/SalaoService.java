package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaoService {

    @Autowired
    private SalaoRepository salaoRepository;

    public List<Salao> buscarSaloesPorProprietario(Long proprietarioId) {
        return salaoRepository.findByProprietarioIdUsuario(proprietarioId);
    }
} 