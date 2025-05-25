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

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SecurityService securityService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> listarAdmins() {
        return ResponseEntity.ok(adminService.listarAdmins());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Admin> buscarAdmin(@PathVariable Long id) {
        Admin admin = adminService.buscarAdmin(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.notFound().build();
    }

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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Void> deletarAdmin(@PathVariable Long id) {
        if (adminService.deletarAdmin(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
} 