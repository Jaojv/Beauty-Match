package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.LoginRequest;
import com.beauty.com.MatchBeauty.dto.LoginResponse;
import com.beauty.com.MatchBeauty.dto.RegistroRequest;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.security.JwtTokenProvider;
import com.beauty.com.MatchBeauty.security.UserPrincipal;
import com.beauty.com.MatchBeauty.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Controller responsável por gerenciar autenticação e autorização
// Fornece endpoints para login, registro e consulta de perfil
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Gerenciador de autenticação do Spring Security
    @Autowired
    private AuthenticationManager authenticationManager;

    // Provedor de tokens JWT
    @Autowired
    private JwtTokenProvider tokenProvider;

    // Serviço de autenticação
    @Autowired
    private AutenticacaoService autenticacaoService;

    // Endpoint para login de usuários
    // Autentica credenciais e retorna token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Autentica as credenciais fornecidas
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            // Define a autenticação no contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Gera token JWT para o usuário autenticado
            String jwt = tokenProvider.generateToken(authentication);

            // Extrai informações do usuário autenticado
            Object principal = authentication.getPrincipal();
            Usuario usuario;
            String tipoUsuario;
            
            // Verifica se é um UserPrincipal (padrão do Spring Security)
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                usuario = new Usuario();
                usuario.setIdUsuario(userPrincipal.getId());
                usuario.setUsername(userPrincipal.getUsername());
                usuario.setPassword(userPrincipal.getPassword());
                usuario.setNome(userPrincipal.getNome());
                usuario.setEmail(userPrincipal.getEmail());
                tipoUsuario = userPrincipal.getTipoUsuario();
            } else if (principal instanceof Usuario) {
                // Caso seja diretamente um Usuario
                usuario = (Usuario) principal;
                tipoUsuario = usuario.getClass().getSimpleName();
            } else {
                return ResponseEntity.badRequest().body("Usuário não autenticado corretamente");
            }

            // Retorna resposta com token e informações do usuário
            return ResponseEntity.ok(new LoginResponse(
                jwt,
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getNome(),
                usuario.getEmail(),
                tipoUsuario
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }

    // Endpoint para registro de novos usuários
    // Cria nova conta com validação de dados obrigatórios
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest registroRequest) {
        try {
            // Validação de dados obrigatórios
            if (registroRequest.getUsername() == null || registroRequest.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username é obrigatório");
            }
            if (registroRequest.getPassword() == null || registroRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }
            if (registroRequest.getNome() == null || registroRequest.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }
            if (registroRequest.getEmail() == null || registroRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }
            if (registroRequest.getTipoUsuario() == null || registroRequest.getTipoUsuario().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Tipo de usuário é obrigatório");
            }

            // Cria o usuário através do serviço de autenticação
            Usuario usuario = autenticacaoService.criarUsuario(
                registroRequest.getUsername(),
                registroRequest.getPassword(),
                registroRequest.getNome(),
                registroRequest.getEmail(),
                registroRequest.getTipoUsuario()
            );

            return ResponseEntity.ok("Usuário registrado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar registro: " + e.getMessage());
        }
    }

    // Endpoint para consultar perfil do usuário logado
    // Retorna informações do usuário autenticado
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            // Obtém a autenticação atual do contexto de segurança
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("Usuário não autenticado");
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                
                // Busca dados completos do usuário no banco de dados
                Usuario usuario = autenticacaoService.buscarUsuarioPorId(userPrincipal.getId());
                
                if (usuario != null) {
                    return ResponseEntity.ok(new LoginResponse(
                        null, // Não retorna token novamente
                        usuario.getIdUsuario(),
                        usuario.getUsername(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getClass().getSimpleName().toUpperCase()
                    ));
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.badRequest().body("Usuário não autenticado corretamente");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar perfil: " + e.getMessage());
        }
    }
} 