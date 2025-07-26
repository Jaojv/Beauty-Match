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

// Controller responsável por gerenciar operações relacionadas aos clientes
// Fornece endpoints para CRUD de clientes com controle de acesso baseado em roles
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    // Serviço para operações de cliente
    @Autowired
    private ClienteService clienteService;

    // Serviço de segurança para validação de permissões
    @Autowired
    private SecurityService securityService;

    // Endpoint para listar todos os clientes
    // Apenas administradores podem listar todos os clientes
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
            // Não retornar senha por segurança
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    // Endpoint para buscar um cliente específico por ID
    // Administradores podem buscar qualquer cliente, clientes só podem buscar seus próprios dados
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<ClienteDTO> buscarCliente(@PathVariable Long id) {
        ClienteDTO cliente = clienteService.buscarCliente(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para criar um novo cliente
    // Apenas administradores podem criar clientes
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody ClienteDTO dto) {
        ClienteDTO novoCliente = clienteService.criarCliente(dto);
        return ResponseEntity.ok(novoCliente);
    }

    // Endpoint para atualizar dados de um cliente
    // Administradores podem atualizar qualquer cliente, clientes só podem atualizar seus próprios dados
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        ClienteDTO clienteAtualizado = clienteService.atualizarCliente(id, dto);
        if (clienteAtualizado != null) {
            return ResponseEntity.ok(clienteAtualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para deletar um cliente
    // Administradores podem deletar qualquer cliente, clientes só podem deletar suas próprias contas
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isClienteLogado(#id)")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        if (clienteService.deletarCliente(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 