package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.ClienteDTO;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private SecurityService securityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        List<ClienteDTO> dtos = clientes.stream().map(cliente -> {
            ClienteDTO dto = new ClienteDTO();
            dto.setClienteId(cliente.getIdUsuario());
            dto.setNome(cliente.getNome());
            dto.setEmail(cliente.getEmail());
            dto.setTelefone(cliente.getTelefone());
            dto.setCpf(cliente.getCpf());
            dto.setDataNascimento(cliente.getDataNascimento());
            dto.setEndereco(cliente.getEndereco());
            dto.setPreferencias(cliente.getPreferencias());
            dto.setUsername(cliente.getUsername());
            // Não retornar senha
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<ClienteDTO> buscarCliente(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.buscarCliente(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody ClienteDTO dto) {
        ClienteDTO novoCliente = clienteService.criarCliente(dto);
        return ResponseEntity.ok(novoCliente);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        ClienteDTO clienteAtualizado = clienteService.atualizarCliente(id, dto);
        if (clienteAtualizado != null) {
            return ResponseEntity.ok(clienteAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (clienteService.deletarCliente(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 