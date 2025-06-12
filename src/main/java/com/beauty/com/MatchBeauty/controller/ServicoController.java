package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ServicoDTO;
import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicos")
@Tag(name = "Serviços", description = "API para gerenciamento de serviços")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    @Operation(summary = "Criar um novo serviço")
    public ResponseEntity<ServicoDTO> criarServico(@Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servicoSalvo = servicoService.criarServico(servicoDTO);
        ServicoDTO response = new ServicoDTO();
        response.setNome(servicoSalvo.getNome());
        response.setDescricao(servicoSalvo.getDescricao());
        response.setDuracaoMinutos(servicoSalvo.getDuracaoMinutos());
        response.setPreco(servicoSalvo.getPreco());
        response.setSalaoId(servicoSalvo.getSalao() != null ? servicoSalvo.getSalao().getId() : null);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<Servico> servicos = servicoService.listarServicos();
        List<ServicoDTO> servicosDTO = servicos.stream()
            .map(servico -> {
                ServicoDTO dto = new ServicoDTO();
                dto.setNome(servico.getNome());
                dto.setDescricao(servico.getDescricao());
                dto.setDuracaoMinutos(servico.getDuracaoMinutos());
                dto.setPreco(servico.getPreco());
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(servicosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID")
    public ResponseEntity<ServicoDTO> buscarServicoPorId(@PathVariable Long id) {
        Servico servico = servicoService.buscarServico(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        ServicoDTO dto = new ServicoDTO();
        dto.setNome(servico.getNome());
        dto.setDescricao(servico.getDescricao());
        dto.setDuracaoMinutos(servico.getDuracaoMinutos());
        dto.setPreco(servico.getPreco());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/salao/{salaoId}")
    @Operation(summary = "Buscar serviços por salão")
    public ResponseEntity<List<ServicoDTO>> buscarServicosPorSalao(@PathVariable Long salaoId) {
        List<Servico> servicos = servicoService.buscarServicosPorSalao(salaoId);
        List<ServicoDTO> servicosDTO = servicos.stream()
            .map(servico -> {
                ServicoDTO dto = new ServicoDTO();
                dto.setNome(servico.getNome());
                dto.setDescricao(servico.getDescricao());
                dto.setDuracaoMinutos(servico.getDuracaoMinutos());
                dto.setPreco(servico.getPreco());
                return dto;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(servicosDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um serviço existente")
    public ResponseEntity<ServicoDTO> atualizarServico(
            @PathVariable Long id,
            @Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.buscarServico(id);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        
        servico.setNome(servicoDTO.getNome());
        servico.setDescricao(servicoDTO.getDescricao());
        servico.setDuracaoMinutos(servicoDTO.getDuracaoMinutos());
        servico.setPreco(servicoDTO.getPreco());
        
        Servico servicoAtualizado = servicoService.atualizarServico(servico);
        
        ServicoDTO response = new ServicoDTO();
        response.setNome(servicoAtualizado.getNome());
        response.setDescricao(servicoAtualizado.getDescricao());
        response.setDuracaoMinutos(servicoAtualizado.getDuracaoMinutos());
        response.setPreco(servicoAtualizado.getPreco());
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um serviço")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
} 