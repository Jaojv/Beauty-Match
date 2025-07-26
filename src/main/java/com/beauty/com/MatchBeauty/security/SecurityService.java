package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

// Serviço responsável por operações de segurança e validação de permissões
// Fornece métodos para verificar se usuários podem acessar recursos específicos
@Service
public class SecurityService {

    // Repositório para operações de cliente
    @Autowired
    private ClienteRepository clienteRepository;

    // Método privado para obter o usuário autenticado do contexto de segurança
    // Converte UserPrincipal para Usuario para facilitar comparações
    private Usuario getUsuarioAutenticado() {
        System.out.println("🔍 DEBUG SecurityService: Obtendo usuário autenticado");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔍 DEBUG SecurityService: Authentication: " + authentication);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ DEBUG SecurityService: Authentication é null ou não autenticado");
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        System.out.println("🔍 DEBUG SecurityService: Principal: " + principal);
        System.out.println("🔍 DEBUG SecurityService: Tipo do principal: " + (principal != null ? principal.getClass().getName() : "null"));
        
        // Converte UserPrincipal para Usuario
        if (principal instanceof UserPrincipal) {
            System.out.println("✅ DEBUG SecurityService: Principal é UserPrincipal");
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            System.out.println("🔍 DEBUG SecurityService: UserPrincipal ID: " + userPrincipal.getId());
            System.out.println("🔍 DEBUG SecurityService: UserPrincipal Username: " + userPrincipal.getUsername());
            System.out.println("🔍 DEBUG SecurityService: UserPrincipal Tipo: " + userPrincipal.getTipoUsuario());
            
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(userPrincipal.getId());
            usuario.setUsername(userPrincipal.getUsername());
            usuario.setPassword(userPrincipal.getPassword());
            usuario.setTipoUsuario(TipoUsuario.valueOf(userPrincipal.getTipoUsuario().toUpperCase()));
            // Não temos nome/email/telefone aqui, mas para comparação de id e tipo basta
            System.out.println("✅ DEBUG SecurityService: Usuario criado com ID: " + usuario.getIdUsuario());
            return usuario;
        } else if (principal instanceof Usuario) {
            System.out.println("✅ DEBUG SecurityService: Principal é Usuario");
            return (Usuario) principal;
        }
        
        System.out.println("❌ DEBUG SecurityService: Principal não é UserPrincipal nem Usuario");
        return null;
    }

    // Verifica se o cliente logado é o mesmo que está sendo acessado
    // Usado para controlar acesso a recursos específicos do cliente
    public boolean isClienteLogado(Long idCliente) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof Cliente)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idCliente);
    }

    // Verifica se o profissional logado é o mesmo que está sendo acessado
    // Usado para controlar acesso a recursos específicos do profissional
    public boolean isProfissionalLogado(Long idProfissional) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof Profissional)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idProfissional);
    }

    // Verifica se o admin logado é o mesmo que está sendo acessado
    // Usado para controlar acesso a recursos específicos do admin
    public boolean isAdminLogado(Long idAdmin) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null) {
            return false;
        }
        // Verifica se o tipo de usuário é ADMIN
        if (usuario.getTipoUsuario() != TipoUsuario.ADMIN) {
            return false;
        }
        return usuario.getIdUsuario().equals(idAdmin);
    }

    // Verifica se o proprietário logado é o mesmo que está sendo acessado
    // Usado para controlar acesso a recursos específicos do proprietário
    public boolean isProprietarioLogado(Long idProprietario) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof com.beauty.com.MatchBeauty.entity.Proprietario)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idProprietario);
    }
    
    // Retorna o ID do cliente atualmente autenticado
    // Usado para obter o ID do cliente logado sem precisar de parâmetros
    public Long getClienteLogadoId() {
        System.out.println("🔍 DEBUG SecurityService: getClienteLogadoId() chamado");
        Usuario usuario = getUsuarioAutenticado();
        System.out.println("🔍 DEBUG SecurityService: Usuario obtido: " + usuario);
        
        if (usuario == null) {
            System.out.println("❌ DEBUG SecurityService: Usuario é null");
            return null;
        }
        
        System.out.println("🔍 DEBUG SecurityService: Tipo do usuário: " + usuario.getTipoUsuario());
        
        // Se o usuário é do tipo CLIENTE, retornar o ID do usuário
        if (usuario.getTipoUsuario() == TipoUsuario.CLIENTE) {
            System.out.println("✅ DEBUG SecurityService: Usuario é do tipo CLIENTE, retornando ID: " + usuario.getIdUsuario());
            return usuario.getIdUsuario();
        }
        
        System.out.println("❌ DEBUG SecurityService: Usuario não é do tipo CLIENTE");
        return null;
    }
} 