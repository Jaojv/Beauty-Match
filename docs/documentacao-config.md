# Documentação da Pasta Config (Configurações)

## Visão Geral

A pasta `config` contém todas as configurações do sistema BeautyMatch, responsáveis por definir comportamentos globais da aplicação, incluindo segurança, CORS, web e agendamento de tarefas. Estas configurações são essenciais para o funcionamento correto e seguro do sistema.

## Estrutura da Pasta

```
src/main/java/com/beauty/com/MatchBeauty/config/
├── SecurityConfig.java          # Configurações de segurança e autenticação
├── WebConfig.java               # Configurações web e CORS
├── CorsConfig.java              # Configurações específicas de CORS
└── SchedulerConfig.java         # Configurações de agendamento de tarefas
```

## Padrões Utilizados

### 1. Configuração por Anotações
- Uso de `@Configuration` para definir classes de configuração
- `@Bean` para registrar componentes no contexto Spring
- `@EnableWebSecurity` para habilitar segurança web
- `@EnableScheduling` para habilitar agendamento

### 2. Configuração Funcional
- Uso de lambdas para configuração de beans
- Configuração fluente (method chaining)
- Separação clara de responsabilidades

### 3. Configuração Modular
- Cada aspecto da aplicação tem sua própria configuração
- Configurações independentes e reutilizáveis
- Fácil manutenção e extensão

## Configurações Detalhadas

### 1. SecurityConfig

**Arquivo**: `SecurityConfig.java`

**Responsabilidade**: Configuração completa de segurança, autenticação e autorização.

#### Anotações Principais
```java
@Configuration
@EnableWebSecurity
```

#### Beans Configurados

##### AuthenticationManager
```java
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}
```
**Função**: Gerencia a autenticação de usuários no sistema.

##### SecurityFilterChain
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Endpoints públicos
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/saloes").permitAll()
            .requestMatchers("/api/saloes/{id}").permitAll()
            .requestMatchers("/api/servicos").permitAll()
            .requestMatchers("/api/servicos/{id}").permitAll()
            .requestMatchers("/api/profissionais").permitAll()
            .requestMatchers("/api/profissionais/{id}").permitAll()
            .requestMatchers("/api/agendamentos/horarios-disponiveis").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/error").permitAll()
            
            // Endpoints que requerem autenticação
            .requestMatchers("/api/agendamentos/**").authenticated()
            .requestMatchers("/api/clientes/**").authenticated()
            .requestMatchers("/api/profissionais/**").authenticated()
            .requestMatchers("/api/saloes/**").authenticated()
            .requestMatchers("/api/servicos/**").authenticated()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        );

    // Adiciona o filtro JWT antes do filtro padrão de autenticação
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
}
```

**Configurações de Segurança**:
- **CORS**: Configurado com fonte personalizada
- **CSRF**: Desabilitado (não necessário para APIs stateless)
- **Sessão**: Configurada como STATELESS (sem estado)
- **Autorização**: Endpoints públicos e protegidos definidos
- **Filtro JWT**: Adicionado antes do filtro padrão

##### PasswordEncoder
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
**Função**: Codifica senhas usando BCrypt para segurança.

##### CorsConfigurationSource
```java
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
```

**Configurações CORS**:
- **Origens**: Permite todas as origens (`*`)
- **Métodos**: GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Headers**: Permite todos os headers
- **Headers Expostos**: Authorization
- **Credenciais**: Desabilitadas para permitir todas as origens
- **Cache**: 1 hora

### 2. WebConfig

**Arquivo**: `WebConfig.java`

**Responsabilidade**: Configurações web e CORS específicas para MVC.

#### Anotações Principais
```java
@Configuration
```

#### Beans Configurados

##### WebMvcConfigurer
```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOriginPatterns(
                    "http://localhost:3000",   // React local
                    "http://localhost:8081",   // Outro serviço local
                    "http://localhost:8080"    // Postman ou outro backend local
                    // "https://meusistema.com.br" // Produção (comente enquanto não tiver domínio)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
        }
    };
}
```

**Configurações CORS MVC**:
- **Mapeamento**: Aplicado a todas as rotas (`/**`)
- **Origens Permitidas**: 
  - `http://localhost:3000` (React local)
  - `http://localhost:8081` (Outro serviço local)
  - `http://localhost:8080` (Postman/backend local)
- **Métodos**: GET, POST, PUT, DELETE, OPTIONS
- **Credenciais**: Habilitadas

### 3. CorsConfig

**Arquivo**: `CorsConfig.java`

**Responsabilidade**: Configuração específica de CORS usando filtro.

#### Anotações Principais
```java
@Configuration
```

#### Beans Configurados

##### CorsFilter
```java
@Bean
public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    
    // Permitir todas as origens em desenvolvimento
    config.addAllowedOrigin("*");
    
    // Permitir todos os métodos HTTP
    config.addAllowedMethod("*");
    
    // Permitir todos os headers
    config.addAllowedHeader("*");
    
    // Permitir credenciais
    config.setAllowCredentials(true);
    
    // Aplicar configuração para todas as rotas
    source.registerCorsConfiguration("/**", config);
    
    return new CorsFilter(source);
}
```

**Configurações CORS Filtro**:
- **Origens**: Permite todas as origens (`*`)
- **Métodos**: Permite todos os métodos HTTP (`*`)
- **Headers**: Permite todos os headers (`*`)
- **Credenciais**: Habilitadas
- **Aplicação**: Todas as rotas (`/**`)

### 4. SchedulerConfig

**Arquivo**: `SchedulerConfig.java`

**Responsabilidade**: Habilitar agendamento de tarefas no sistema.

#### Anotações Principais
```java
@Configuration
@EnableScheduling
```

**Função**: Habilita o uso de `@Scheduled` em métodos da aplicação para execução de tarefas agendadas.

## Endpoints e Autorização

### Endpoints Públicos
- `/api/auth/**` - Autenticação e registro
- `/api/saloes` - Listagem de salões
- `/api/saloes/{id}` - Detalhes de salão específico
- `/api/servicos` - Listagem de serviços
- `/api/servicos/{id}` - Detalhes de serviço específico
- `/api/profissionais` - Listagem de profissionais
- `/api/profissionais/{id}` - Detalhes de profissional específico
- `/api/agendamentos/horarios-disponiveis` - Horários disponíveis
- `/swagger-ui/**` - Documentação Swagger
- `/v3/api-docs/**` - Especificação OpenAPI
- `/error` - Páginas de erro

### Endpoints Autenticados
- `/api/agendamentos/**` - Operações de agendamento
- `/api/clientes/**` - Operações de cliente
- `/api/profissionais/**` - Operações de profissional
- `/api/saloes/**` - Operações de salão
- `/api/servicos/**` - Operações de serviço

### Endpoints Administrativos
- `/api/admin/**` - Requer role ADMIN

## Configurações de Segurança

### 1. Autenticação JWT
- Filtro JWT personalizado (`JwtAuthenticationFilter`)
- Sessões stateless
- Tokens JWT para autenticação

### 2. Codificação de Senhas
- BCrypt para hash de senhas
- Configuração automática do encoder

### 3. Controle de Acesso
- Autorização baseada em roles
- Endpoints públicos e protegidos
- Controle granular de acesso

## Configurações CORS

### 1. Múltiplas Configurações
- **SecurityConfig**: CORS para segurança
- **WebConfig**: CORS para MVC
- **CorsConfig**: CORS via filtro

### 2. Configurações de Desenvolvimento
- Permite todas as origens em desenvolvimento
- Headers e métodos liberados
- Credenciais configuradas

### 3. Configurações de Produção
- Origens específicas para produção
- Configurações mais restritivas
- Segurança aprimorada

## Integrações

### 1. Com Security
- Integração com `JwtAuthenticationFilter`
- Configuração de autenticação
- Controle de acesso baseado em roles

### 2. Com Controllers
- Configuração de CORS para endpoints
- Configuração de segurança para rotas
- Suporte a diferentes tipos de requisição

### 3. Com Services
- Configuração de agendamento para serviços
- Suporte a tarefas agendadas
- Configuração de execução assíncrona

## Considerações de Ambiente

### 1. Desenvolvimento
- CORS liberado para todas as origens
- Configurações de debug habilitadas
- Endpoints de documentação acessíveis

### 2. Produção
- CORS restrito a origens específicas
- Configurações de segurança rigorosas
- Logs e debug desabilitados

### 3. Teste
- Configurações específicas para testes
- Endpoints de teste habilitados
- Configurações de mock

## Próximos Passos e Melhorias

### 1. Implementações Sugeridas
- [ ] Configuração de rate limiting
- [ ] Configuração de cache distribuído
- [ ] Configuração de métricas e monitoramento
- [ ] Configuração de logs estruturados

### 2. Melhorias de Segurança
- [ ] Implementar HTTPS obrigatório
- [ ] Configurar headers de segurança
- [ ] Implementar rate limiting por IP
- [ ] Configurar auditoria de acesso

### 3. Melhorias de Performance
- [ ] Configurar cache de configurações
- [ ] Implementar configurações de pool de conexões
- [ ] Configurar timeout de requisições
- [ ] Implementar circuit breaker

### 4. Configurações de Ambiente
- [ ] Configurações específicas por ambiente
- [ ] Configurações via variáveis de ambiente
- [ ] Configurações de backup e recuperação
- [ ] Configurações de monitoramento

### 5. Testes
- [ ] Testes de configuração de segurança
- [ ] Testes de configuração CORS
- [ ] Testes de configuração de agendamento
- [ ] Testes de integração de configurações

## Conclusão

A pasta `config` implementa uma arquitetura robusta de configurações que garante a segurança, performance e funcionalidade do sistema BeautyMatch. As configurações são modulares, bem estruturadas e seguem as melhores práticas do Spring Boot.

A estrutura atual suporta adequadamente as necessidades de desenvolvimento e produção, mas há oportunidades de melhoria através da implementação de configurações mais avançadas de segurança, performance e monitoramento.

## Análise de Escalabilidade e Manutenibilidade

A configuração atual do sistema BeautyMatch demonstra uma arquitetura bem estruturada que segue as melhores práticas do Spring Boot. A separação clara de responsabilidades entre as diferentes configurações facilita a manutenção e extensão do sistema.

**Pontos Fortes:**
- Configuração modular e bem organizada
- Segurança robusta com JWT e controle de acesso
- Configuração CORS flexível para diferentes ambientes
- Suporte a agendamento de tarefas
- Configuração stateless adequada para APIs

**Oportunidades de Melhoria:**
- Implementar configurações específicas por ambiente (dev, test, prod)
- Adicionar configurações de monitoramento e métricas
- Implementar rate limiting para proteção contra ataques
- Configurar cache distribuído para melhor performance
- Adicionar configurações de auditoria e logs estruturados

A estrutura atual suporta adequadamente as operações principais do sistema, mas pode ser aprimorada com as melhorias sugeridas para maior escalabilidade e manutenibilidade em ambientes de produção. 