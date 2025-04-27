package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.CategoriaServico;
import br.com.beautymatch.beautymatch.service.CategoriaServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaServicoController {

    @Autowired
    private CategoriaServicoService categoriaServicoService;

    @PostMapping
    public ResponseEntity<CategoriaServico> criar(@Valid @RequestBody CategoriaServico categoriaServico) {
        return ResponseEntity.ok(categoriaServicoService.salvar(categoriaServico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaServico> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaServico categoriaServico) {
        categoriaServico.setId(id);
        return ResponseEntity.ok(categoriaServicoService.salvar(categoriaServico));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaServico>> listarTodos() {
        return ResponseEntity.ok(categoriaServicoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaServico> buscarPorId(@PathVariable Long id) {
        return categoriaServicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<CategoriaServico>> buscarAtivos() {
        return ResponseEntity.ok(categoriaServicoService.buscarPorAtivo(true));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<CategoriaServico>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(categoriaServicoService.buscarPorNome(nome));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaServicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoriaServico> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(categoriaServicoService.atualizarStatus(id, ativo));
    }
} 