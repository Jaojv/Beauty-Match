package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.service.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<Profissional> criar(@Valid @RequestBody Profissional profissional) {
        return ResponseEntity.ok(profissionalService.salvar(profissional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profissional> atualizar(@PathVariable Long id, @Valid @RequestBody Profissional profissional) {
        profissional.setId(id);
        return ResponseEntity.ok(profissionalService.salvar(profissional));
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listarTodos() {
        return ResponseEntity.ok(profissionalService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/salao/{salaoId}")
    public ResponseEntity<List<Profissional>> buscarPorSalao(@PathVariable Long salaoId) {
        return ResponseEntity.ok(profissionalService.buscarPorSalao(salaoId));
    }

    @GetMapping("/salao/{salaoId}/ativos")
    public ResponseEntity<List<Profissional>> buscarPorSalaoEAtivo(@PathVariable Long salaoId) {
        return ResponseEntity.ok(profissionalService.buscarPorSalaoEAtivo(salaoId, true));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Profissional>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(profissionalService.buscarPorNome(nome));
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<Profissional>> buscarPorEspecialidade(@PathVariable String especialidade) {
        return ResponseEntity.ok(profissionalService.buscarPorEspecialidade(especialidade));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Profissional> buscarPorUsuario(@PathVariable Long usuarioId) {
        return profissionalService.buscarPorUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        profissionalService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Profissional> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(profissionalService.atualizarStatus(id, ativo));
    }

    @PatchMapping("/{id}/salao")
    public ResponseEntity<Profissional> atualizarSalao(@PathVariable Long id, @RequestParam Long salaoId) {
        return ResponseEntity.ok(profissionalService.atualizarSalao(id, salaoId));
    }
} 