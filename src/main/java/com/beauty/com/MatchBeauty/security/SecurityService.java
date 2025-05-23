package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private Usuario getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(userPrincipal.getId());
            usuario.setUsername(userPrincipal.getUsername());
            usuario.setPassword(userPrincipal.getPassword());
            // Não temos nome/email/telefone aqui, mas para comparação de id e tipo basta
            return usuario;
        } else if (principal instanceof Usuario) {
            return (Usuario) principal;
        }
        return null;
    }

    public boolean isClienteLogado(Long idCliente) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof Cliente)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idCliente);
    }

    public boolean isProfissionalLogado(Long idProfissional) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof Profissional)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idProfissional);
    }

    public boolean isAdminLogado(Long idAdmin) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof Admin)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idAdmin);
    }

    public boolean isProprietarioLogado(Long idProprietario) {
        Usuario usuario = getUsuarioAutenticado();
        if (usuario == null || !(usuario instanceof com.beauty.com.MatchBeauty.entity.Proprietario)) {
            return false;
        }
        return usuario.getIdUsuario().equals(idProprietario);
    }
} 