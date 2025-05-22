package com.beauty.com.MatchBeauty.service;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import com.beauty.com.MatchBeauty.security.JwtTokenProvider;

@Service
public class AutenticacaoService {
    
    private static Usuario usuarioLogado;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Scanner scanner;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.scanner = new Scanner(System.in);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public boolean realizarLogin() {
        try {
            System.out.println("\n=== BeautyMatch Login ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            System.out.println("Tentando autenticar usuário: " + username);
            
            // Primeiro, vamos verificar se o usuário existe
            Usuario usuario = usuarioRepository.findByUsername(username)
                .orElse(null);
            
            if (usuario == null) {
                System.out.println("\nUsuário não encontrado!");
                return false;
            }
            
            System.out.println("Usuário encontrado: " + usuario.getClass().getSimpleName());
            
            // Verificar se a senha está correta
            if (!passwordEncoder.matches(password, usuario.getPassword())) {
                System.out.println("\nSenha incorreta!");
                return false;
            }
            
            System.out.println("Senha verificada com sucesso");
            
            // Criar token de autenticação
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, password);
            
            // Autenticar
            authentication = authenticationManager.authenticate(authentication);
            System.out.println("Autenticação bem-sucedida");
            
            // Configurar contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Gerar token JWT
            String jwt = tokenProvider.generateToken(authentication);
            System.out.println("Token JWT gerado");
            
            // Configurar usuário logado
            usuarioLogado = usuario;
            System.out.println("\nLogin bem-sucedido! Bem-vindo, " + usuario.getClass().getSimpleName().toUpperCase() + ".");
            System.out.println("Token JWT: " + jwt);
            
            return true;
            
        } catch (Exception e) {
            System.out.println("\nErro durante a autenticação: " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        usuarioLogado = null;
        SecurityContextHolder.clearContext();
        System.out.println("\nLogout realizado com sucesso!");
    }

    public String getTokenAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return tokenProvider.generateToken(authentication);
        }
        return null;
    }
} 