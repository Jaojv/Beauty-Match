# Documentação da Pasta DTO (Data Transfer Objects)

## Visão Geral

A pasta `dto` contém os objetos de transferência de dados (DTOs) do sistema BeautyMatch. Os DTOs são responsáveis por encapsular os dados que são transferidos entre as camadas da aplicação, especialmente entre os controllers e os clientes (frontend/API consumers).

## Estrutura da Pasta

```
src/main/java/com/beauty/com/MatchBeauty/dto/
├── AgendamentoDTO.java          # DTOs para agendamentos (Request/Response)
├── SalaoDTO.java                # DTOs para salões (Request/Response)
├── ServicoDTO.java              # DTOs para serviços (Request/Response)
├── ProfissionalDTO.java         # DTOs para profissionais (Request/Response)
├── UsuarioDTO.java              # DTOs para usuários (Request/Response)
├── ClienteDTO.java              # DTO para clientes
├── ProprietarioDTO.java         # DTO para proprietários
├── AdminDTO.java                # DTO para administradores
├── LoginRequest.java            # DTO para requisições de login
├── LoginResponse.java           # DTO para respostas de login
└── RegistroRequest.java         # DTO para requisições de registro
```

## Padrões Utilizados

### 1. Padrão Request/Response
Muitos DTOs seguem o padrão de classes internas `Request` e `Response`:
- **Request**: Dados enviados pelo cliente para criar/atualizar recursos
- **Response**: Dados retornados pelo servidor após processamento

### 2. Mapeamento de Entidades
Os DTOs incluem métodos de conversão para mapear entre entidades JPA e DTOs:
- `fromEntity()`: Converte entidade para DTO
- Construtores que recebem entidades

### 3. Separação de Responsabilidades
- DTOs específicos para cada tipo de operação
- Evita exposição desnecessária de dados sensíveis
- Controla a estrutura dos dados de entrada e saída

## DTOs Detalhados

### 1. AgendamentoDTO

**Arquivo**: `AgendamentoDTO.java`

**Responsabilidade**: Transferência de dados para operações de agendamento.

#### Request
```java
public static class Request {
    private LocalDateTime dataHora;
    private Long clienteId;
    private Long profissionalId;
    private Long servicoId;
    private Long salaoId;
    private String observacoes;
}
```

**Campos**:
- `dataHora`: Data e hora do agendamento
- `clienteId`: ID do cliente que está fazendo o agendamento
- `profissionalId`: ID do profissional escolhido
- `servicoId`: ID do serviço solicitado
- `salaoId`: ID do salão onde será realizado
- `observacoes`: Observações adicionais do cliente

#### Response
```java
public static class Response {
    private Long id;
    private LocalDateTime dataHora;
    private Agendamento.StatusAgendamento status;
    private ClienteDTO cliente;
    private UsuarioDTO.Response profissional;
    private ServicoDTO.Response servico;
    private SalaoDTO.Response salao;
    private String observacoes;
    private Double valorServico;
}
```

**Características**:
- Inclui objetos completos (cliente, profissional, serviço, salão)
- Calcula automaticamente o valor do serviço
- Método `fromEntity()` para conversão de entidade

### 2. SalaoDTO

**Arquivo**: `SalaoDTO.java`

**Responsabilidade**: Transferência de dados para operações de salão.

#### Request
```java
public static class Request {
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String descricao;
    private String horarioFuncionamento;
    private Long proprietarioId;
}
```

#### Response
```java
public static class Response {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
    private String descricao;
    private String horarioFuncionamento;
    private UsuarioDTO.Response proprietario;
    private List<ServicoDTO.Response> servicos;
}
```

**Características**:
- Inclui lista de serviços oferecidos
- Inclui dados do proprietário
- Suporte a horário de funcionamento

### 3. ServicoDTO

**Arquivo**: `ServicoDTO.java`

**Responsabilidade**: Transferência de dados para operações de serviço.

#### DTO Principal
```java
public class ServicoDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;
    private Long salaoId;
    private List<Long> profissionaisIds;
}
```

#### Response
```java
public static class Response {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracaoMinutos;
    private Long salaoId;
}
```

**Características**:
- Suporte a múltiplos profissionais por serviço
- Inclui duração em minutos
- Preço como BigDecimal para precisão

### 4. ProfissionalDTO

**Arquivo**: `ProfissionalDTO.java`

**Responsabilidade**: Transferência de dados para operações de profissional.

#### Request
```java
public static class Request {
    private String username;
    private String password;
    private String email;
    private String telefone;
    private String nome;
    private String cpf;
    private String especialidade;
    private String biografia;
    private Long salaoId;
}
```

#### Response
```java
public static class Response {
    private Long idUsuario;
    private String username;
    private String email;
    private String telefone;
    private String nome;
    private String cpf;
    private String especialidade;
    private String biografia;
    private SalaoResumoDTO salao;
}
```

#### SalaoResumoDTO
```java
public static class SalaoResumoDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String email;
}
```

**Características**:
- Inclui dados do salão vinculado
- DTO resumido do salão para evitar recursão
- Construtor que recebe entidade Profissional

### 5. UsuarioDTO

**Arquivo**: `UsuarioDTO.java`

**Responsabilidade**: Transferência de dados genéricos de usuário.

#### Request
```java
public static class Request {
    private String username;
    private String password;
    private String nome;
    private String email;
    private String telefone;
    private TipoUsuario tipoUsuario;
}
```

#### Response
```java
public static class Response {
    private Long clienteId;
    private String username;
    private String nome;
    private String email;
    private String telefone;
    private TipoUsuario tipoUsuario;
}
```

**Características**:
- Usa Lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
- Suporte a diferentes tipos de usuário
- Construtor específico para conversão

### 6. ClienteDTO

**Arquivo**: `ClienteDTO.java`

**Responsabilidade**: Transferência de dados específicos de cliente.

```java
public class ClienteDTO {
    private String username;
    private String password;
    private String email;
    private String telefone;
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String endereco;
    private String preferencias;
    private Long clienteId;
}
```

**Características**:
- Dados específicos de cliente (CPF, data nascimento, preferências)
- Campo clienteId para identificação
- Sem separação Request/Response

### 7. ProprietarioDTO

**Arquivo**: `ProprietarioDTO.java`

**Responsabilidade**: Transferência de dados específicos de proprietário.

```java
public class ProprietarioDTO {
    private String username;
    private String password;
    private String email;
    private String telefone;
    private String nome;
    private String cnpj;
    private String razaoSocial;
    private String endereco;
    private String horarioFuncionamento;
}
```

**Características**:
- Dados específicos de proprietário (CNPJ, razão social)
- Usa Lombok (@Data)
- Inclui horário de funcionamento

### 8. AdminDTO

**Arquivo**: `AdminDTO.java`

**Responsabilidade**: Transferência de dados específicos de administrador.

```java
public class AdminDTO {
    private String username;
    private String password;
    private String email;
    private String telefone;
    private String nome;
    private String nivelAcesso;
}
```

**Características**:
- Dados específicos de admin (nível de acesso)
- Estrutura simples sem separação Request/Response

### 9. LoginRequest

**Arquivo**: `LoginRequest.java`

**Responsabilidade**: Dados para autenticação de usuário.

```java
public class LoginRequest {
    private String username;
    private String password;
}
```

**Características**:
- Estrutura mínima para login
- Apenas credenciais necessárias

### 10. LoginResponse

**Arquivo**: `LoginResponse.java`

**Responsabilidade**: Resposta após autenticação bem-sucedida.

```java
public class LoginResponse {
    private String token;
    private Long idUsuario;
    private String username;
    private String tipoUsuario;
}
```

**Características**:
- Inclui token JWT
- Dados básicos do usuário autenticado
- Tipo de usuário para controle de acesso

### 11. RegistroRequest

**Arquivo**: `RegistroRequest.java`

**Responsabilidade**: Dados para registro de novo usuário.

```java
public class RegistroRequest {
    private String username;
    private String password;
    private String nome;
    private String email;
    private String tipoUsuario;
}
```

**Características**:
- Dados básicos para registro
- Tipo de usuário como string
- Estrutura simplificada

## Integrações

### 1. Com Controllers
- Os DTOs são utilizados como parâmetros e retornos dos endpoints
- Validação de entrada através dos DTOs
- Conversão automática de JSON para DTOs

### 2. Com Services
- Conversão de DTOs para entidades antes do processamento
- Conversão de entidades para DTOs após processamento
- Mapeamento de dados entre camadas

### 3. Com Entidades
- Métodos de conversão (`fromEntity()`)
- Construtores que recebem entidades
- Mapeamento de relacionamentos

## Validações

### 1. Validações de Entrada
- Campos obrigatórios
- Formatos de dados (email, CPF, CNPJ)
- Tamanhos de campos
- Validações de negócio

### 2. Validações de Segurança
- Não exposição de senhas em responses
- Filtragem de dados sensíveis
- Controle de acesso a informações

## Padrões de Nomenclatura

### 1. Classes
- `[Entidade]DTO`: DTO principal da entidade
- `Request`: Classe interna para dados de entrada
- `Response`: Classe interna para dados de saída
- `ResumoDTO`: DTO simplificado para evitar recursão

### 2. Métodos
- `fromEntity()`: Conversão de entidade para DTO
- `toEntity()`: Conversão de DTO para entidade (quando aplicável)

## Considerações de Performance

### 1. Lazy Loading
- DTOs evitam carregamento desnecessário de relacionamentos
- Dados resumidos para listagens
- Dados completos apenas quando necessário

### 2. Serialização
- Estruturas otimizadas para JSON
- Evita referências circulares
- Controle de profundidade de objetos

## Próximos Passos e Melhorias

### 1. Implementações Sugeridas
- [ ] Adicionar validações Bean Validation (@NotNull, @Email, etc.)
- [ ] Implementar DTOs para operações de busca/filtro
- [ ] Criar DTOs para operações em lote
- [ ] Adicionar versionamento de DTOs

### 2. Melhorias de Arquitetura
- [ ] Implementar mapeamento automático (MapStruct)
- [ ] Criar DTOs para operações de relatório
- [ ] Adicionar suporte a paginação
- [ ] Implementar cache de DTOs

### 3. Testes
- [ ] Testes unitários para conversões
- [ ] Testes de validação
- [ ] Testes de serialização/deserialização
- [ ] Testes de integração com controllers

### 4. Documentação
- [ ] Adicionar anotações de documentação (Swagger)
- [ ] Criar exemplos de uso
- [ ] Documentar regras de negócio
- [ ] Criar guias de migração

## Conclusão

A pasta `dto` implementa uma arquitetura robusta para transferência de dados, seguindo boas práticas de separação de responsabilidades e encapsulamento. Os DTOs garantem que apenas os dados necessários sejam expostos, mantendo a segurança e performance do sistema.

A estrutura atual suporta todas as operações principais do sistema BeautyMatch, mas há oportunidades de melhoria através da implementação de validações mais robustas, mapeamento automático e otimizações de performance. 