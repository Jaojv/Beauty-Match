# Documentação - Pasta Controller

## Visão Geral

A pasta `controller` contém todos os controladores REST da API do sistema BeautyMatch. Estes controladores são responsáveis por receber requisições HTTP, validar dados de entrada, orquestrar chamadas para os serviços e retornar respostas apropriadas. Todos os controladores seguem o padrão REST e implementam autenticação/autorização baseada em roles.

---

## Estrutura Geral

### Base Tecnológica
- **Spring Web**: Framework para criação de APIs REST
- **Spring Security**: Controle de autenticação e autorização
- **Swagger/OpenAPI**: Documentação automática da API
- **Bean Validation**: Validação de dados de entrada
- **CORS**: Suporte a requisições cross-origin

### Padrões Utilizados
- **REST**: Endpoints seguem convenções RESTful
- **DTO Pattern**: Separação entre dados de entrada/saída e entidades
- **Role-based Authorization**: Controle de acesso baseado em roles
- **ResponseEntity**: Respostas HTTP padronizadas

---

## Controllers Principais

### 1. AuthController
**Base Path:** `/api/auth`

**Responsabilidade:** Gerenciar autenticação, login e registro de usuários.

#### Endpoints:

**POST `/api/auth/login`**
- **Descrição:** Realiza login do usuário
- **Body:** `LoginRequest` (username, password)
- **Response:** `LoginResponse` (token JWT, id, username, tipoUsuario)
- **Autenticação:** Não requerida
- **Autorização:** Pública

**POST `/api/auth/registro`**
- **Descrição:** Registra novo usuário no sistema
- **Body:** `RegistroRequest` (username, password, nome, email, tipoUsuario)
- **Response:** `LoginResponse` (token JWT após registro automático)
- **Autenticação:** Não requerida
- **Autorização:** Pública
- **Validações:** Campos obrigatórios, username único

#### Características:
- Gera tokens JWT automaticamente após login/registro
- Suporte a diferentes tipos de usuário (ADMIN, CLIENTE, PROFISSIONAL, PROPRIETARIO)
- Validação de credenciais e tratamento de erros
- Integração com Spring Security e JWT

---

### 2. AgendamentoController
**Base Path:** `/api/agendamentos`

**Responsabilidade:** Gerenciar todo o ciclo de vida dos agendamentos.

#### Endpoints de Consulta:

**GET `/api/agendamentos`**
- **Descrição:** Lista todos os agendamentos (apenas ADMIN)
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/agendamentos/{id}`**
- **Descrição:** Busca agendamento por ID
- **Response:** `AgendamentoDTO.Response`
- **Autorização:** `hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO', 'ADMIN')`

**GET `/api/agendamentos/cliente`**
- **Descrição:** Lista agendamentos do cliente logado
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('CLIENTE')`

**GET `/api/agendamentos/profissional`**
- **Descrição:** Lista agendamentos do profissional logado
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('PROFISSIONAL')`

**GET `/api/agendamentos/salao`**
- **Descrição:** Lista agendamentos dos salões do proprietário logado
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('PROPRIETARIO')`

#### Endpoints de Histórico e Estatísticas:

**GET `/api/agendamentos/cliente/historico`**
- **Descrição:** Histórico de agendamentos do cliente
- **Query Params:** `dataInicio`, `dataFim` (opcionais)
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('CLIENTE')`

**GET `/api/agendamentos/profissional/historico`**
- **Descrição:** Histórico de agendamentos do profissional
- **Query Params:** `dataInicio`, `dataFim` (opcionais)
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('PROFISSIONAL')`

**GET `/api/agendamentos/salao/historico`**
- **Descrição:** Histórico de agendamentos do salão
- **Query Params:** `salaoId`, `dataInicio`, `dataFim` (opcionais)
- **Response:** `List<AgendamentoDTO.Response>`
- **Autorização:** `hasRole('PROPRIETARIO')`

**GET `/api/agendamentos/cliente/estatisticas`**
- **Descrição:** Estatísticas de agendamentos do cliente
- **Query Params:** `dataInicio`, `dataFim` (opcionais)
- **Response:** `AgendamentoEstatisticas`
- **Autorização:** `hasRole('CLIENTE')`

#### Endpoints de Operações:

**POST `/api/agendamentos`**
- **Descrição:** Cria novo agendamento
- **Body:** `AgendamentoDTO.Request`
- **Response:** `AgendamentoDTO.Response`
- **Autorização:** `hasRole('CLIENTE')`
- **Validações:** Disponibilidade, conflitos, horários válidos

**PUT `/api/agendamentos/{id}/cancelar`**
- **Descrição:** Cancela agendamento
- **Response:** `AgendamentoDTO.Response`
- **Autorização:** `hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO')`

**PUT `/api/agendamentos/{id}/concluir`**
- **Descrição:** Conclui agendamento
- **Response:** `AgendamentoDTO.Response`
- **Autorização:** `hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')`

#### Endpoints de Horários:

**GET `/api/agendamentos/horarios-disponiveis`**
- **Descrição:** Lista horários disponíveis para agendamento
- **Query Params:** `salaoId`, `profissionalId`, `data`
- **Response:** `List<String>` (horários formatados)
- **Autorização:** Pública

**POST `/api/agendamentos/horarios/bloquear`**
- **Descrição:** Bloqueia horário para profissional
- **Query Params:** `profissionalId`, `dataHora`
- **Response:** `HorarioTrabalho`
- **Autorização:** `hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')`

**POST `/api/agendamentos/horarios/desbloquear`**
- **Descrição:** Desbloqueia horário para profissional
- **Query Params:** `profissionalId`, `dataHora`
- **Response:** `HorarioTrabalho`
- **Autorização:** `hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')`

#### Características:
- Controle granular de acesso baseado em roles
- Validações complexas de disponibilidade e conflitos
- Suporte a histórico e estatísticas
- Geração automática de horários disponíveis
- Integração com múltiplos serviços

---

### 3. SalaoController
**Base Path:** `/api/saloes`

**Responsabilidade:** Gerenciar salões de beleza.

#### Endpoints:

**GET `/api/saloes`**
- **Descrição:** Lista todos os salões
- **Response:** `List<SalaoDTO.Response>`
- **Autorização:** Pública

**GET `/api/saloes/{id}`**
- **Descrição:** Busca salão por ID
- **Response:** `SalaoDTO.Response`
- **Autorização:** Pública

**POST `/api/saloes`**
- **Descrição:** Cria novo salão
- **Body:** `SalaoDTO.Request`
- **Response:** `SalaoDTO.Response`
- **Autorização:** Pública
- **Validações:** Dados obrigatórios, duplicidade

**PUT `/api/saloes/{id}`**
- **Descrição:** Atualiza salão existente
- **Body:** `SalaoDTO.Request`
- **Response:** `SalaoDTO.Response`
- **Autorização:** Pública

**DELETE `/api/saloes/{id}`**
- **Descrição:** Remove salão
- **Response:** `204 No Content`
- **Autorização:** Pública

**GET `/api/saloes/proprietario`**
- **Descrição:** Lista salões por proprietário
- **Query Params:** `proprietarioId`
- **Response:** `List<SalaoDTO.Response>`
- **Autorização:** Pública

**GET `/api/saloes/buscar`**
- **Descrição:** Busca salão por nome e endereço
- **Query Params:** `nome`, `endereco`
- **Response:** `SalaoDTO.Response`
- **Autorização:** Pública

**GET `/api/saloes/{id}/servicos`**
- **Descrição:** Lista serviços do salão
- **Response:** `List<ServicoDTO.Response>`
- **Autorização:** Pública

**GET `/api/saloes/{id}/profissionais`**
- **Descrição:** Lista profissionais do salão
- **Response:** `List<ProfissionalDTO.Response>`
- **Autorização:** Pública

#### Características:
- Suporte a CORS para frontend
- Conversão automática entre DTOs e entidades
- Configuração automática de horários padrão
- Validações de dados obrigatórios

---

### 4. ServicoController
**Base Path:** `/api/servicos`

**Responsabilidade:** Gerenciar serviços oferecidos pelos salões.

#### Endpoints:

**GET `/api/servicos`**
- **Descrição:** Lista todos os serviços
- **Response:** `List<ServicoDTO.Response>`
- **Autorização:** Pública

**GET `/api/servicos/{id}`**
- **Descrição:** Busca serviço por ID
- **Response:** `ServicoDTO.Response`
- **Autorização:** Pública

**POST `/api/servicos`**
- **Descrição:** Cria novo serviço
- **Body:** `ServicoDTO`
- **Response:** `ServicoDTO.Response`
- **Autorização:** Pública
- **Validações:** Dados obrigatórios

**PUT `/api/servicos/{id}`**
- **Descrição:** Atualiza serviço existente
- **Body:** `ServicoDTO`
- **Response:** `ServicoDTO.Response`
- **Autorização:** Pública

**DELETE `/api/servicos/{id}`**
- **Descrição:** Remove serviço
- **Response:** `204 No Content`
- **Autorização:** Pública

**GET `/api/servicos/salao/{salaoId}`**
- **Descrição:** Lista serviços por salão
- **Response:** `List<ServicoDTO.Response>`
- **Autorização:** Pública

#### Características:
- Documentação Swagger/OpenAPI
- Validação de dados com Bean Validation
- Conversão automática entre DTOs e entidades

---

### 5. ProfissionalController
**Base Path:** `/api/profissionais`

**Responsabilidade:** Gerenciar profissionais de beleza.

#### Endpoints:

**GET `/api/profissionais`**
- **Descrição:** Lista todos os profissionais
- **Response:** `List<ProfissionalDTO.Response>`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/profissionais/{id}`**
- **Descrição:** Busca profissional por ID
- **Response:** `ProfissionalDTO.Response`
- **Autorização:** `hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)`

**POST `/api/profissionais`**
- **Descrição:** Cria novo profissional
- **Body:** `ProfissionalDTO.Request`
- **Response:** `ProfissionalDTO.Response`
- **Autorização:** `hasRole('ADMIN')`

**PUT `/api/profissionais/{id}`**
- **Descrição:** Atualiza profissional existente
- **Body:** `ProfissionalDTO.Request`
- **Response:** `ProfissionalDTO.Response`
- **Autorização:** `hasRole('ADMIN') or @securityService.isProfissionalLogado(#id)`

**DELETE `/api/profissionais/{id}`**
- **Descrição:** Remove profissional
- **Response:** `200 OK`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/profissionais/{id}/servicos`**
- **Descrição:** Lista serviços disponíveis para o profissional
- **Response:** `List<ServicoDTO.Response>`
- **Autorização:** Pública

**GET `/api/profissionais/{id}/disponibilidade`**
- **Descrição:** Verifica disponibilidade do profissional
- **Query Params:** `data` (LocalDate)
- **Response:** `Map<String, List<String>>`
- **Autorização:** Pública

#### Características:
- Controle de acesso baseado em roles e propriedade
- Integração com SecurityService para validações específicas
- Suporte a verificação de disponibilidade
- Vinculação automática com salão

---

### 6. ClienteController
**Base Path:** `/api/clientes`

**Responsabilidade:** Gerenciar clientes do sistema.

#### Endpoints:

**GET `/api/clientes`**
- **Descrição:** Lista todos os clientes
- **Response:** `List<ClienteDTO>`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/clientes/{id}`**
- **Descrição:** Busca cliente por ID
- **Response:** `ClienteDTO`
- **Autorização:** `hasRole('ADMIN') or @securityService.isClienteLogado(#id)`

**POST `/api/clientes`**
- **Descrição:** Cria novo cliente
- **Body:** `ClienteDTO`
- **Response:** `ClienteDTO`
- **Autorização:** `hasRole('ADMIN')`

**PUT `/api/clientes/{id}`**
- **Descrição:** Atualiza cliente existente
- **Body:** `ClienteDTO`
- **Response:** `ClienteDTO`
- **Autorização:** `hasRole('ADMIN') or @securityService.isClienteLogado(#id)`

**DELETE `/api/clientes/{id}`**
- **Descrição:** Remove cliente
- **Response:** `200 OK`
- **Autorização:** `hasRole('ADMIN') or @securityService.isClienteLogado(#id)`

#### Características:
- Controle de acesso baseado em roles e propriedade
- Proteção de dados sensíveis (senha não retornada)
- Integração com SecurityService

---

### 7. ProprietarioController
**Base Path:** `/api/proprietarios`

**Responsabilidade:** Gerenciar proprietários de salões.

#### Endpoints:

**GET `/api/proprietarios`**
- **Descrição:** Lista todos os proprietários
- **Response:** `List<Proprietario>`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/proprietarios/{id}`**
- **Descrição:** Busca proprietário por ID
- **Response:** `Proprietario`
- **Autorização:** `hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)`

**POST `/api/proprietarios`**
- **Descrição:** Cria novo proprietário
- **Body:** `ProprietarioDTO`
- **Response:** `Proprietario`
- **Autorização:** `hasRole('ADMIN')`

**PUT `/api/proprietarios/{id}`**
- **Descrição:** Atualiza proprietário existente
- **Body:** `ProprietarioDTO`
- **Response:** `Proprietario`
- **Autorização:** `hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)`

**DELETE `/api/proprietarios/{id}`**
- **Descrição:** Remove proprietário
- **Response:** `200 OK`
- **Autorização:** `hasRole('ADMIN') or @securityService.isProprietarioLogado(#id)`

#### Características:
- Controle de acesso baseado em roles e propriedade
- Integração com SecurityService

---

### 8. AdminController
**Base Path:** `/api/admin`

**Responsabilidade:** Gerenciar administradores do sistema.

#### Endpoints:

**GET `/api/admin`**
- **Descrição:** Lista todos os administradores
- **Response:** `List<Admin>`
- **Autorização:** `hasRole('ADMIN')`

**GET `/api/admin/{id}`**
- **Descrição:** Busca administrador por ID
- **Response:** `Admin`
- **Autorização:** `hasRole('ADMIN') and @securityService.isAdminLogado(#id)`

**POST `/api/admin`**
- **Descrição:** Cria novo administrador
- **Body:** `AdminDTO`
- **Response:** `Admin`
- **Autorização:** `hasRole('ADMIN')`

**PUT `/api/admin/{id}`**
- **Descrição:** Atualiza administrador existente
- **Body:** `AdminDTO`
- **Response:** `Admin`
- **Autorização:** `hasRole('ADMIN') and @securityService.isAdminLogado(#id)`

**DELETE `/api/admin/{id}`**
- **Descrição:** Remove administrador
- **Response:** `200 OK`
- **Autorização:** `hasRole('ADMIN') and @securityService.isAdminLogado(#id)`

#### Características:
- Controle de acesso restrito apenas a administradores
- Validação de propriedade através do SecurityService

---

## Padrões de Segurança

### 1. Autenticação
- **JWT Token**: Autenticação baseada em tokens JWT
- **Spring Security**: Framework de segurança integrado
- **UserPrincipal**: Classe customizada para detalhes do usuário

### 2. Autorização
- **@PreAuthorize**: Anotações para controle de acesso
- **Role-based**: Controle baseado em roles (ADMIN, CLIENTE, PROFISSIONAL, PROPRIETARIO)
- **SecurityService**: Validações específicas de propriedade

### 3. Validações
- **Bean Validation**: Validação automática de dados de entrada
- **@Valid**: Anotação para ativar validações
- **Tratamento de Erros**: Respostas HTTP apropriadas para diferentes tipos de erro

---

## Padrões de Resposta

### 1. Códigos HTTP
- **200 OK**: Operação bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação bem-sucedida sem conteúdo
- **400 Bad Request**: Dados inválidos
- **401 Unauthorized**: Não autenticado
- **403 Forbidden**: Não autorizado
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

### 2. Estrutura de Resposta
- **DTOs**: Separação entre dados de entrada e saída
- **ResponseEntity**: Wrapper para respostas HTTP
- **Listas**: Respostas paginadas quando necessário

---

## Considerações de Design

### 1. Separação de Responsabilidades
- Controllers focados apenas em receber requisições e retornar respostas
- Lógica de negócio delegada para serviços
- Validações de dados centralizadas

### 2. Segurança
- Controle granular de acesso baseado em roles
- Validação de propriedade de recursos
- Proteção contra ataques comuns

### 3. Manutenibilidade
- Código limpo e bem estruturado
- Documentação automática com Swagger
- Padrões consistentes em todos os controllers

---

## Próximos Passos Sugeridos

### 1. Melhorias
- Implementar paginação em endpoints de listagem
- Adicionar cache para consultas frequentes
- Implementar rate limiting para proteção contra abuso

### 2. Documentação
- Expandir documentação Swagger com exemplos
- Adicionar testes de integração para todos os endpoints
- Documentar códigos de erro específicos

### 3. Funcionalidades
- Implementar upload de imagens para serviços
- Adicionar endpoints para relatórios avançados
- Implementar notificações em tempo real

---

## Resumo da Arquitetura

```
Controller Layer
├── AuthController (Autenticação)
├── AgendamentoController (Agendamentos - mais complexo)
├── SalaoController (Salões)
├── ServicoController (Serviços)
├── ProfissionalController (Profissionais)
├── ClienteController (Clientes)
├── ProprietarioController (Proprietários)
└── AdminController (Administradores)
```

A camada de controller fornece uma API REST completa e segura, seguindo as melhores práticas do Spring Boot e implementando controle de acesso granular baseado em roles. 