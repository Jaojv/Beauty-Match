package com.beauty.com.MatchBeauty.security;

import com.beauty.com.MatchBeauty.entity.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long jwtExpiration;

    public JwtTokenProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtExpiration = 900000; // 15 minutos
    }

    public JwtTokenProvider(@Value("${app.jwt.expiration}") long jwtExpiration) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtExpiration = jwtExpiration;
    }

    public String generateToken(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long idUsuario;
        if (principal instanceof UserPrincipal) {
            idUsuario = ((UserPrincipal) principal).getId();
        } else if (principal instanceof Usuario) {
            idUsuario = ((Usuario) principal).getIdUsuario();
        } else {
            throw new RuntimeException("Tipo de usuário não suportado para geração de token");
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(Long.toString(idUsuario))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

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
} 