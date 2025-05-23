package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String tipoUsuario;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String password, String tipoUsuario) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        String role = tipoUsuario.equals("ADMINISTRADOR") ? "ADMIN" : tipoUsuario;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    public static UserPrincipal create(Usuario usuario) {
        return new UserPrincipal(
            usuario.getIdUsuario(),
            usuario.getUsername(),
            usuario.getPassword(),
            usuario.getClass().getSimpleName().toUpperCase()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
} 