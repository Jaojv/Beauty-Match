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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
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
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/saloes").permitAll()
                .requestMatchers("/api/saloes/{id}").permitAll()
                .requestMatchers("/api/servicos").permitAll()
                .requestMatchers("/api/servicos/{id}").permitAll()
                .requestMatchers("/api/servicos/salao/{salaoId}").permitAll()
                .requestMatchers("/api/profissionais").permitAll()
                .requestMatchers("/api/profissionais/{id}").permitAll()
                .requestMatchers("/api/profissionais/salao/{salaoId}").permitAll()
                .requestMatchers("/api/agendamentos/horarios-disponiveis").permitAll()
                .requestMatchers("/api/quiz/perguntas").permitAll()
                .requestMatchers("/api/quiz/recomendacao").permitAll()
                .requestMatchers("/api/quiz/recomendacoes/{criterio}").permitAll()
                .requestMatchers("/api/quiz/debug/**").permitAll()
                .requestMatchers("/api/favoritos/**").hasRole("CLIENTE")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/error").permitAll()
                
                // Endpoints que requerem autenticação
                .requestMatchers("/api/agendamentos/**").authenticated()
                .requestMatchers("/api/clientes/**").authenticated()
                .requestMatchers("/api/profissionais/**").authenticated()
                .requestMatchers("/api/saloes/**").authenticated()
                .requestMatchers("/api/quiz/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );

        // Adiciona o filtro JWT antes do filtro padrão de autenticação
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permite todas as origens
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permite todos os headers
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(false); // Alterado para false para permitir todas as origens
        configuration.setMaxAge(3600L); // Cache das configurações CORS por 1 hora
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 