# Documentação da Pasta Exception (Tratamento de Exceções)

## Visão Geral

A pasta `exception` contém as exceções customizadas do sistema BeautyMatch, responsáveis por encapsular erros específicos do domínio da aplicação. O sistema utiliza uma abordagem estruturada para tratamento de exceções, combinando exceções customizadas com mensagens de erro localizadas.

## Estrutura da Pasta

```
src/main/java/com/beauty/com/MatchBeauty/exception/
└── AgendamentoException.java    # Exceção específica para agendamentos
```

## Padrões Utilizados

### 1. Exceções Customizadas
- Exceções específicas do domínio
- Herança de `RuntimeException` para exceções não verificadas
- Construtores com mensagem e causa

### 2. Tratamento de Exceções
- Tratamento local em controllers
- Mensagens de erro localizadas
- Logs de erro para depuração

### 3. Mensagens de Erro
- Arquivos de propriedades para internacionalização
- Mensagens específicas por contexto
- Suporte a múltiplos idiomas

## Exceções Detalhadas

### 1. AgendamentoException

**Arquivo**: `AgendamentoException.java`

**Responsabilidade**: Exceção específica para erros relacionados a agendamentos.

#### Estrutura
```java
public class AgendamentoException extends RuntimeException {
    
    public AgendamentoException(String message) {
        super(message);
    }
    
    public AgendamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

#### Características
- **Herança**: Estende `RuntimeException` (exceção não verificada)
- **Construtores**: Suporte a mensagem e causa
- **Uso**: Erros específicos de agendamento

#### Cenários de Uso
- Profissional indisponível no horário
- Conflito de horários
- Horário bloqueado
- Validações de agendamento

## Tratamento de Exceções no Sistema

### 1. Tratamento Local em Controllers

#### AgendamentoController
```java
@PostMapping
public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoDTO.Request request) {
    try {
        // Lógica de criação
    } catch (AgendamentoException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Erro interno ao criar agendamento: " + e.getMessage());
    }
}
```

#### Padrões de Tratamento
- **AgendamentoException**: Retorna 400 Bad Request
- **Exception genérica**: Retorna 500 Internal Server Error
- **Logs**: Impressão de stack trace para depuração

### 2. Uso em Services

#### AgendamentoService
```java
public Agendamento criarAgendamento(AgendamentoDTO.Request request) {
    // Validações
    if (!profissionalDisponivel) {
        throw new AgendamentoException("Profissional não está disponível no horário selecionado");
    }
    
    if (horarioBloqueado) {
        throw new AgendamentoException("Horário está bloqueado");
    }
    
    if (conflitoHorario) {
        throw new AgendamentoException("Existe conflito de horário com outro agendamento");
    }
}
```

#### HorarioTrabalhoService
```java
public void bloquearHorario(Usuario profissional, LocalDateTime dataHora) {
    if (conflitoAgendamentos) {
        throw new AgendamentoException("Existe conflito com agendamentos existentes neste horário");
    }
}
```

## Mensagens de Erro

### 1. Arquivo de Mensagens

**Arquivo**: `src/main/resources/messages.properties`

#### Estrutura de Mensagens
```properties
# Mensagens Gerais
message.generic.success=Operação realizada com sucesso
message.generic.error=Ocorreu um erro ao processar sua solicitação
message.generic.notFound=Recurso não encontrado
message.generic.unauthorized=Acesso não autorizado
message.generic.forbidden=Acesso proibido
message.generic.badRequest=Requisição inválida

# Mensagens de Agendamento
message.scheduling.created=Agendamento criado com sucesso
message.scheduling.updated=Agendamento atualizado com sucesso
message.scheduling.deleted=Agendamento removido com sucesso
message.scheduling.notFound=Agendamento não encontrado
message.scheduling.time.conflict=Horário já está ocupado
message.scheduling.invalid.date=Data inválida para agendamento
message.scheduling.past.date=Não é possível agendar para datas passadas

# Mensagens de Validação
message.validation.required=Campo obrigatório
message.validation.email=E-mail inválido
message.validation.password=Senha deve ter no mínimo 6 caracteres
```

### 2. Mensagens Específicas por Contexto

#### Autenticação
- Credenciais inválidas
- Token inválido ou expirado
- Acesso negado

#### Usuários
- Usuário não encontrado
- E-mail já cadastrado
- Papel de usuário inválido

#### Serviços
- Serviço não encontrado
- Nome do serviço já cadastrado

#### Agendamentos
- Horário já ocupado
- Data inválida para agendamento
- Não é possível agendar para datas passadas

## Padrões de Tratamento de Erro

### 1. Exceções Runtime vs Checked
- **RuntimeException**: Para erros de negócio (AgendamentoException)
- **Checked Exceptions**: Para erros de infraestrutura (IOException, SQLException)

### 2. Hierarquia de Exceções
```
RuntimeException
└── AgendamentoException
    ├── ProfissionalIndisponivelException (sugerido)
    ├── ConflitoHorarioException (sugerido)
    └── HorarioBloqueadoException (sugerido)
```

### 3. Tratamento por Camada
- **Controllers**: Tratamento de exceções HTTP
- **Services**: Lançamento de exceções de negócio
- **Repositories**: Tratamento de exceções de dados

## Exceções Utilizadas no Sistema

### 1. Exceções Customizadas
- **AgendamentoException**: Erros específicos de agendamento

### 2. Exceções Padrão Java
- **RuntimeException**: Erros genéricos de negócio
- **IllegalArgumentException**: Argumentos inválidos
- **UsernameNotFoundException**: Usuário não encontrado (Spring Security)

### 3. Exceções Spring
- **ServletException**: Erros de servlet
- **IOException**: Erros de entrada/saída

## Cenários de Erro Comuns

### 1. Agendamentos
```java
// Profissional indisponível
throw new AgendamentoException("Profissional não está disponível no horário selecionado");

// Conflito de horário
throw new AgendamentoException("Existe conflito de horário com outro agendamento");

// Horário bloqueado
throw new AgendamentoException("Horário está bloqueado");
```

### 2. Validações de Negócio
```java
// Salão duplicado
throw new RuntimeException("Já existe um salão com este nome e endereço");

// Profissional não encontrado
throw new RuntimeException("Profissional não encontrado");

// Username duplicado
throw new RuntimeException("Username já está em uso");
```

### 3. Validações de Entrada
```java
// Campos obrigatórios
throw new RuntimeException("Nome do salão é obrigatório");
throw new RuntimeException("Endereço do salão é obrigatório");

// Tipo de usuário inválido
throw new RuntimeException("Tipo de usuário inválido");
```

## Integrações

### 1. Com Controllers
- Tratamento de exceções HTTP
- Mapeamento de exceções para códigos de status
- Respostas de erro padronizadas

### 2. Com Services
- Lançamento de exceções de negócio
- Validações com exceções específicas
- Propagação de erros para controllers

### 3. Com Mensagens
- Internacionalização de mensagens
- Contexto específico de erro
- Suporte a múltiplos idiomas

## Considerações de Segurança

### 1. Exposição de Informações
- Evitar exposição de detalhes internos
- Mensagens genéricas para usuários finais
- Logs detalhados para administradores

### 2. Logs de Erro
- Registro de exceções para auditoria
- Informações sensíveis não logadas
- Rastreamento de erros

### 3. Validação de Entrada
- Validação antes do processamento
- Sanitização de dados
- Prevenção de ataques

## Próximos Passos e Melhorias

### 1. Implementações Sugeridas
- [ ] Criar GlobalExceptionHandler (@ControllerAdvice)
- [ ] Implementar exceções específicas por domínio
- [ ] Adicionar códigos de erro padronizados
- [ ] Implementar retry automático para exceções específicas

### 2. Melhorias de Arquitetura
- [ ] Criar hierarquia de exceções
- [ ] Implementar logging estruturado
- [ ] Adicionar métricas de erro
- [ ] Implementar circuit breaker

### 3. Exceções Específicas Sugeridas
- [ ] `UsuarioException` - Erros de usuário
- [ ] `SalaoException` - Erros de salão
- [ ] `ServicoException` - Erros de serviço
- [ ] `AutenticacaoException` - Erros de autenticação
- [ ] `ValidacaoException` - Erros de validação

### 4. Melhorias de Tratamento
- [ ] Implementar retry para exceções temporárias
- [ ] Adicionar fallback para serviços externos
- [ ] Implementar graceful degradation
- [ ] Adicionar health checks

### 5. Testes
- [ ] Testes unitários para exceções
- [ ] Testes de integração para tratamento
- [ ] Testes de cenários de erro
- [ ] Testes de performance com exceções

### 6. Monitoramento
- [ ] Alertas para exceções críticas
- [ ] Dashboards de erro
- [ ] Análise de tendências de erro
- [ ] Relatórios de qualidade

## Exemplo de Implementação Sugerida

### GlobalExceptionHandler
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AgendamentoException.class)
    public ResponseEntity<ErrorResponse> handleAgendamentoException(AgendamentoException e) {
        ErrorResponse error = new ErrorResponse("AGENDAMENTO_ERROR", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

### ErrorResponse
```java
public class ErrorResponse {
    private String code;
    private String message;
    private LocalDateTime timestamp;
    
    // Construtores, getters e setters
}
```

## Conclusão

A pasta `exception` implementa uma estrutura básica mas funcional para tratamento de exceções no sistema BeautyMatch. A exceção `AgendamentoException` atende às necessidades específicas de agendamentos, mas há oportunidades significativas de melhoria.

A estrutura atual suporta o tratamento básico de erros, mas pode ser aprimorada com a implementação de um sistema mais robusto de exceções, incluindo handlers globais, hierarquia de exceções e melhor monitoramento.

## Análise de Escalabilidade e Manutenibilidade

O sistema atual de tratamento de exceções do BeautyMatch demonstra uma abordagem funcional mas limitada. A presença de apenas uma exceção customizada (`AgendamentoException`) indica uma oportunidade significativa de melhoria.

**Pontos Fortes:**
- Exceção específica para agendamentos bem implementada
- Mensagens de erro localizadas e organizadas
- Tratamento local adequado em controllers
- Suporte a internacionalização

**Oportunidades de Melhoria:**
- Implementar GlobalExceptionHandler para centralizar tratamento
- Criar hierarquia de exceções específicas por domínio
- Adicionar códigos de erro padronizados
- Implementar logging estruturado e monitoramento
- Adicionar métricas e alertas para exceções críticas

A estrutura atual suporta as operações básicas, mas pode ser significativamente aprimorada para maior escalabilidade e manutenibilidade, especialmente em ambientes de produção com alta carga. 