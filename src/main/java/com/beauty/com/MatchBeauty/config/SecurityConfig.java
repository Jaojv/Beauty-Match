package com.beauty.com.MatchBeauty.config;

import com.beauty.com.MatchBeauty.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Classe de configuração de segurança do sistema
// Define as regras de autenticação e autorização para as requisições HTTP
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Filtro JWT para autenticação baseada em token
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Bean para gerenciar a autenticação
    // Responsável por processar as credenciais dos usuários
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Configuração principal da cadeia de filtros de segurança
    // Define quais endpoints são públicos e quais requerem autenticação
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configuração CORS para permitir requisições de diferentes origens
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Desabilita CSRF pois estamos usando JWT
            .csrf(csrf -> csrf.disable())
            // Configura sessões stateless (sem estado) para JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configuração das regras de autorização para diferentes endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos - não requerem autenticação
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/pages/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/uploads/**",
                    "/fonts/**",
                    "/favicon.ico"
                ).permitAll()
                // Endpoints de autenticação são públicos
                .requestMatchers("/api/auth/**").permitAll()
                // Endpoints de consulta de salões são públicos
                .requestMatchers("/api/saloes").permitAll()
                .requestMatchers("/api/saloes/{id}").permitAll()
                // Endpoints de consulta de serviços são públicos
                .requestMatchers("/api/servicos").permitAll()
                .requestMatchers("/api/servicos/{id}").permitAll()
                .requestMatchers("/api/servicos/salao/{salaoId}").permitAll()
                // Endpoints de consulta de profissionais são públicos
                .requestMatchers("/api/profissionais").permitAll()
                .requestMatchers("/api/profissionais/{id}").permitAll()
                .requestMatchers("/api/profissionais/salao/{salaoId}").permitAll()
                // Endpoints de consulta de horários disponíveis são públicos
                .requestMatchers("/api/agendamentos/horarios-disponiveis").permitAll()
                // Endpoints do quiz são públicos
                .requestMatchers("/api/quiz/perguntas").permitAll()
                .requestMatchers("/api/quiz/recomendacao").permitAll()
                .requestMatchers("/api/quiz/recomendacoes/{criterio}").permitAll()
                .requestMatchers("/api/quiz/debug/**").permitAll()
                // Endpoints de favoritos requerem role CLIENTE
                .requestMatchers("/api/favoritos/**").hasRole("CLIENTE")
                // Documentação da API é pública
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Página de erro é pública
                .requestMatchers("/error").permitAll()
                
                // Endpoints que requerem autenticação - usuário deve estar logado
                .requestMatchers("/api/agendamentos/**").authenticated()
                .requestMatchers("/api/clientes/**").authenticated()
                .requestMatchers("/api/profissionais/**").authenticated()
                .requestMatchers("/api/saloes/**").authenticated()
                .requestMatchers("/api/quiz/**").authenticated()
                // Endpoints de admin requerem role ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            );

        // Adiciona o filtro JWT antes do filtro padrão de autenticação
        // Este filtro verifica o token JWT em cada requisição
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    // Bean para codificação de senhas
    // Usa BCrypt para hash seguro das senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração CORS para permitir requisições de diferentes origens
    // Define quais origens, métodos e headers são permitidos
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite todas as origens para desenvolvimento
        configuration.setAllowedOrigins(Arrays.asList("*")); 
        // Permite todos os métodos HTTP principais
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Permite todos os headers
        configuration.setAllowedHeaders(Arrays.asList("*")); 
        // Expõe o header Authorization para o frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        // Desabilita credenciais para permitir todas as origens
        configuration.setAllowCredentials(false); 
        // Cache das configurações CORS por 1 hora
        configuration.setMaxAge(3600L); 
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração para todas as rotas
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 