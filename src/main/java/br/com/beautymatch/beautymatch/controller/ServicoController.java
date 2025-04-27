package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@CrossOrigin(origins = "*")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<Servico> criar(@Valid @RequestBody Servico servico) {
        return ResponseEntity.ok(servicoService.salvar(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @Valid @RequestBody Servico servico) {
        servico.setId(id);
        return ResponseEntity.ok(servicoService.salvar(servico));
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarTodos() {
        return ResponseEntity.ok(servicoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        return servicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/salao/{salaoId}")
    public ResponseEntity<List<Servico>> buscarPorSalao(@PathVariable Long salaoId) {
        return ResponseEntity.ok(servicoService.buscarPorSalao(salaoId));
    }

    @GetMapping("/salao/{salaoId}/ativos")
    public ResponseEntity<List<Servico>> buscarPorSalaoEAtivo(@PathVariable Long salaoId) {
        return ResponseEntity.ok(servicoService.buscarPorSalaoEAtivo(salaoId, true));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Servico>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(servicoService.buscarPorCategoria(categoriaId));
    }

    @GetMapping("/salao/{salaoId}/nome/{nome}")
    public ResponseEntity<List<Servico>> buscarPorSalaoENome(@PathVariable Long salaoId, @PathVariable String nome) {
        return ResponseEntity.ok(servicoService.buscarPorSalaoENome(salaoId, nome));
    }

    @GetMapping("/salao/{salaoId}/preco")
    public ResponseEntity<List<Servico>> buscarPorSalaoEPrecoEntre(
            @PathVariable Long salaoId,
            @RequestParam BigDecimal precoMin,
            @RequestParam BigDecimal precoMax) {
        return ResponseEntity.ok(servicoService.buscarPorSalaoEPrecoEntre(salaoId, precoMin, precoMax));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Servico> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(servicoService.atualizarStatus(id, ativo));
    }

    @PatchMapping("/{id}/preco")
    public ResponseEntity<Servico> atualizarPreco(@PathVariable Long id, @RequestParam BigDecimal novoPreco) {
        return ResponseEntity.ok(servicoService.atualizarPreco(id, novoPreco));
    }

    @PatchMapping("/{id}/duracao")
    public ResponseEntity<Servico> atualizarDuracao(@PathVariable Long id, @RequestParam int novaDuracaoMinutos) {
        return ResponseEntity.ok(servicoService.atualizarDuracao(id, novaDuracaoMinutos));
    }
} 