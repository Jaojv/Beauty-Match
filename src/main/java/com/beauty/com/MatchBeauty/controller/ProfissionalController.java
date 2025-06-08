package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ProfissionalDTO;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.ProfissionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

 
    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private SalaoRepository salaoRepository;



    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Profissional>> listarProfissionais() {
        return ResponseEntity.ok(profissionalService.listarProfissionais());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)")
    public ResponseEntity<Profissional> buscarProfissional(@PathVariable Long id) {
        Profissional profissional = profissionalService.buscarProfissional(id);
        if (profissional != null) {
            return ResponseEntity.ok(profissional);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Profissional> criarProfissional(@RequestBody ProfissionalDTO dto) {
        Profissional profissional = new Profissional();
        profissional.setUsername(dto.getUsername());
        profissional.setPassword(dto.getPassword());
        profissional.setEmail(dto.getEmail());
        profissional.setTelefone(dto.getTelefone());
        profissional.setNome(dto.getNome());
        profissional.setCpf(dto.getCpf());
        profissional.setEspecialidade(dto.getEspecialidade());
        profissional.setBiografia(dto.getBiografia());
        profissional.setHorarioTrabalho(dto.getHorarioTrabalho());
        profissional.setDiasTrabalho(dto.getDiasTrabalho());
        profissional.setTipoUsuario(com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario.PROFISSIONAL);

        // Buscar e associar o salão
        Salao salao = salaoRepository.findById(dto.getSalaoId())
            .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
        profissional.setSalao(salao);

        Profissional novoProfissional = profissionalService.criarProfissional(profissional);
        return ResponseEntity.ok(novoProfissional);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)")
    public ResponseEntity<Profissional> atualizarProfissional(@PathVariable Long id, @RequestBody ProfissionalDTO dto) {
        Profissional profissional = profissionalService.buscarProfissional(id);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }

        profissional.setUsername(dto.getUsername());
        profissional.setPassword(dto.getPassword());
        profissional.setEmail(dto.getEmail());
        profissional.setTelefone(dto.getTelefone());
        profissional.setNome(dto.getNome());
        profissional.setCpf(dto.getCpf());
        profissional.setEspecialidade(dto.getEspecialidade());
        profissional.setBiografia(dto.getBiografia());
        profissional.setHorarioTrabalho(dto.getHorarioTrabalho());
        profissional.setDiasTrabalho(dto.getDiasTrabalho());

        Profissional profissionalAtualizado = profissionalService.atualizarProfissional(profissional);
        return ResponseEntity.ok(profissionalAtualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarProfissional(@PathVariable Long id) {
        if (profissionalService.deletarProfissional(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 