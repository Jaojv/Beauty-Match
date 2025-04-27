package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Avaliacao;
import br.com.beautymatch.beautymatch.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@Valid @RequestBody Avaliacao avaliacao) {
        return ResponseEntity.ok(avaliacaoService.salvar(avaliacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id, @Valid @RequestBody Avaliacao avaliacao) {
        avaliacao.setId(id);
        return ResponseEntity.ok(avaliacaoService.salvar(avaliacao));
    }

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listarTodos() {
        return ResponseEntity.ok(avaliacaoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Avaliacao>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(avaliacaoService.buscarPorCliente(clienteId));
    }

    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<Avaliacao>> buscarPorServico(@PathVariable Long servicoId) {
        return ResponseEntity.ok(avaliacaoService.buscarPorServico(servicoId));
    }

    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<Avaliacao>> buscarPorProfissional(@PathVariable Long profissionalId) {
        return ResponseEntity.ok(avaliacaoService.buscarPorProfissional(profissionalId));
    }

    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<List<Avaliacao>> buscarPorAgendamento(@PathVariable Long agendamentoId) {
        return ResponseEntity.ok(avaliacaoService.buscarPorAgendamento(agendamentoId));
    }

    @GetMapping("/servico/{servicoId}/media")
    public ResponseEntity<Double> getMediaAvaliacaoServico(@PathVariable Long servicoId) {
        return ResponseEntity.ok(avaliacaoService.getMediaAvaliacaoServico(servicoId));
    }

    @GetMapping("/profissional/{profissionalId}/media")
    public ResponseEntity<Double> getMediaAvaliacaoProfissional(@PathVariable Long profissionalId) {
        return ResponseEntity.ok(avaliacaoService.getMediaAvaliacaoProfissional(profissionalId));
    }

    @GetMapping("/salao/{salaoId}/media")
    public ResponseEntity<Double> getMediaAvaliacaoSalao(@PathVariable Long salaoId) {
        return ResponseEntity.ok(avaliacaoService.getMediaAvaliacaoSalao(salaoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/resposta")
    public ResponseEntity<Avaliacao> responder(@PathVariable Long id, @RequestParam String resposta) {
        return ResponseEntity.ok(avaliacaoService.responder(id, resposta));
    }
} 