package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.FavoritoDTO;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class FavoritoController {
    
    @Autowired
    private FavoritoService favoritoService;
    
    @Autowired
    private SecurityService securityService;
    
    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<FavoritoDTO.ResponseSimples>> listarFavoritos() {
        try {
            Long clienteId = securityService.getClienteLogadoId();
            List<FavoritoDTO.ResponseSimples> favoritos = favoritoService.listarFavoritosCliente(clienteId);
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<FavoritoDTO.ResponseSimples> adicionarFavorito(@RequestBody FavoritoDTO.Request request) {
        try {
            Long clienteId = securityService.getClienteLogadoId();
            FavoritoDTO.ResponseSimples favorito = favoritoService.adicionarFavorito(clienteId, request.getSalaoId());
            return ResponseEntity.ok(favorito);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{salaoId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> removerFavorito(@PathVariable Long salaoId) {
        try {
            Long clienteId = securityService.getClienteLogadoId();
            favoritoService.removerFavorito(clienteId, salaoId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/verificar/{salaoId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Boolean> verificarFavorito(@PathVariable Long salaoId) {
        try {
            Long clienteId = securityService.getClienteLogadoId();
            boolean isFavoritado = favoritoService.verificarFavorito(clienteId, salaoId);
            return ResponseEntity.ok(isFavoritado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/contar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Long> contarFavoritos() {
        try {
            Long clienteId = securityService.getClienteLogadoId();
            Long quantidade = favoritoService.contarFavoritosCliente(clienteId);
            return ResponseEntity.ok(quantidade);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 