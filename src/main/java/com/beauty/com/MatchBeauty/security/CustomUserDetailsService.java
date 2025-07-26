package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Serviço customizado para carregar detalhes do usuário
// Implementa UserDetailsService do Spring Security para autenticação
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Repositório para operações de usuário
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método principal para carregar usuário por username
    // Usado pelo Spring Security durante o processo de autenticação
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Tentando carregar usuário por username: " + username);
        
        // Busca o usuário no banco de dados pelo username
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado: " + username);
                    return new UsernameNotFoundException("Usuário não encontrado com username: " + username);
                });

        System.out.println("Usuário encontrado: " + usuario.getClass().getSimpleName());
        // Cria UserDetails a partir do usuário encontrado
        UserDetails userDetails = UserPrincipal.create(usuario);
        System.out.println("UserDetails criado com sucesso");
        
        return userDetails;
    }

    // Método para carregar usuário por ID
    // Usado quando precisamos obter detalhes do usuário pelo ID
    @Transactional
    public UserDetails loadUserById(Long id) {
        System.out.println("Tentando carregar usuário por id: " + id);
        
        // Busca o usuário no banco de dados pelo ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Usuário não encontrado com id: " + id);
                    return new UsernameNotFoundException("Usuário não encontrado com id: " + id);
                });

        System.out.println("Usuário encontrado: " + usuario.getClass().getSimpleName());
        // Cria UserDetails a partir do usuário encontrado
        UserDetails userDetails = UserPrincipal.create(usuario);
        System.out.println("UserDetails criado com sucesso");
        
        return userDetails;
    }
} 