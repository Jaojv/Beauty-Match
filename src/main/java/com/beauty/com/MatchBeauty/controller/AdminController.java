package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.AdminDTO;
import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller responsável por gerenciar operações relacionadas aos administradores
// Fornece endpoints para CRUD de administradores com controle de acesso baseado em roles
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Serviço para operações de administrador
    @Autowired
    private AdminService adminService;

    // Serviço de segurança para validação de permissões
    @Autowired
    private SecurityService securityService;

    // Endpoint para listar todos os administradores
    // Apenas administradores podem acessar esta funcionalidade
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> listarAdmins() {
        return ResponseEntity.ok(adminService.listarAdmins());
    }

    // Endpoint para buscar um administrador específico por ID
    // Apenas administradores podem acessar e apenas o próprio admin pode ver seus dados
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Admin> buscarAdmin(@PathVariable Long id) {
        Admin admin = adminService.buscarAdmin(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para criar um novo administrador
    // Apenas administradores podem criar outros administradores
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> criarAdmin(@RequestBody AdminDTO dto) {
        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        admin.setTelefone(dto.getTelefone());
        admin.setNome(dto.getNome());
        admin.setNivelAcesso(dto.getNivelAcesso());
        
        Admin novoAdmin = adminService.criarAdmin(admin);
        return ResponseEntity.ok(novoAdmin);
    }

    // Endpoint para atualizar dados de um administrador
    // Apenas administradores podem atualizar e apenas o próprio admin pode atualizar seus dados
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Admin> atualizarAdmin(@PathVariable Long id, @RequestBody AdminDTO dto) {
        Admin admin = adminService.buscarAdmin(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        admin.setTelefone(dto.getTelefone());
        admin.setNome(dto.getNome());
        admin.setNivelAcesso(dto.getNivelAcesso());

        Admin adminAtualizado = adminService.atualizarAdmin(admin);
        return ResponseEntity.ok(adminAtualizado);
    }

    // Endpoint para deletar um administrador
    // Apenas administradores podem deletar e apenas o próprio admin pode deletar sua conta
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Void> deletarAdmin(@PathVariable Long id) {
        if (adminService.deletarAdmin(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 