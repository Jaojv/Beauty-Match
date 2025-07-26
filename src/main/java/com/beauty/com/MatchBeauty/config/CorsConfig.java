package com.beauty.com.MatchBeauty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Classe de configuração CORS do sistema
// Permite requisições de diferentes origens (domínios)
@Configuration
public class CorsConfig {

    // Bean para configurar o filtro CORS
    // Define as regras de Cross-Origin Resource Sharing
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir todas as origens em desenvolvimento
        // Em produção, deve ser configurado com origens específicas
        config.addAllowedOrigin("*");
        
        // Permitir todos os métodos HTTP (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");
        
        // Permitir todos os headers HTTP
        config.addAllowedHeader("*");
        
        // Permitir credenciais (cookies, headers de autorização)
        config.setAllowCredentials(true);
        
        // Aplicar configuração para todas as rotas
        // O padrão /** significa todas as URLs
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 