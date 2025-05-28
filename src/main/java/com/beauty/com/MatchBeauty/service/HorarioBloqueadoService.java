package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.HorarioBloqueado;
import com.beauty.com.MatchBeauty.repository.HorarioBloqueadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HorarioBloqueadoService {

    @Autowired
    private HorarioBloqueadoRepository horarioBloqueadoRepository;

    public List<HorarioBloqueado> listarHorariosBloqueados() {
        return horarioBloqueadoRepository.findAll();
    }

    public HorarioBloqueado buscarHorarioBloqueado(Long id) {
        return horarioBloqueadoRepository.findById(id).orElse(null);
    }

    public HorarioBloqueado criarHorarioBloqueado(HorarioBloqueado horarioBloqueado) {
        return horarioBloqueadoRepository.save(horarioBloqueado);
    }

    public HorarioBloqueado atualizarHorarioBloqueado(HorarioBloqueado horarioBloqueado) {
        if (horarioBloqueadoRepository.existsById(horarioBloqueado.getId())) {
            return horarioBloqueadoRepository.save(horarioBloqueado);
        }
        return null;
    }

    public boolean deletarHorarioBloqueado(Long id) {
        if (horarioBloqueadoRepository.existsById(id)) {
            horarioBloqueadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<HorarioBloqueado> buscarHorariosBloqueadosPorSalao(Long salaoId) {
        return horarioBloqueadoRepository.findBySalaoId(salaoId);
    }

    public List<HorarioBloqueado> buscarHorariosBloqueadosPorSalaoEPeriodo(
        Long salaoId, 
        LocalDateTime inicio, 
        LocalDateTime fim
    ) {
        return horarioBloqueadoRepository.findBySalaoIdAndDataHoraInicioBetween(salaoId, inicio, fim);
    }
} 