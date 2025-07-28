package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

// Componente responsável por gerar e validar tokens JWT
// Gerencia a criação, validação e extração de informações dos tokens
@Component
public class JwtTokenProvider {

    // Chave secreta para assinar os tokens
    private final Key key;
    // Tempo de expiração do token em milissegundos
    private final long jwtExpiration;

    // Construtor padrão com tempo de expiração de 15 minutos
    public JwtTokenProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtExpiration = 900000; // 15 minutos
    }

    // Construtor com tempo de expiração configurável
    public JwtTokenProvider(@Value("${app.jwt.expiration}") long jwtExpiration) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtExpiration = jwtExpiration;
    }

    // Gera um token JWT a partir da autenticação do usuário
    // Inclui ID do usuário, autoridades e tempo de expiração
    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long idUsuario;
        Collection<? extends GrantedAuthority> authorities;
        
        // Extrai informações do principal baseado no tipo
        if (principal instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            idUsuario = userPrincipal.getId();
            authorities = userPrincipal.getAuthorities();
        } else if (principal instanceof Usuario) {
            Usuario usuario = (Usuario) principal;
            idUsuario = usuario.getIdUsuario();
            authorities = usuario.getAuthorities();
        } else {
            throw new RuntimeException("Tipo de usuário não suportado para geração de token");
        }

        // Define datas de criação e expiração
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // Constrói o token JWT
        return Jwts.builder()
                .setSubject(Long.toString(idUsuario))
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    // Extrai o ID do usuário a partir do token JWT
    // Usado para identificar o usuário sem consultar o banco
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    // Valida se um token JWT é válido
    // Verifica se o token não expirou e se a assinatura está correta
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Retorna a chave secreta usada para assinar os tokens
    public Key getKey() {
        return key;
    }
} 