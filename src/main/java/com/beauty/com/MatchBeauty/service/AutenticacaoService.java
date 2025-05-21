package com.beauty.com.MatchBeauty.service;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;

@Service
public class AutenticacaoService {
    
    private static Usuario usuarioLogado;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Scanner scanner;

    @Autowired
    public AutenticacaoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.scanner = new Scanner(System.in);
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public boolean realizarLogin() {
        System.out.println("\n=== BeautyMatch Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        return usuarioRepository.findByUsername(username)
            .map(usuario -> {
                if (passwordEncoder.matches(password, usuario.getPassword())) {
                    usuarioLogado = usuario;
                    System.out.println("\nLogin bem-sucedido! Bem-vindo, " + usuario.getClass().getSimpleName().toUpperCase() + ".");
                    return true;
                }
                System.out.println("\nUsuário ou senha inválidos!");
                return false;
            })
            .orElseGet(() -> {
                System.out.println("\nUsuário ou senha inválidos!");
                return false;
            });
    }

    public void logout() {
        usuarioLogado = null;
        System.out.println("\nLogout realizado com sucesso!");
    }
} 