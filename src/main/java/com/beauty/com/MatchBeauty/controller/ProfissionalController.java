package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ProfissionalDTO;
import com.beauty.com.MatchBeauty.dto.ServicoDTO;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Controller responsável por gerenciar operações relacionadas aos profissionais
// Fornece endpoints para CRUD de profissionais e consultas relacionadas
@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    // Serviço para operações de profissional
    @Autowired
    private ProfissionalService profissionalService;

    // Serviço de segurança para validação de permissões
    @Autowired
    private SecurityService securityService;

    // Repositório para operações de salão
    @Autowired
    private SalaoRepository salaoRepository;

    // Endpoint para listar todos os profissionais
    // Apenas administradores podem listar todos os profissionais
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfissionalDTO.Response>> listarProfissionais() {
        List<Profissional> profissionais = profissionalService.listarProfissionais();
        List<ProfissionalDTO.Response> response = profissionais.stream()
            .map(ProfissionalDTO.Response::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Endpoint para buscar um profissional específico por ID
    // Administradores podem buscar qualquer profissional, profissionais só podem buscar seus próprios dados
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)")
    public ResponseEntity<ProfissionalDTO.Response> buscarProfissional(@PathVariable Long id) {
        Profissional profissional = profissionalService.buscarProfissional(id);
        if (profissional != null) {
            return ResponseEntity.ok(new ProfissionalDTO.Response(profissional));
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para criar um novo profissional
    // Apenas administradores podem criar profissionais
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfissionalDTO.Response> criarProfissional(@RequestBody ProfissionalDTO.Request dto) {
        // Cria nova entidade profissional com dados do DTO
        Profissional profissional = new Profissional();
        profissional.setUsername(dto.getUsername());
        profissional.setPassword(dto.getPassword());
        profissional.setEmail(dto.getEmail());
        profissional.setTelefone(dto.getTelefone());
        profissional.setNome(dto.getNome());
        profissional.setCpf(dto.getCpf());
        profissional.setEspecialidade(dto.getEspecialidade());
        profissional.setBiografia(dto.getBiografia());
        profissional.setTipoUsuario(com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario.PROFISSIONAL);

        // Buscar e associar o salão ao profissional
        Salao salao = salaoRepository.findById(dto.getSalaoId())
            .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
        profissional.setSalao(salao);

        Profissional novoProfissional = profissionalService.criarProfissional(profissional);
        return ResponseEntity.ok(new ProfissionalDTO.Response(novoProfissional));
    }

    // Endpoint para atualizar dados de um profissional
    // Administradores podem atualizar qualquer profissional, profissionais só podem atualizar seus próprios dados
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)")
    public ResponseEntity<ProfissionalDTO.Response> atualizarProfissional(@PathVariable Long id, @RequestBody ProfissionalDTO.Request dto) {
        Profissional profissional = profissionalService.buscarProfissional(id);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza os campos do profissional
        profissional.setUsername(dto.getUsername());
        profissional.setPassword(dto.getPassword());
        profissional.setEmail(dto.getEmail());
        profissional.setTelefone(dto.getTelefone());
        profissional.setNome(dto.getNome());
        profissional.setCpf(dto.getCpf());
        profissional.setEspecialidade(dto.getEspecialidade());
        profissional.setBiografia(dto.getBiografia());

        Profissional profissionalAtualizado = profissionalService.atualizarProfissional(profissional);
        return ResponseEntity.ok(new ProfissionalDTO.Response(profissionalAtualizado));
    }

    // Endpoint para deletar um profissional
    // Apenas administradores podem deletar profissionais
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarProfissional(@PathVariable Long id) {
        if (profissionalService.deletarProfissional(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para listar serviços de um profissional
    // Retorna todos os serviços oferecidos por um profissional específico
    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<ServicoDTO.Response>> listarServicosProfissional(@PathVariable Long id) {
        try {
            List<Servico> servicos = profissionalService.listarServicos(id);
            List<ServicoDTO.Response> servicosDTO = servicos.stream()
                .map(this::converterServicoParaDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(servicosDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para verificar disponibilidade de um profissional
    // Retorna horários disponíveis de um profissional em uma data específica
    @GetMapping("/{id}/disponibilidade")
    public ResponseEntity<Map<String, List<String>>> verificarDisponibilidade(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            Map<String, List<String>> disponibilidade = profissionalService.verificarHorariosDisponiveis(id, data);
            return ResponseEntity.ok(disponibilidade);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para buscar profissionais por salão
    // Retorna todos os profissionais que trabalham em um salão específico
    @GetMapping("/salao/{salaoId}")
    public ResponseEntity<List<ProfissionalDTO.Response>> buscarProfissionaisPorSalao(@PathVariable Long salaoId) {
        try {
            List<Profissional> profissionais = profissionalService.buscarProfissionaisPorSalao(salaoId);
            List<ProfissionalDTO.Response> response = profissionais.stream()
                .map(ProfissionalDTO.Response::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Método auxiliar para converter Servico para ServicoDTO.Response
    private ServicoDTO.Response converterServicoParaDTO(Servico servico) {
        return new ServicoDTO.Response(servico);
    }
} 