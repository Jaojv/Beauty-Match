package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.HorarioBloqueado;
import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.repository.HorarioBloqueadoRepository;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioBloqueadoService {

    @Autowired
    private HorarioBloqueadoRepository horarioBloqueadoRepository;
    
    @Autowired
    private ProfissionalRepository profissionalRepository;
    
    @Transactional
    public HorarioBloqueado salvar(HorarioBloqueado horarioBloqueado) {
        // Verificar se o profissional existe
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(horarioBloqueado.getProfissional().getId());
        if (profissionalOpt.isEmpty()) {
            throw new RuntimeException("Profissional não encontrado");
        }
        
        // Verificar se já existe um horário bloqueado para o mesmo período
        List<HorarioBloqueado> conflitos = horarioBloqueadoRepository.findConflitosHorario(
                horarioBloqueado.getProfissional().getId(),
                horarioBloqueado.getDataBloqueio(),
                horarioBloqueado.getHoraInicio(),
                horarioBloqueado.getHoraFim());
        
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Já existe um horário bloqueado para este período");
        }
        
        return horarioBloqueadoRepository.save(horarioBloqueado);
    }
    
    public List<HorarioBloqueado> buscarPorProfissional(Long profissionalId) {
        return horarioBloqueadoRepository.findByProfissionalId(profissionalId);
    }
    
    public List<HorarioBloqueado> buscarPorProfissionalEData(Long profissionalId, LocalDate data) {
        return horarioBloqueadoRepository.findByProfissionalIdAndDataBloqueio(profissionalId, data);
    }
    
    @Transactional
    public void excluir(Long id) {
        horarioBloqueadoRepository.deleteById(id);
    }
    
    public Optional<HorarioBloqueado> buscarPorId(Long id) {
        return horarioBloqueadoRepository.findById(id);
    }
    
    @Transactional
    public HorarioBloqueado atualizar(Long id, HorarioBloqueado horarioBloqueado) {
        Optional<HorarioBloqueado> existenteOpt = horarioBloqueadoRepository.findById(id);
        if (existenteOpt.isEmpty()) {
            throw new RuntimeException("Horário bloqueado não encontrado");
        }
        
        HorarioBloqueado existente = existenteOpt.get();
        
        // Verificar se já existe um horário bloqueado para o mesmo período (exceto o atual)
        List<HorarioBloqueado> conflitos = horarioBloqueadoRepository.findConflitosHorario(
                horarioBloqueado.getProfissional().getId(),
                horarioBloqueado.getDataBloqueio(),
                horarioBloqueado.getHoraInicio(),
                horarioBloqueado.getHoraFim());
        
        if (!conflitos.isEmpty() && conflitos.stream().anyMatch(h -> !h.getId().equals(id))) {
            throw new RuntimeException("Já existe um horário bloqueado para este período");
        }
        
        existente.setDataBloqueio(horarioBloqueado.getDataBloqueio());
        existente.setHoraInicio(horarioBloqueado.getHoraInicio());
        existente.setHoraFim(horarioBloqueado.getHoraFim());
        existente.setMotivo(horarioBloqueado.getMotivo());
        
        return horarioBloqueadoRepository.save(existente);
    }
} 