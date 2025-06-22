package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.SalaoDTO;
import com.beauty.com.MatchBeauty.dto.UsuarioDTO;
import com.beauty.com.MatchBeauty.dto.ServicoDTO;
import com.beauty.com.MatchBeauty.dto.ProfissionalDTO;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.HorarioFuncionamentoSalao;
import com.beauty.com.MatchBeauty.service.SalaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/saloes")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class SalaoController {

    @Autowired
    private SalaoService salaoService;

    @GetMapping
    public ResponseEntity<List<SalaoDTO.Response>> listarSaloes() {
        List<Salao> saloes = salaoService.listarSaloes();
        List<SalaoDTO.Response> response = saloes.stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaoDTO.Response> buscarSalao(@PathVariable Long id) {
        try {
            Salao salao = salaoService.buscarSalao(id);
            return ResponseEntity.ok(converterParaDTO(salao));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SalaoDTO.Response> criarSalao(@Valid @RequestBody SalaoDTO.Request request) {
        try {
            Salao salao = converterParaEntidade(request);
            Salao salaoCriado = salaoService.criarSalao(salao, request.getProprietarioId());
            return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(salaoCriado));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaoDTO.Response> atualizarSalao(
            @PathVariable Long id,
            @Valid @RequestBody SalaoDTO.Request request) {
        try {
            Salao salao = converterParaEntidade(request);
            Salao salaoAtualizado = salaoService.atualizarSalao(id, salao);
            return ResponseEntity.ok(converterParaDTO(salaoAtualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSalao(@PathVariable Long id) {
        boolean deletado = salaoService.deletarSalao(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/proprietario")
    public ResponseEntity<List<SalaoDTO.Response>> buscarSaloesPorProprietario(@RequestParam Long proprietarioId) {
        try {
            if (proprietarioId == null || proprietarioId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            List<Salao> saloes = salaoService.buscarSaloesPorProprietario(proprietarioId);
            if (saloes.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(saloes.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<SalaoDTO.Response> buscarSalaoPorNomeEEndereco(
            @RequestParam String nome,
            @RequestParam String endereco) {
        return salaoService.buscarSalaoPorNomeEEndereco(nome, endereco)
            .map(salao -> ResponseEntity.ok(converterParaDTO(salao)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<ServicoDTO.Response>> listarServicosSalao(@PathVariable Long id) {
        try {
            List<Servico> servicos = salaoService.listarServicos(id);
            List<ServicoDTO.Response> servicosDTO = servicos.stream()
                .map(this::converterServicoParaDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(servicosDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/profissionais")
    public ResponseEntity<List<ProfissionalDTO>> listarProfissionaisSalao(@PathVariable Long id) {
        try {
            List<Profissional> profissionais = salaoService.listarProfissionais(id);
            List<ProfissionalDTO> profissionaisDTO = profissionais.stream()
                .map(this::converterProfissionalParaDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(profissionaisDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ServicoDTO.Response converterServicoParaDTO(Servico servico) {
        ServicoDTO.Response dto = new ServicoDTO.Response();
        dto.setId(servico.getId());
        dto.setNome(servico.getNome());
        dto.setDescricao(servico.getDescricao());
        dto.setDuracaoMinutos(servico.getDuracaoMinutos());
        dto.setPreco(servico.getPreco());
        return dto;
    }

    private ProfissionalDTO converterProfissionalParaDTO(Profissional profissional) {
        ProfissionalDTO dto = new ProfissionalDTO();
        dto.setNome(profissional.getNome());
        dto.setEspecialidade(profissional.getEspecialidade());
        dto.setBiografia(profissional.getBiografia());
        dto.setHorarioTrabalho(profissional.getHorarioTrabalho());
        dto.setDiasTrabalho(profissional.getDiasTrabalho());
        return dto;
    }

    private SalaoDTO.Response converterParaDTO(Salao salao) {
        SalaoDTO.Response response = new SalaoDTO.Response();
        response.setId(salao.getId());
        response.setNome(salao.getNome());
        response.setEndereco(salao.getEndereco());
        response.setTelefone(salao.getTelefone());
        response.setDescricao(salao.getDescricao());
        
        // Converter horários de funcionamento para string
        if (salao.getHorariosFuncionamento() != null && !salao.getHorariosFuncionamento().isEmpty()) {
            String horariosStr = salao.getHorariosFuncionamento().stream()
                .map(h -> h.getDiaSemana() + ": " + h.getHoraInicio() + " - " + h.getHoraFim())
                .collect(Collectors.joining("; "));
            response.setHorarioFuncionamento(horariosStr);
        }
        
        UsuarioDTO.Response proprietarioResponse = new UsuarioDTO.Response(
            salao.getProprietario().getIdUsuario(),
            salao.getProprietario().getUsername(),
            salao.getProprietario().getNome(),
            salao.getProprietario().getEmail(),
            salao.getProprietario().getTipoUsuario()
        );
        proprietarioResponse.setTelefone(salao.getProprietario().getTelefone());
        response.setProprietario(proprietarioResponse);
        
        return response;
    }

    private Salao converterParaEntidade(SalaoDTO.Request dto) {
        Salao salao = new Salao();
        salao.setNome(dto.getNome());
        salao.setEndereco(dto.getEndereco());
        salao.setTelefone(dto.getTelefone());
        salao.setEmail(dto.getEmail());
        salao.setDescricao(dto.getDescricao());
        // Horários de funcionamento serão configurados pelo serviço
        salao.setServicos(new ArrayList<>());
        salao.setAgendamentos(new ArrayList<>());
        return salao;
    }
} 