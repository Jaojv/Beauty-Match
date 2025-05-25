package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ProprietarioDTO;
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
    public ResponseEntity<Proprietario> criarProprietario(@RequestBody ProprietarioDTO dto) {
        Proprietario proprietario = new Proprietario();
        proprietario.setUsername(dto.getUsername());
        proprietario.setPassword(dto.getPassword());
        proprietario.setEmail(dto.getEmail());
        proprietario.setTelefone(dto.getTelefone());
        proprietario.setNome(dto.getNome());
        proprietario.setCnpj(dto.getCnpj());
        proprietario.setRazaoSocial(dto.getRazaoSocial());
        proprietario.setEndereco(dto.getEndereco());
        proprietario.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        
        Proprietario novoProprietario = proprietarioService.criar(proprietario);
        return ResponseEntity.ok(novoProprietario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)")
    public ResponseEntity<Proprietario> atualizarProprietario(@PathVariable Long id, @RequestBody ProprietarioDTO dto) {
        Proprietario proprietario = proprietarioService.buscarPorId(id);
        if (proprietario == null) {
            return ResponseEntity.notFound().build();
        }

        proprietario.setUsername(dto.getUsername());
        proprietario.setPassword(dto.getPassword());
        proprietario.setEmail(dto.getEmail());
        proprietario.setTelefone(dto.getTelefone());
        proprietario.setNome(dto.getNome());
        proprietario.setCnpj(dto.getCnpj());
        proprietario.setRazaoSocial(dto.getRazaoSocial());
        proprietario.setEndereco(dto.getEndereco());
        proprietario.setHorarioFuncionamento(dto.getHorarioFuncionamento());

        Proprietario proprietarioAtualizado = proprietarioService.atualizar(id, proprietario);
        return ResponseEntity.ok(proprietarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)")
    public ResponseEntity<Void> deletarProprietario(@PathVariable Long id) {
        proprietarioService.deletar(id);
        return ResponseEntity.ok().build();
    }
} 