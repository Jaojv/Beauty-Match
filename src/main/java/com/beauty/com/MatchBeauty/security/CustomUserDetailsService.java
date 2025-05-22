package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Tentando carregar usuário por username: " + username);
        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado: " + username);
                    return new UsernameNotFoundException("Usuário não encontrado com username: " + username);
                });

        System.out.println("Usuário encontrado: " + usuario.getClass().getSimpleName());
        UserDetails userDetails = UserPrincipal.create(usuario);
        System.out.println("UserDetails criado com sucesso");
        
        return userDetails;
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        System.out.println("Tentando carregar usuário por id: " + id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado com id: " + id);
                    return new UsernameNotFoundException("Usuário não encontrado com id: " + id);
                });

        System.out.println("Usuário encontrado: " + usuario.getClass().getSimpleName());
        UserDetails userDetails = UserPrincipal.create(usuario);
        System.out.println("UserDetails criado com sucesso");
        
        return userDetails;
    }
} 