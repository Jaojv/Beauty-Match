# Documentação - Pasta Entity

## Visão Geral

A pasta `entity` contém todas as entidades JPA do sistema BeautyMatch, representando o modelo de dados do banco de dados. O sistema utiliza herança com estratégia `JOINED` para implementar diferentes tipos de usuários, além de entidades para gerenciar salões, serviços, agendamentos e horários.

## Estrutura de Herança de Usuários

### 1. Usuario (Classe Base)
**Arquivo:** `Usuario.java`

**Descrição:** Classe base para todos os tipos de usuários do sistema, implementando `UserDetails` do Spring Security.

**Características:**
- **Estratégia de Herança:** `JOINED` - cada subtipo tem sua própria tabela
- **Segurança:** Implementa `UserDetails` para autenticação Spring Security
- **Auditoria:** Campos `criadoEm` e `atualizadoEm` para rastreamento temporal

**Atributos Principais:**
- `idUsuario` (Long, PK): Identificador único
- `username` (String, unique): Nome de usuário para login
- `password` (String): Senha criptografada
- `email` (String, unique): Email do usuário
- `nome` (String): Nome completo
- `telefone` (String): Número de telefone
- `tipoUsuario` (TipoUsuario): Enum que define o tipo (ADMIN, CLIENTE, PROFISSIONAL, PROPRIETARIO)
- `criadoEm` (LocalDateTime): Data/hora de criação
- `atualizadoEm` (LocalDateTime): Data/hora da última atualização

**Enum TipoUsuario:**
```java
public enum TipoUsuario {
    ADMIN,
    CLIENTE,
    PROFISSIONAL,
    PROPRIETARIO
}
```

### 2. Cliente
**Arquivo:** `Cliente.java`

**Descrição:** Representa os clientes que fazem agendamentos nos salões.

**Atributos Específicos:**
- `cpf` (String): CPF do cliente
- `dataNascimento` (String): Data de nascimento
- `endereco` (String): Endereço residencial
- `preferencias` (String): Preferências de serviços/estilo

**Relacionamentos:**
- Herda de `Usuario`
- Relacionado com `Agendamento` (cliente que faz o agendamento)

### 3. Profissional
**Arquivo:** `Profissional.java`

**Descrição:** Representa os profissionais que prestam serviços nos salões.

**Atributos Específicos:**
- `cpf` (String): CPF do profissional
- `especialidade` (String): Área de especialização
- `biografia` (String): Descrição profissional
- `salao` (Salao): Salão onde trabalha

**Relacionamentos:**
- Herda de `Usuario`
- `@ManyToOne` com `Salao` (profissional trabalha em um salão)
- Relacionado com `Agendamento` (profissional que atende)
- Relacionado com `HorarioTrabalho` (horários disponíveis)
- Relacionado com `HorarioBloqueado` (horários indisponíveis)

### 4. Proprietario
**Arquivo:** `Proprietario.java`

**Descrição:** Representa os proprietários dos salões.

**Atributos Específicos:**
- `cnpj` (String): CNPJ da empresa
- `razaoSocial` (String): Razão social da empresa
- `endereco` (String): Endereço comercial
- `horarioFuncionamento` (String): Horários de funcionamento (legado)

**Relacionamentos:**
- Herda de `Usuario`
- Relacionado com `Salao` (proprietário do salão)

**Métodos Específicos:**
- `atualizarDados()`: Atualiza informações básicas do proprietário

### 5. Admin
**Arquivo:** `Admin.java`

**Descrição:** Representa administradores do sistema.

**Atributos Específicos:**
- `nivelAcesso` (String): Nível de acesso administrativo

**Relacionamentos:**
- Herda de `Usuario`

### 6. Administrador
**Arquivo:** `Administrador.java`

**Descrição:** Classe alternativa para administradores (parece ser uma versão simplificada).

**Relacionamentos:**
- Herda de `Usuario`
- Sem atributos específicos adicionais

## Entidades de Negócio

### 7. Salao
**Arquivo:** `Salao.java`

**Descrição:** Representa os estabelecimentos de beleza.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `nome` (String): Nome do salão
- `endereco` (String): Endereço físico
- `telefone` (String): Telefone de contato
- `email` (String): Email de contato
- `descricao` (String): Descrição do estabelecimento

**Relacionamentos:**
- `@ManyToOne` com `Usuario` (proprietario)
- `@OneToMany` com `HorarioFuncionamentoSalao` (horários de funcionamento)
- `@OneToMany` com `Servico` (serviços oferecidos)
- `@OneToMany` com `Agendamento` (agendamentos realizados)
- `@OneToMany` com `Profissional` (profissionais que trabalham)

### 8. Servico
**Arquivo:** `Servico.java`

**Descrição:** Representa os serviços oferecidos pelos salões.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `nome` (String): Nome do serviço
- `descricao` (String): Descrição detalhada
- `preco` (BigDecimal): Valor do serviço
- `duracaoMinutos` (Integer): Duração em minutos
- `imagem` (String): URL da imagem do serviço
- `ativo` (Boolean): Status de disponibilidade

**Relacionamentos:**
- `@ManyToOne` com `Salao` (salão que oferece o serviço)
- `@OneToMany` com `Agendamento` (agendamentos para este serviço)

### 9. Agendamento
**Arquivo:** `Agendamento.java`

**Descrição:** Representa os agendamentos de serviços.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `dataHora` (LocalDateTime): Data e hora do agendamento
- `observacoes` (String): Observações adicionais
- `status` (StatusAgendamento): Status atual do agendamento

**Relacionamentos:**
- `@ManyToOne` com `Usuario` (cliente que fez o agendamento)
- `@ManyToOne` com `Usuario` (profissional que atenderá)
- `@ManyToOne` com `Servico` (serviço agendado)
- `@ManyToOne` com `Salao` (salão onde será realizado)

**Enum StatusAgendamento:**
```java
public enum StatusAgendamento {
    AGENDADO,
    CONCLUIDO,
    CANCELADO,
    FALTANTE
}
```

## Entidades de Horários

### 10. HorarioFuncionamentoSalao
**Arquivo:** `HorarioFuncionamentoSalao.java`

**Descrição:** Define os horários de funcionamento de cada salão por dia da semana.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `salao` (Salao): Salão referenciado
- `diaSemana` (DayOfWeek): Dia da semana
- `horaInicio` (LocalTime): Horário de início
- `horaFim` (LocalTime): Horário de encerramento
- `ativo` (Boolean): Status de disponibilidade

**Relacionamentos:**
- `@ManyToOne` com `Salao` (salão que define o horário)

### 11. HorarioTrabalho
**Arquivo:** `HorarioTrabalho.java`

**Descrição:** Define os horários de trabalho de cada profissional por dia da semana.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `profissional` (Usuario): Profissional referenciado
- `diaSemana` (DayOfWeek): Dia da semana
- `horaInicio` (LocalTime): Horário de início
- `horaFim` (LocalTime): Horário de encerramento
- `ativo` (Boolean): Status de disponibilidade
- `bloqueado` (Boolean): Indica se o horário está bloqueado
- `observacoes` (String): Observações sobre o horário

**Relacionamentos:**
- `@ManyToOne` com `Usuario` (profissional que define o horário)

### 12. HorarioBloqueado
**Arquivo:** `HorarioBloqueado.java`

**Descrição:** Representa horários específicos bloqueados para profissionais.

**Atributos Principais:**
- `id` (Long, PK): Identificador único
- `profissional` (Profissional): Profissional com horário bloqueado
- `salao` (Salao): Salão onde o bloqueio ocorre
- `dataHoraInicio` (LocalDateTime): Início do bloqueio
- `dataHoraFim` (LocalDateTime): Fim do bloqueio

**Relacionamentos:**
- `@ManyToOne` com `Profissional` (profissional bloqueado)
- `@ManyToOne` com `Salao` (salão onde ocorre o bloqueio)

## Enums

### 13. StatusAgendamento
**Arquivo:** `StatusAgendamento.java`

**Descrição:** Enum que define os possíveis status de um agendamento.

**Valores:**
- `AGENDADO`: Agendamento confirmado
- `CONCLUIDO`: Serviço realizado
- `CANCELADO`: Agendamento cancelado
- `FALTANTE`: Cliente não compareceu

### 14. Role
**Arquivo:** `Role.java`

**Descrição:** Enum que define os papéis/roles dos usuários no sistema.

**Valores:**
- `ADMIN`: Administrador do sistema
- `PROPRIETARIO`: Proprietário de salão
- `PROFISSIONAL`: Profissional de beleza
- `CLIENTE`: Cliente que faz agendamentos

## Diagrama de Relacionamentos

```
Usuario (Base)
├── Cliente
├── Profissional ────┐
├── Proprietario     │
└── Admin            │
                     │
Salao ───────────────┼─── Servico ──── Agendamento
│                     │
HorarioFuncionamento  │
Salao                 │
                      │
HorarioTrabalho ──────┘
│
HorarioBloqueado ─────┘
```

## Considerações de Design

### 1. Herança com JOINED
- Cada subtipo de usuário tem sua própria tabela
- Permite flexibilidade para adicionar campos específicos
- Facilita consultas específicas por tipo de usuário

### 2. Segurança Integrada
- Implementação de `UserDetails` na classe base
- Suporte nativo ao Spring Security
- Controle de autorização baseado em roles

### 3. Auditoria Temporal
- Campos de criação e atualização automáticos
- Rastreamento de mudanças nas entidades

### 4. Relacionamentos Bidirecionais
- Mapeamento completo entre entidades relacionadas
- Facilita navegação entre objetos

### 5. Enums para Status
- Controle de estado através de enums
- Facilita validações e transições de estado

## Próximos Passos Sugeridos

1. **Validações:** Adicionar anotações de validação (@NotNull, @Size, etc.)
2. **Índices:** Definir índices para campos frequentemente consultados
3. **Auditoria:** Implementar auditoria mais robusta com @EntityListeners
4. **Versionamento:** Adicionar controle de versão para entidades críticas
5. **Documentação:** Adicionar JavaDoc para métodos complexos 