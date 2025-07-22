# Documentação - Pasta Security

## Visão Geral

A pasta `security` centraliza toda a infraestrutura de autenticação e autorização do sistema BeautyMatch. Utiliza Spring Security, JWT (JSON Web Token) e implementa regras customizadas para controle de acesso baseado em roles e propriedade de recursos.

---

## Componentes Principais

### 1. JwtTokenProvider
**Responsabilidade:** Gerar, validar e extrair informações de tokens JWT.

**Principais métodos:**
- `generateToken(Authentication)`: Gera um token JWT para o usuário autenticado, incluindo authorities (roles).
- `getUserIdFromJWT(String)`: Extrai o ID do usuário do token.
- `validateToken(String)`: Valida a assinatura e expiração do token.
- `getKey()`: Retorna a chave secreta usada na assinatura.

**Integrações:**
- Usado pelo filtro de autenticação e pelo AuthController.
- Utiliza a biblioteca `io.jsonwebtoken` para manipulação dos tokens.

---

### 2. JwtAuthenticationFilter
**Responsabilidade:** Interceptar todas as requisições HTTP, extrair e validar o token JWT, e configurar o contexto de segurança do Spring.

**Fluxo:**
- Extrai o token do header `Authorization`.
- Valida o token usando `JwtTokenProvider`.
- Extrai o ID do usuário e authorities do token.
- Cria um objeto `UserPrincipal` e configura o contexto de autenticação do Spring.
- Permite que a requisição prossiga para o controller.

**Integrações:**
- Usado como filtro global na aplicação.
- Depende de `JwtTokenProvider` e `UserPrincipal`.

---

### 3. UserPrincipal
**Responsabilidade:** Representar o usuário autenticado no contexto do Spring Security.

**Principais métodos:**
- Implementa `UserDetails` do Spring Security.
- Armazena ID, username, senha, tipo de usuário e authorities (roles).
- Métodos para controle de expiração, bloqueio e ativação de conta.

**Integrações:**
- Usado por `JwtAuthenticationFilter`, `CustomUserDetailsService` e em todo o contexto de autenticação.
- Facilita o controle de roles e permissões.

---

### 4. CustomUserDetailsService
**Responsabilidade:** Carregar os detalhes do usuário a partir do banco de dados para autenticação.

**Principais métodos:**
- `loadUserByUsername(String)`: Busca usuário pelo username e retorna um `UserPrincipal`.
- `loadUserById(Long)`: Busca usuário pelo ID e retorna um `UserPrincipal`.

**Integrações:**
- Usado pelo Spring Security durante o processo de autenticação.
- Integra-se ao `UsuarioRepository`.

---

### 5. SecurityService
**Responsabilidade:** Implementar regras customizadas de autorização para garantir que apenas o dono do recurso ou usuários autorizados possam acessar/alterar dados.

**Principais métodos:**
- `isClienteLogado(Long)`: Verifica se o cliente autenticado é o dono do recurso.
- `isProfissionalLogado(Long)`: Verifica se o profissional autenticado é o dono do recurso.
- `isAdminLogado(Long)`: Verifica se o admin autenticado é o dono do recurso.
- `isProprietarioLogado(Long)`: Verifica se o proprietário autenticado é o dono do recurso.

**Integrações:**
- Usado em anotações `@PreAuthorize` nos controllers para controle de acesso fino.
- Utiliza o contexto de autenticação do Spring para obter o usuário logado.

---

## Fluxo de Autenticação e Autorização

1. **Login:**
   - Usuário envia username e senha para `/api/auth/login`.
   - Se autenticado, um token JWT é gerado e retornado.
2. **Requisições subsequentes:**
   - O token JWT é enviado no header `Authorization`.
   - O filtro `JwtAuthenticationFilter` valida o token e configura o contexto de segurança.
   - Controllers usam `@PreAuthorize` e `SecurityService` para garantir que apenas usuários autorizados acessem os recursos.

---

## Padrões e Boas Práticas
- **JWT:** Stateless, não armazena sessão no servidor.
- **Roles:** Controle de acesso baseado em roles e propriedade.
- **UserDetails:** Abstração para detalhes do usuário autenticado.
- **Customização:** Fácil extensão para novas regras de autorização.
- **Segurança:** Tokens com expiração, validação de assinatura e roles embutidos.

---

## Próximos Passos Sugeridos
1. Implementar refresh token para sessões longas.
2. Adicionar logs de segurança para tentativas de acesso negado.
3. Expandir roles e permissões para cenários mais complexos.
4. Adicionar testes automatizados para regras de autorização customizadas. 