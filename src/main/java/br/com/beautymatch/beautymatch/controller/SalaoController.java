package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.service.SalaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saloes")
@CrossOrigin(origins = "*")
public class SalaoController {

    @Autowired
    private SalaoService salaoService;

    @PostMapping
    public ResponseEntity<Salao> criar(@Valid @RequestBody Salao salao) {
        return ResponseEntity.ok(salaoService.salvar(salao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Salao> atualizar(@PathVariable Long id, @Valid @RequestBody Salao salao) {
        salao.setId(id);
        return ResponseEntity.ok(salaoService.salvar(salao));
    }

    @GetMapping
    public ResponseEntity<List<Salao>> listarTodos() {
        return ResponseEntity.ok(salaoService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salao> buscarPorId(@PathVariable Long id) {
        return salaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Salao> buscarPorCnpj(@PathVariable String cnpj) {
        return salaoService.buscarPorCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Salao>> buscarAtivos() {
        return ResponseEntity.ok(salaoService.buscarPorAtivo(true));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Salao>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(salaoService.buscarPorNome(nome));
    }

    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<Salao>> buscarPorCidade(@PathVariable String cidade) {
        return ResponseEntity.ok(salaoService.buscarPorCidade(cidade));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Salao>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(salaoService.buscarPorEstado(estado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        salaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Salao> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(salaoService.atualizarStatus(id, ativo));
    }

    @PatchMapping("/{id}/horario-funcionamento")
    public ResponseEntity<Salao> atualizarHorarioFuncionamento(@PathVariable Long id,
                                                             @RequestParam String horarioAbertura,
                                                             @RequestParam String horarioFechamento) {
        return ResponseEntity.ok(salaoService.atualizarHorarioFuncionamento(id, horarioAbertura, horarioFechamento));
    }

    @PatchMapping("/{id}/endereco")
    public ResponseEntity<Salao> atualizarEndereco(@PathVariable Long id,
                                                 @RequestParam String logradouro,
                                                 @RequestParam String numero,
                                                 @RequestParam(required = false) String complemento,
                                                 @RequestParam String bairro,
                                                 @RequestParam String cidade,
                                                 @RequestParam String estado,
                                                 @RequestParam String cep) {
        return ResponseEntity.ok(salaoService.atualizarEndereco(id, logradouro, numero, complemento,
                bairro, cidade, estado, cep));
    }
} 