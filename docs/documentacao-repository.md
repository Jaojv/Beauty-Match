# Documentação - Pasta Repository

## Visão Geral

A pasta `repository` contém todas as interfaces de acesso a dados do sistema BeautyMatch, utilizando Spring Data JPA. Cada repositório estende `JpaRepository` e fornece métodos para operações CRUD básicas, além de consultas personalizadas específicas para cada entidade.

## Arquitetura dos Repositórios

### Base Tecnológica
- **Spring Data JPA**: Framework para simplificar o acesso a dados
- **JpaRepository**: Interface base que fornece operações CRUD padrão
- **Query Methods**: Métodos de consulta baseados em convenções de nomenclatura
- **Anotação @Repository**: Identifica as interfaces como componentes de persistência

### Estrutura Padrão
```java
@Repository
public interface [EntityName]Repository extends JpaRepository<[Entity], Long> {
    // Métodos de consulta personalizados
}
```

## Repositórios de Usuários

### 1. UsuarioRepository
**Arquivo:** `UsuarioRepository.java`

**Descrição:** Repositório base para todas as entidades de usuário, fornecendo acesso às funcionalidades comuns.

**Métodos Herdados (JpaRepository):**
- `save(Usuario)`: Salva ou atualiza um usuário
- `findById(Long)`: Busca usuário por ID
- `findAll()`: Lista todos os usuários
- `delete(Usuario)`: Remove um usuário
- `count()`: Conta total de usuários

**Métodos Personalizados:**
```java
Optional<Usuario> findByUsername(String username);
```
- **Propósito:** Busca usuário pelo nome de usuário (login)
- **Retorno:** Optional para tratamento seguro de valores nulos
- **Uso:** Autenticação e validação de usuários

**Casos de Uso:**
- Autenticação de usuários
- Validação de username único
- Busca de usuários para login

### 2. ClienteRepository
**Arquivo:** `ClienteRepository.java`

**Descrição:** Repositório específico para entidades Cliente, herda funcionalidades básicas do JpaRepository.

**Métodos Disponíveis:**
- Todos os métodos CRUD padrão do JpaRepository
- Consultas específicas para clientes podem ser adicionadas conforme necessário

**Casos de Uso:**
- Gerenciamento de cadastros de clientes
- Listagem de clientes
- Operações CRUD básicas

### 3. ProfissionalRepository
**Arquivo:** `ProfissionalRepository.java`

**Descrição:** Repositório para entidades Profissional, preparado para consultas específicas de profissionais.

**Métodos Disponíveis:**
- Todos os métodos CRUD padrão do JpaRepository
- Estrutura preparada para métodos específicos de profissionais

**Casos de Uso:**
- Gerenciamento de profissionais
- Busca de profissionais por salão
- Consultas por especialidade (futuro)

### 4. ProprietarioRepository
**Arquivo:** `ProprietarioRepository.java`

**Descrição:** Repositório para entidades Proprietario, fornecendo acesso aos dados dos proprietários de salões.

**Métodos Disponíveis:**
- Todos os métodos CRUD padrão do JpaRepository

**Casos de Uso:**
- Gerenciamento de proprietários
- Validação de CNPJ
- Consultas por razão social

### 5. AdminRepository
**Arquivo:** `AdminRepository.java`

**Descrição:** Repositório para entidades Admin, gerenciando administradores do sistema.

**Métodos Disponíveis:**
- Todos os métodos CRUD padrão do JpaRepository

**Casos de Uso:**
- Gerenciamento de administradores
- Controle de níveis de acesso
- Auditoria administrativa

## Repositórios de Negócio

### 6. SalaoRepository
**Arquivo:** `SalaoRepository.java`

**Descrição:** Repositório para entidades Salao, com consultas específicas para gerenciamento de estabelecimentos.

**Métodos Personalizados:**
```java
List<Salao> findByProprietarioIdUsuario(Long proprietarioId);
Optional<Salao> findByNomeAndEndereco(String nome, String endereco);
boolean existsByNomeAndEndereco(String nome, String endereco);
```

**Detalhamento dos Métodos:**

**`findByProprietarioIdUsuario(Long proprietarioId)`**
- **Propósito:** Busca todos os salões de um proprietário específico
- **Parâmetros:** ID do proprietário
- **Retorno:** Lista de salões
- **Uso:** Dashboard do proprietário, listagem de estabelecimentos

**`findByNomeAndEndereco(String nome, String endereco)`**
- **Propósito:** Busca salão específico por nome e endereço
- **Parâmetros:** Nome e endereço do salão
- **Retorno:** Optional<Salao>
- **Uso:** Validação de duplicatas, busca específica

**`existsByNomeAndEndereco(String nome, String endereco)`**
- **Propósito:** Verifica se existe salão com nome e endereço específicos
- **Parâmetros:** Nome e endereço do salão
- **Retorno:** Boolean
- **Uso:** Validação antes de criar novo salão

**Casos de Uso:**
- Cadastro e edição de salões
- Validação de duplicatas
- Dashboard do proprietário
- Busca de estabelecimentos

### 7. ServicoRepository
**Arquivo:** `ServicoRepository.java`

**Descrição:** Repositório para entidades Servico, gerenciando os serviços oferecidos pelos salões.

**Métodos Personalizados:**
```java
List<Servico> findBySalaoId(Long salaoId);
Optional<Servico> findByNomeAndSalaoId(String nome, Long salaoId);
boolean existsByNomeAndSalaoId(String nome, Long salaoId);
```

**Detalhamento dos Métodos:**

**`findBySalaoId(Long salaoId)`**
- **Propósito:** Lista todos os serviços de um salão específico
- **Parâmetros:** ID do salão
- **Retorno:** Lista de serviços
- **Uso:** Catálogo de serviços do salão

**`findByNomeAndSalaoId(String nome, Long salaoId)`**
- **Propósito:** Busca serviço específico por nome dentro de um salão
- **Parâmetros:** Nome do serviço e ID do salão
- **Retorno:** Optional<Servico>
- **Uso:** Validação de duplicatas por salão

**`existsByNomeAndSalaoId(String nome, Long salaoId)`**
- **Propósito:** Verifica se existe serviço com nome específico no salão
- **Parâmetros:** Nome do serviço e ID do salão
- **Retorno:** Boolean
- **Uso:** Validação antes de criar novo serviço

**Casos de Uso:**
- Gerenciamento de catálogo de serviços
- Validação de duplicatas por salão
- Listagem de serviços disponíveis
- Controle de preços e duração

## Repositório de Agendamentos

### 8. AgendamentoRepository
**Arquivo:** `AgendamentoRepository.java`

**Descrição:** Repositório mais complexo do sistema, gerenciando agendamentos com múltiplas consultas por diferentes critérios.

**Métodos Personalizados:**

#### Consultas por Entidade
```java
List<Agendamento> findByClienteIdUsuario(Long clienteId);
List<Agendamento> findByProfissionalIdUsuario(Long profissionalId);
List<Agendamento> findBySalaoId(Long salaoId);
```

#### Consultas por Status
```java
List<Agendamento> findByStatus(StatusAgendamento status);
```

#### Consultas por Período
```java
List<Agendamento> findByClienteIdUsuarioAndDataHoraBetween(
    Long clienteId, LocalDateTime inicio, LocalDateTime fim);
List<Agendamento> findByProfissionalIdUsuarioAndDataHoraBetween(
    Long profissionalId, LocalDateTime inicio, LocalDateTime fim);
List<Agendamento> findBySalaoIdAndDataHoraBetween(
    Long salaoId, LocalDateTime inicio, LocalDateTime fim);
```

#### Consultas Combinadas (Status + Período)
```java
List<Agendamento> findByClienteIdUsuarioAndStatusAndDataHoraBetween(
    Long clienteId, StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim);
List<Agendamento> findByProfissionalIdUsuarioAndStatusAndDataHoraBetween(
    Long profissionalId, StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim);
List<Agendamento> findBySalaoIdAndStatusAndDataHoraBetween(
    Long salaoId, StatusAgendamento status, LocalDateTime inicio, LocalDateTime fim);
```

**Casos de Uso por Método:**

**Consultas por Entidade:**
- **Cliente:** Histórico de agendamentos do cliente
- **Profissional:** Agenda do profissional
- **Salão:** Todos os agendamentos do estabelecimento

**Consultas por Status:**
- **AGENDADO:** Agendamentos pendentes
- **CONCLUIDO:** Histórico de serviços realizados
- **CANCELADO:** Agendamentos cancelados
- **FALTANTE:** Clientes que não compareceram

**Consultas por Período:**
- Relatórios mensais/semanais
- Análise de ocupação
- Estatísticas de agendamentos

**Consultas Combinadas:**
- Relatórios específicos por status e período
- Análise de performance por profissional
- Estatísticas de cancelamentos

## Repositórios de Horários

### 9. HorarioTrabalhoRepository
**Arquivo:** `HorarioTrabalhoRepository.java`

**Descrição:** Gerencia os horários de trabalho dos profissionais, essencial para verificação de disponibilidade.

**Métodos Personalizados:**
```java
List<HorarioTrabalho> findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue(
    Long profissionalId, DayOfWeek diaSemana);
List<HorarioTrabalho> findByProfissionalIdUsuarioAndAtivoTrue(Long profissionalId);
```

**Detalhamento dos Métodos:**

**`findByProfissionalIdUsuarioAndDiaSemanaAndAtivoTrue`**
- **Propósito:** Busca horários de trabalho de um profissional em um dia específico
- **Parâmetros:** ID do profissional e dia da semana
- **Retorno:** Lista de horários ativos
- **Uso:** Verificação de disponibilidade para agendamento

**`findByProfissionalIdUsuarioAndAtivoTrue`**
- **Propósito:** Lista todos os horários ativos de um profissional
- **Parâmetros:** ID do profissional
- **Retorno:** Lista de horários ativos
- **Uso:** Visualização completa da agenda do profissional

**Casos de Uso:**
- Verificação de disponibilidade para agendamentos
- Configuração de horários de trabalho
- Geração de slots disponíveis
- Controle de agenda profissional

### 10. HorarioFuncionamentoSalaoRepository
**Arquivo:** `HorarioFuncionamentoSalaoRepository.java`

**Descrição:** Gerencia os horários de funcionamento dos salões, base para geração de slots disponíveis.

**Métodos Personalizados:**
```java
List<HorarioFuncionamentoSalao> findBySalaoIdAndAtivoTrue(Long salaoId);
List<HorarioFuncionamentoSalao> findBySalaoIdAndDiaSemanaAndAtivoTrue(
    Long salaoId, DayOfWeek diaSemana);
List<HorarioFuncionamentoSalao> findBySalaoId(Long salaoId);
void deleteBySalaoId(Long salaoId);
```

**Detalhamento dos Métodos:**

**`findBySalaoIdAndAtivoTrue`**
- **Propósito:** Lista horários de funcionamento ativos de um salão
- **Parâmetros:** ID do salão
- **Retorno:** Lista de horários ativos
- **Uso:** Configuração de agenda do salão

**`findBySalaoIdAndDiaSemanaAndAtivoTrue`**
- **Propósito:** Busca horário de funcionamento para um dia específico
- **Parâmetros:** ID do salão e dia da semana
- **Retorno:** Lista de horários ativos
- **Uso:** Verificação de funcionamento em dia específico

**`findBySalaoId`**
- **Propósito:** Lista todos os horários de funcionamento (ativos e inativos)
- **Parâmetros:** ID do salão
- **Retorno:** Lista completa de horários
- **Uso:** Gerenciamento completo de horários

**`deleteBySalaoId`**
- **Propósito:** Remove todos os horários de funcionamento de um salão
- **Parâmetros:** ID do salão
- **Retorno:** void
- **Uso:** Limpeza de dados ao remover salão

**Casos de Uso:**
- Configuração de horários de funcionamento
- Geração automática de slots disponíveis
- Validação de agendamentos
- Gerenciamento de horários especiais

### 11. HorarioBloqueadoRepository
**Arquivo:** `HorarioBloqueadoRepository.java`

**Descrição:** Gerencia horários bloqueados para profissionais, permitindo controle de indisponibilidades.

**Métodos Personalizados:**
```java
List<HorarioBloqueado> findBySalaoId(Long salaoId);
List<HorarioBloqueado> findBySalaoIdAndDataHoraInicioBetween(
    Long salaoId, LocalDateTime inicio, LocalDateTime fim);
```

**Detalhamento dos Métodos:**

**`findBySalaoId`**
- **Propósito:** Lista todos os horários bloqueados de um salão
- **Parâmetros:** ID do salão
- **Retorno:** Lista de horários bloqueados
- **Uso:** Visualização de bloqueios do salão

**`findBySalaoIdAndDataHoraInicioBetween`**
- **Propósito:** Busca horários bloqueados em um período específico
- **Parâmetros:** ID do salão, data/hora início e fim
- **Retorno:** Lista de horários bloqueados no período
- **Uso:** Verificação de conflitos para agendamentos

**Casos de Uso:**
- Controle de férias e ausências
- Bloqueio de horários para manutenção
- Gestão de indisponibilidades temporárias
- Prevenção de agendamentos conflitantes

## Padrões de Consulta Utilizados

### 1. Query Methods por Convenção
- **findBy[Property]**: Busca por propriedade específica
- **findBy[Property]And[Property]**: Busca com múltiplos critérios
- **findBy[Property]Between**: Busca em intervalo
- **existsBy[Property]**: Verifica existência
- **deleteBy[Property]**: Remove por critério

### 2. Relacionamentos
- **findBy[Entity]Id**: Busca por ID de entidade relacionada
- **findBy[Entity]Id[Property]**: Busca por propriedade de entidade relacionada

### 3. Condicionais
- **And**: Combina múltiplos critérios
- **Or**: Alternativa entre critérios
- **Between**: Intervalo de valores
- **True/False**: Filtros booleanos

## Considerações de Performance

### 1. Índices Recomendados
```sql
-- Usuario
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_email ON usuario(email);

-- Agendamento
CREATE INDEX idx_agendamento_cliente_data ON agendamento(cliente_id_usuario, data_hora);
CREATE INDEX idx_agendamento_profissional_data ON agendamento(profissional_id_usuario, data_hora);
CREATE INDEX idx_agendamento_salao_data ON agendamento(salao_id, data_hora);
CREATE INDEX idx_agendamento_status ON agendamento(status);

-- Horários
CREATE INDEX idx_horario_trabalho_profissional_dia ON horario_trabalho(profissional_id_usuario, dia_semana);
CREATE INDEX idx_horario_funcionamento_salao_dia ON horario_funcionamento_salao(salao_id, dia_semana);
```

### 2. Paginação
- Implementar `Pageable` para consultas que retornam grandes volumes
- Usar `@Query` com `LIMIT` para consultas específicas

### 3. Cache
- Considerar cache para consultas frequentes (horários de funcionamento)
- Implementar cache de segundo nível para entidades estáticas

## Próximos Passos Sugeridos

### 1. Otimizações
- Adicionar métodos de consulta com `@Query` para consultas complexas
- Implementar paginação em consultas de listagem
- Adicionar índices para campos frequentemente consultados

### 2. Funcionalidades
- Métodos de busca por texto (nome, descrição)
- Consultas de relatórios e estatísticas
- Métodos de agregação (count, sum, avg)

### 3. Validações
- Adicionar validações de negócio nos repositórios
- Implementar verificações de integridade referencial
- Adicionar logs para operações críticas

### 4. Documentação
- Adicionar JavaDoc para métodos complexos
- Documentar casos de uso específicos
- Criar exemplos de consultas

## Resumo da Arquitetura

```
Repository Layer
├── Usuários
│   ├── UsuarioRepository (base)
│   ├── ClienteRepository
│   ├── ProfissionalRepository
│   ├── ProprietarioRepository
│   └── AdminRepository
├── Negócio
│   ├── SalaoRepository
│   ├── ServicoRepository
│   └── AgendamentoRepository (mais complexo)
└── Horários
    ├── HorarioTrabalhoRepository
    ├── HorarioFuncionamentoSalaoRepository
    └── HorarioBloqueadoRepository
```

A camada de repositório fornece uma abstração limpa para acesso a dados, seguindo os princípios do Spring Data JPA e facilitando a implementação de consultas complexas através de convenções de nomenclatura. 