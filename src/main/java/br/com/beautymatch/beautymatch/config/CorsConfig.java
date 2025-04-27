package br.com.beautymatch.beautymatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permitir todas as origens em desenvolvimento
        config.addAllowedOrigin("*");
        
        // Permitir todos os headers
        config.addAllowedHeader("*");
        
        // Permitir todos os métodos HTTP
        config.addAllowedMethod("*");
        
        // Permitir credenciais
        config.setAllowCredentials(true);
        
        // Tempo máximo que o navegador deve armazenar em cache a resposta preflight
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
} 