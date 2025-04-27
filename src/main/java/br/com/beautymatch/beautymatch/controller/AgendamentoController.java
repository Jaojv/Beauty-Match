package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<Agendamento> criar(@Valid @RequestBody Agendamento agendamento) {
        return ResponseEntity.ok(agendamentoService.salvar(agendamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @Valid @RequestBody Agendamento agendamento) {
        agendamento.setId(id);
        return ResponseEntity.ok(agendamentoService.salvar(agendamento));
    }

    @GetMapping
    public ResponseEntity<List<Agendamento>> listarTodos() {
        return ResponseEntity.ok(agendamentoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return agendamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Agendamento>> buscarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(agendamentoService.buscarPorCliente(clienteId));
    }

    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<Agendamento>> buscarPorProfissional(@PathVariable Long profissionalId) {
        return ResponseEntity.ok(agendamentoService.buscarPorProfissional(profissionalId));
    }

    @GetMapping("/servico/{servicoId}")
    public ResponseEntity<List<Agendamento>> buscarPorServico(@PathVariable Long servicoId) {
        return ResponseEntity.ok(agendamentoService.buscarPorServico(servicoId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Agendamento>> buscarPorStatus(@PathVariable Agendamento.StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.buscarPorStatus(status));
    }

    @GetMapping("/cliente/{clienteId}/status/{status}")
    public ResponseEntity<List<Agendamento>> buscarPorClienteEStatus(
            @PathVariable Long clienteId,
            @PathVariable Agendamento.StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.buscarPorClienteEStatus(clienteId, status));
    }

    @GetMapping("/profissional/{profissionalId}/status/{status}")
    public ResponseEntity<List<Agendamento>> buscarPorProfissionalEStatus(
            @PathVariable Long profissionalId,
            @PathVariable Agendamento.StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.buscarPorProfissionalEStatus(profissionalId, status));
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Agendamento>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(agendamentoService.buscarPorDataHoraEntre(inicio, fim));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        agendamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Agendamento> atualizarStatus(
            @PathVariable Long id,
            @RequestParam Agendamento.StatusAgendamento status) {
        return ResponseEntity.ok(agendamentoService.atualizarStatus(id, status));
    }
} 