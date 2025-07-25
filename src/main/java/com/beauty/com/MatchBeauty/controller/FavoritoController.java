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
            System.out.println("🔍 DEBUG: Tentando listar favoritos");
            Long clienteId = securityService.getClienteLogadoId();
            System.out.println("🔍 DEBUG: Cliente ID obtido: " + clienteId);
            if (clienteId == null) {
                System.out.println("❌ DEBUG: Cliente ID é null");
                return ResponseEntity.badRequest().build();
            }
            List<FavoritoDTO.ResponseSimples> favoritos = favoritoService.listarFavoritosCliente(clienteId);
            System.out.println("✅ DEBUG: Favoritos listados com sucesso: " + favoritos.size());
            return ResponseEntity.ok(favoritos);
        } catch (Exception e) {
            System.out.println("❌ DEBUG: Erro ao listar favoritos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<FavoritoDTO.ResponseSimples> adicionarFavorito(@RequestBody FavoritoDTO.Request request) {
        try {
            System.out.println("🔍 DEBUG: Tentando adicionar favorito para salão: " + request.getSalaoId());
            Long clienteId = securityService.getClienteLogadoId();
            System.out.println("🔍 DEBUG: Cliente ID obtido: " + clienteId);
            if (clienteId == null) {
                System.out.println("❌ DEBUG: Cliente ID é null");
                return ResponseEntity.badRequest().build();
            }
            FavoritoDTO.ResponseSimples favorito = favoritoService.adicionarFavorito(clienteId, request.getSalaoId());
            System.out.println("✅ DEBUG: Favorito adicionado com sucesso");
            return ResponseEntity.ok(favorito);
        } catch (RuntimeException e) {
            System.out.println("❌ DEBUG: Erro ao adicionar favorito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{salaoId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> removerFavorito(@PathVariable Long salaoId) {
        try {
            System.out.println("🔍 DEBUG: Tentando remover favorito para salão: " + salaoId);
            Long clienteId = securityService.getClienteLogadoId();
            System.out.println("🔍 DEBUG: Cliente ID obtido: " + clienteId);
            if (clienteId == null) {
                System.out.println("❌ DEBUG: Cliente ID é null");
                return ResponseEntity.badRequest().build();
            }
            favoritoService.removerFavorito(clienteId, salaoId);
            System.out.println("✅ DEBUG: Favorito removido com sucesso");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.out.println("❌ DEBUG: Erro ao remover favorito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/verificar/{salaoId}")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Boolean> verificarFavorito(@PathVariable Long salaoId) {
        try {
            System.out.println("🔍 DEBUG: Tentando verificar favorito para salão: " + salaoId);
            Long clienteId = securityService.getClienteLogadoId();
            System.out.println("🔍 DEBUG: Cliente ID obtido: " + clienteId);
            if (clienteId == null) {
                System.out.println("❌ DEBUG: Cliente ID é null");
                return ResponseEntity.badRequest().build();
            }
            boolean isFavoritado = favoritoService.verificarFavorito(clienteId, salaoId);
            System.out.println("✅ DEBUG: Verificação concluída: " + isFavoritado);
            return ResponseEntity.ok(isFavoritado);
        } catch (Exception e) {
            System.out.println("❌ DEBUG: Erro ao verificar favorito: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/contar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Long> contarFavoritos() {
        try {
            System.out.println("🔍 DEBUG: Tentando contar favoritos");
            Long clienteId = securityService.getClienteLogadoId();
            System.out.println("🔍 DEBUG: Cliente ID obtido: " + clienteId);
            if (clienteId == null) {
                System.out.println("❌ DEBUG: Cliente ID é null");
                return ResponseEntity.badRequest().build();
            }
            Long quantidade = favoritoService.contarFavoritosCliente(clienteId);
            System.out.println("✅ DEBUG: Contagem concluída: " + quantidade);
            return ResponseEntity.ok(quantidade);
        } catch (Exception e) {
            System.out.println("❌ DEBUG: Erro ao contar favoritos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
} 