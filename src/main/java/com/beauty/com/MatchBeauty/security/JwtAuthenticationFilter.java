package com.beauty.com.MatchBeauty.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// Filtro que intercepta todas as requisições para validar tokens JWT
// Configura a autenticação no contexto de segurança quando o token é válido
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Provedor de tokens JWT para validação e extração de informações
    private final JwtTokenProvider tokenProvider;

    // Construtor que recebe o provedor de tokens
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // Método principal que processa cada requisição
    // Extrai o token JWT do header Authorization e valida
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.println("🔍 DEBUG JwtFilter: Processando requisição para: " + request.getRequestURI());
            // Extrai o token JWT da requisição
            String jwt = getJwtFromRequest(request);
            System.out.println("🔍 DEBUG JwtFilter: JWT extraído: " + (jwt != null ? "SIM" : "NÃO"));

            // Valida o token se ele existir
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                System.out.println("✅ DEBUG JwtFilter: Token válido");
                // Extrai o ID do usuário do token
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                System.out.println("🔍 DEBUG JwtFilter: User ID: " + userId);

                // Carrega as autoridades do token
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(tokenProvider.getKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
                
                @SuppressWarnings("unchecked")
                List<String> authorities = claims.get("authorities", List.class);
                System.out.println("🔍 DEBUG JwtFilter: Authorities: " + authorities);
                
                if (authorities != null) {
                    // Converte as autoridades para objetos GrantedAuthority
                    List<GrantedAuthority> grantedAuthorities = authorities.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
                    
                    // Cria UserDetails com as informações do token
                    UserDetails userDetails = new UserPrincipal(
                        userId,
                        claims.getSubject(),
                        "", // Não precisamos da senha aqui
                        "", // Nome vazio - será buscado do banco quando necessário
                        "", // Email vazio - será buscado do banco quando necessário
                        authorities.get(0).replace("ROLE_", "")
                    );

                    // Cria o token de autenticação e configura no contexto
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("✅ DEBUG JwtFilter: Autenticação configurada com sucesso");
                }
            } else {
                System.out.println("❌ DEBUG JwtFilter: Token inválido ou não encontrado");
            }
        } catch (Exception ex) {
            System.out.println("❌ DEBUG JwtFilter: Erro ao processar token: " + ex.getMessage());
            logger.error("Não foi possível autenticar o usuário", ex);
        }

        // Continua o processamento da requisição
        filterChain.doFilter(request, response);
    }

    // Extrai o token JWT do header Authorization
    // Remove o prefixo "Bearer " se presente
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 