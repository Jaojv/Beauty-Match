package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ProprietarioDTO;
import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proprietarios")
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    @GetMapping
    public ResponseEntity<List<ProprietarioDTO>> listarProprietarios() {
        List<Proprietario> proprietarios = proprietarioService.listarTodos();
        List<ProprietarioDTO> proprietariosDTO = proprietarios.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(proprietariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProprietarioDTO> buscarProprietario(@PathVariable Long id) {
        Proprietario proprietario = proprietarioService.buscarPorId(id);
        return ResponseEntity.ok(converterParaDTO(proprietario));
    }

    @PostMapping
    public ResponseEntity<ProprietarioDTO> criarProprietario(@RequestBody ProprietarioDTO proprietarioDTO) {
        Proprietario proprietario = proprietarioService.criar(converterParaEntidade(proprietarioDTO));
        return ResponseEntity.ok(converterParaDTO(proprietario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProprietarioDTO> atualizarProprietario(@PathVariable Long id, 
                                                               @RequestBody ProprietarioDTO proprietarioDTO) {
        Proprietario proprietario = proprietarioService.atualizar(id, converterParaEntidade(proprietarioDTO));
        return ResponseEntity.ok(converterParaDTO(proprietario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProprietario(@PathVariable Long id) {
        proprietarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private ProprietarioDTO converterParaDTO(Proprietario proprietario) {
        return new ProprietarioDTO(
            proprietario.getIdUsuario(),
            proprietario.getUsername(),
            proprietario.getNome(),
            proprietario.getEmail(),
            proprietario.getTelefone()
        );
    }

    private Proprietario converterParaEntidade(ProprietarioDTO dto) {
        Proprietario proprietario = new Proprietario();
        proprietario.setIdUsuario(dto.getIdUsuario());
        proprietario.setUsername(dto.getUsername());
        proprietario.setNome(dto.getNome());
        proprietario.setEmail(dto.getEmail());
        proprietario.setTelefone(dto.getTelefone());
        return proprietario;
    }
} 