package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.entity.Profissional;
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
    public ResponseEntity<Profissional> criarProfissional(@RequestBody Profissional profissional) {
        Profissional novoProfissional = profissionalService.criarProfissional(profissional);
        return ResponseEntity.ok(novoProfissional);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)")
    public ResponseEntity<Profissional> atualizarProfissional(@PathVariable Long id, @RequestBody Profissional profissional) {
        profissional.setIdUsuario(id);
        Profissional profissionalAtualizado = profissionalService.atualizarProfissional(profissional);
        if (profissionalAtualizado != null) {
            return ResponseEntity.ok(profissionalAtualizado);
        }
        return ResponseEntity.notFound().build();
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