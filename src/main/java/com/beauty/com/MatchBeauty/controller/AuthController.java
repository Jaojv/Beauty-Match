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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Recebida requisição de login para usuário: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Object principal = authentication.getPrincipal();
            Usuario usuario;
            String tipoUsuario;
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                usuario = new Usuario();
                usuario.setIdUsuario(userPrincipal.getId());
                usuario.setUsername(userPrincipal.getUsername());
                usuario.setPassword(userPrincipal.getPassword());
                tipoUsuario = userPrincipal.getTipoUsuario();
            } else if (principal instanceof Usuario) {
                usuario = (Usuario) principal;
                tipoUsuario = usuario.getClass().getSimpleName();
            } else {
                return ResponseEntity.badRequest().body("Usuário não autenticado corretamente");
            }

            logger.info("Login bem-sucedido para usuário: {}", loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(
                jwt,
                usuario.getIdUsuario(),
                usuario.getUsername(),
                tipoUsuario
            ));
        } catch (Exception e) {
            logger.error("Erro no login para usuário {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest().body("Credenciais inválidas");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest registroRequest) {
        logger.info("Recebida requisição de registro para usuário: {}", registroRequest.getUsername());
        logger.info("Dados do registro: username={}, nome={}, email={}, tipoUsuario={}", 
                   registroRequest.getUsername(), registroRequest.getNome(), 
                   registroRequest.getEmail(), registroRequest.getTipoUsuario());
        
        try {
            // Validar dados de entrada
            if (registroRequest.getUsername() == null || registroRequest.getUsername().trim().isEmpty()) {
                logger.warn("Username é obrigatório");
                return ResponseEntity.badRequest().body("Username é obrigatório");
            }
            if (registroRequest.getPassword() == null || registroRequest.getPassword().trim().isEmpty()) {
                logger.warn("Senha é obrigatória");
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }
            if (registroRequest.getNome() == null || registroRequest.getNome().trim().isEmpty()) {
                logger.warn("Nome é obrigatório");
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }
            if (registroRequest.getEmail() == null || registroRequest.getEmail().trim().isEmpty()) {
                logger.warn("Email é obrigatório");
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }
            if (registroRequest.getTipoUsuario() == null || registroRequest.getTipoUsuario().trim().isEmpty()) {
                logger.warn("Tipo de usuário é obrigatório");
                return ResponseEntity.badRequest().body("Tipo de usuário é obrigatório");
            }

            // Criar usuário
            autenticacaoService.criarUsuario(
                registroRequest.getUsername(),
                registroRequest.getPassword(),
                registroRequest.getNome(),
                registroRequest.getEmail(),
                registroRequest.getTipoUsuario()
            );

            // Autenticar usuário após registro
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    registroRequest.getUsername(),
                    registroRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Object principal = authentication.getPrincipal();
            String tipoUsuario;
            Long idUsuario;
            String username;
            if (principal instanceof UserPrincipal) {
                UserPrincipal userPrincipal = (UserPrincipal) principal;
                idUsuario = userPrincipal.getId();
                username = userPrincipal.getUsername();
                tipoUsuario = userPrincipal.getTipoUsuario();
            } else if (principal instanceof Usuario) {
                Usuario u = (Usuario) principal;
                idUsuario = u.getIdUsuario();
                username = u.getUsername();
                tipoUsuario = u.getClass().getSimpleName();
            } else {
                return ResponseEntity.badRequest().body("Usuário não autenticado corretamente");
            }

            logger.info("Registro bem-sucedido para usuário: {}", registroRequest.getUsername());
            return ResponseEntity.ok(new LoginResponse(
                jwt,
                idUsuario,
                username,
                tipoUsuario
            ));
        } catch (Exception e) {
            logger.error("Erro no registro para usuário {}: {}", registroRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erro ao realizar registro: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody Object request) {
        logger.info("Endpoint de teste chamado com sucesso");
        logger.info("Tipo da requisição: {}", request.getClass().getSimpleName());
        logger.info("Conteúdo da requisição: {}", request.toString());
        return ResponseEntity.ok("Teste funcionando! Dados recebidos: " + request.toString());
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        logger.info("Health check chamado");
        return ResponseEntity.ok("API funcionando!");
    }
} 