package br.com.beautymatch.beautymatch.controller;

import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        usuario.setId(id);
        return ResponseEntity.ok(usuarioService.salvar(usuario));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Usuario> buscarPorCpf(@PathVariable String cpf) {
        return usuarioService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Usuario>> buscarPorTipo(@PathVariable Usuario.TipoUsuario tipo) {
        return ResponseEntity.ok(usuarioService.buscarPorTipo(tipo));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Usuario>> buscarAtivos() {
        return ResponseEntity.ok(usuarioService.buscarPorAtivo(true));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Usuario>> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(usuarioService.buscarPorNome(nome));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Usuario> atualizarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        return ResponseEntity.ok(usuarioService.atualizarStatus(id, ativo));
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Usuario> atualizarSenha(@PathVariable Long id, 
                                                @RequestParam String senhaAtual,
                                                @RequestParam String novaSenha) {
        return ResponseEntity.ok(usuarioService.atualizarSenha(id, senhaAtual, novaSenha));
    }

    @PatchMapping("/{id}/tipo")
    public ResponseEntity<Usuario> atualizarTipo(@PathVariable Long id, 
                                               @RequestParam Usuario.TipoUsuario tipo) {
        return ResponseEntity.ok(usuarioService.atualizarTipo(id, tipo));
    }
} 