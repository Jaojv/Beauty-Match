package com.beauty.com.MatchBeauty.service;

import java.util.Scanner;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.repository.AdminRepository;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import com.beauty.com.MatchBeauty.security.JwtTokenProvider;
import com.beauty.com.MatchBeauty.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

/**
 * SERVIÇO DE AUTENTICAÇÃO - GERENCIAMENTO DE LOGIN E TOKENS JWT
 * 
 * Este serviço gerencia todo o processo de autenticação do sistema,
 * incluindo login, geração de tokens JWT, validação de credenciais
 * e gerenciamento de sessões de usuário.
 * 
 * FUNCIONALIDADES:
 * - Autenticação de usuários com username e password
 * - Geração de tokens JWT para sessões
 * - Validação de credenciais de login
 * - Gerenciamento de contexto de segurança
 * - Verificação de existência de usuários
 * - Criptografia de senhas para novos usuários
 * 
 * SEGURANÇA:
 * - Uso de PasswordEncoder para criptografia
 * - Tokens JWT para autenticação stateless
 * - Spring Security para gerenciamento de contexto
 * - Validação de credenciais antes de gerar tokens
 * 
 * DEPENDÊNCIAS:
 * - UsuarioRepository: Para consulta de usuários
 * - JwtTokenProvider: Para geração de tokens JWT
 * - AuthenticationManager: Para autenticação Spring Security
 * - PasswordEncoder: Para criptografia de senhas
 */
@Service
public class AutenticacaoService {
    
    private static Usuario usuarioLogado;
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Scanner scanner;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminRepository adminRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final PasswordEncoder springPasswordEncoder;

    @Autowired
    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider,
            AdminRepository adminRepository,
            ClienteRepository clienteRepository,
            ProfissionalRepository profissionalRepository,
            PasswordEncoder springPasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.scanner = new Scanner(System.in);
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.adminRepository = adminRepository;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.springPasswordEncoder = springPasswordEncoder;
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

    public Usuario criarUsuario(String username, String password, String nome, String email, String tipoUsuario) {
        // Verificar se o usuário já existe
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username já está em uso");
        }

        // Criar usuário baseado no tipo
        Usuario usuario;
        switch (tipoUsuario.toUpperCase()) {
            case "ADMIN":
                usuario = new Admin();
                break;
            case "CLIENTE":
                usuario = new Cliente();
                break;
            case "PROFISSIONAL":
                usuario = new Profissional();
                break;
            case "PROPRIETARIO":
                usuario = new Proprietario();
                break;
            default:
                throw new RuntimeException("Tipo de usuário inválido");
        }

        // Configurar dados do usuário
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTipoUsuario(Usuario.TipoUsuario.valueOf(tipoUsuario.toUpperCase()));

        // Salvar usuário
        return usuarioRepository.save(usuario);
    }

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return tokenProvider.generateToken(authentication);
    }

    public Admin registrarAdmin(Admin admin) {
        if (usuarioRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe");
        }
        admin.setPassword(springPasswordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }

    public Cliente registrarCliente(Cliente cliente) {
        if (usuarioRepository.findByUsername(cliente.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe");
        }
        cliente.setPassword(springPasswordEncoder.encode(cliente.getPassword()));
        return clienteRepository.save(cliente);
    }

    public Profissional registrarProfissional(Profissional profissional) {
        if (usuarioRepository.findByUsername(profissional.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe");
        }
        profissional.setPassword(springPasswordEncoder.encode(profissional.getPassword()));
        return profissionalRepository.save(profissional);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
} 