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
    public ResponseEntity<ServicoDTO.Response> criarServico(@Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servicoSalvo = servicoService.criarServico(servicoDTO);
        return ResponseEntity.ok(new ServicoDTO.Response(servicoSalvo));
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços")
    public ResponseEntity<List<ServicoDTO.Response>> listarServicos() {
        List<Servico> servicos = servicoService.listarServicos();
        List<ServicoDTO.Response> servicosDTO = servicos.stream()
            .map(ServicoDTO.Response::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(servicosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar serviço por ID")
    public ResponseEntity<ServicoDTO.Response> buscarServicoPorId(@PathVariable Long id) {
        Servico servico = servicoService.buscarServico(id).orElse(null);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ServicoDTO.Response(servico));
    }

    @GetMapping("/salao/{salaoId}")
    @Operation(summary = "Buscar serviços por salão")
    public ResponseEntity<List<ServicoDTO.Response>> buscarServicosPorSalao(@PathVariable Long salaoId) {
        List<Servico> servicos = servicoService.buscarServicosPorSalao(salaoId);
        List<ServicoDTO.Response> servicosDTO = servicos.stream()
            .map(ServicoDTO.Response::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(servicosDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um serviço existente")
    public ResponseEntity<ServicoDTO.Response> atualizarServico(
            @PathVariable Long id,
            @Valid @RequestBody ServicoDTO servicoDTO) {
        Servico servico = servicoService.buscarServico(id).orElse(null);
        if (servico == null) {
            return ResponseEntity.notFound().build();
        }
        
        servico.setNome(servicoDTO.getNome());
        servico.setDescricao(servicoDTO.getDescricao());
        servico.setDuracaoMinutos(servicoDTO.getDuracaoMinutos());
        servico.setPreco(servicoDTO.getPreco());
        
        Servico servicoAtualizado = servicoService.atualizarServico(servico);
        
        return ResponseEntity.ok(new ServicoDTO.Response(servicoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um serviço")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
} 