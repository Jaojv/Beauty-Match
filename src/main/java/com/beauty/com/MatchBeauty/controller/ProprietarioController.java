package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proprietarios")
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private SecurityService securityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Proprietario>> listarProprietarios() {
        return ResponseEntity.ok(proprietarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)")
    public ResponseEntity<Proprietario> buscarProprietario(@PathVariable Long id) {
        Proprietario proprietario = proprietarioService.buscarPorId(id);
        if (proprietario != null) {
            return ResponseEntity.ok(proprietario);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Proprietario> criarProprietario(@RequestBody Proprietario proprietario) {
        Proprietario novoProprietario = proprietarioService.criar(proprietario);
        return ResponseEntity.ok(novoProprietario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)")
    public ResponseEntity<Proprietario> atualizarProprietario(@PathVariable Long id, @RequestBody Proprietario proprietario) {
        proprietario.setIdUsuario(id);
        Proprietario proprietarioAtualizado = proprietarioService.atualizar(id, proprietario);
        if (proprietarioAtualizado != null) {
            return ResponseEntity.ok(proprietarioAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)")
    public ResponseEntity<Void> deletarProprietario(@PathVariable Long id) {
        proprietarioService.deletar(id);
        return ResponseEntity.ok().build();
    }
} 