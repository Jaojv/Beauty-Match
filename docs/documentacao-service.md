# Documentação - Pasta Service

## Visão Geral

A pasta `service` concentra toda a lógica de negócio do sistema BeautyMatch. Os serviços são responsáveis por orquestrar operações entre entidades, repositórios e regras de negócio, além de garantir validações, integrações e fluxos críticos do sistema.

---

## Serviços Principais

### 1. AgendamentoService
**Responsabilidade:** Gerenciar todo o ciclo de vida dos agendamentos (criação, atualização, cancelamento, conclusão, histórico e estatísticas).

**Principais métodos:**
- `criarAgendamento(Agendamento)`: Valida disponibilidade, bloqueios e conflitos antes de criar um agendamento.
- `atualizarAgendamento(Agendamento)`: Atualiza agendamento com as mesmas validações da criação.
- `deletarAgendamento(Long)`: Remove um agendamento por ID.
- `cancelarAgendamento(Long)`: Altera o status para CANCELADO.
- `concluirAgendamento(Long)`: Altera o status para CONCLUIDO.
- `concluirAgendamentosPassados()`: Conclui automaticamente agendamentos que já passaram do horário.
- `buscarAgendamentosPorCliente/Profissional/Salao/Status/Periodo`: Diversos métodos para consultas filtradas.
- `buscarHistoricoCliente/Profissional/Salao`: Retorna histórico de agendamentos concluídos.
- `buscarEstatisticasCliente/Profissional/Salao`: Gera estatísticas de agendamentos (total, concluídos, cancelados, faltantes, valor total).

**Integrações:**
- Usa `HorarioTrabalhoService` para validar disponibilidade e bloqueios.
- Usa `AgendamentoRetryService` para tentativas automáticas de conclusão.
- Usa `SalaoService` para validações de contexto.

**Validações:**
- Disponibilidade do profissional
- Horários bloqueados
- Conflito de horários (sobreposição)
- Status do agendamento

---

### 2. SalaoService
**Responsabilidade:** Gerenciar o ciclo de vida dos salões de beleza.

**Principais métodos:**
- `criarSalao(Salao, Long proprietarioId)`: Cria salão, valida duplicidade e configura horários padrão.
- `atualizarSalao(Long, Salao)`: Atualiza dados do salão com validações.
- `deletarSalao(Long)`: Remove salão por ID.
- `buscarSalao(Long)`, `buscarSalaoPorNomeEEndereco(String, String)`: Busca salão por ID ou por nome/endereço.
- `listarSaloes()`, `buscarSaloesPorProprietario(Long)`: Listagem geral ou por proprietário.
- `listarServicos(Long)`, `listarProfissionais(Long)`: Listagem de serviços e profissionais do salão.

**Integrações:**
- Usa `HorarioFuncionamentoSalaoService` para configurar horários padrão.
- Usa `UsuarioRepository` para buscar proprietário.

**Validações:**
- Nome, endereço, telefone, email e proprietário obrigatórios
- Duplicidade de nome/endereço

---

### 3. AutenticacaoService
**Responsabilidade:** Gerenciar autenticação, login, logout e registro de usuários.

**Principais métodos:**
- `realizarLogin()`: Login interativo via console (útil para testes/CLI).
- `login(String, String)`: Login via API, retorna JWT.
- `logout()`: Limpa contexto de autenticação.
- `criarUsuario(...)`: Cria usuário de qualquer tipo (Admin, Cliente, Profissional, Proprietário).
- `registrarAdmin/Cliente/Profissional`: Registra usuários de cada tipo, com validação de duplicidade e senha criptografada.
- `getUsuarioLogado()`, `setUsuarioLogado(Usuario)`: Gerencia usuário logado em contexto de aplicação.
- `getTokenAtual()`: Recupera token JWT do contexto atual.

**Integrações:**
- Usa `AuthenticationManager` e `JwtTokenProvider` para autenticação e geração de tokens.
- Usa repositórios de usuário para persistência.

**Validações:**
- Username único
- Senha criptografada
- Tipo de usuário válido

---

### 4. HorarioTrabalhoService
**Responsabilidade:** Gerenciar horários de trabalho dos profissionais, bloqueios e validação de disponibilidade.

**Principais métodos:**
- `buscarHorariosTrabalhoProfissional(Long)`: Lista horários ativos do profissional.
- `buscarHorariosTrabalhoProfissionalPorDia(Long, DayOfWeek)`: Lista horários por dia da semana.
- `criar/atualizar/deletarHorarioTrabalho(HorarioTrabalho)`: CRUD de horários.
- `verificarDisponibilidadeHorarioTrabalho(Usuario, LocalDateTime)`: Valida se o profissional está disponível no horário, considerando o funcionamento do salão.
- `verificarHorarioBloqueado(Usuario, LocalDateTime)`: Verifica se o horário está bloqueado para o profissional.
- `bloquearHorario/desbloquearHorario(Usuario, LocalDateTime)`: Bloqueia/desbloqueia horários específicos.

**Integrações:**
- Usa `HorarioFuncionamentoSalaoService` para validar funcionamento do salão.
- Usa `AgendamentoRepository` para checar conflitos.

**Validações:**
- Disponibilidade do profissional
- Bloqueios e conflitos com agendamentos

---

### 5. HorarioFuncionamentoSalaoService
**Responsabilidade:** Gerenciar horários de funcionamento dos salões e geração de slots disponíveis.

**Principais métodos:**
- `configurarHorariosPadrao(Salao)`: Configura horários padrão (seg-sáb, 8h-18h).
- `configurarHorariosCustomizados(Salao, List<HorarioFuncionamentoSalao>)`: Permite customização dos horários.
- `gerarSlotsDisponiveis(Long, DayOfWeek)`: Gera slots de 15 minutos para o dia da semana.
- `isHorarioFuncionamento(Long, DayOfWeek, LocalTime)`: Verifica se um horário está dentro do funcionamento do salão.
- `buscarHorariosPorSalao(Long)`: Lista horários ativos do salão.
- `formatarHorario(LocalTime)`: Formata horário para exibição.

**Integrações:**
- Usa `HorarioFuncionamentoSalaoRepository` para persistência.

**Validações:**
- Geração de slots respeita horários ativos e dias da semana

---

### 6. ServicoService
**Responsabilidade:** Gerenciar os serviços oferecidos pelos salões.

**Principais métodos:**
- `listarServicos()`, `buscarServico(Long)`: Listagem e busca de serviços.
- `criarServico(ServicoDTO)`: Cria serviço a partir de DTO, vinculando ao salão.
- `atualizarServico(Servico)`, `deletarServico(Long)`: Atualização e remoção.
- `buscarServicosPorSalao(Long)`, `buscarServicosPorProfissional(Long)`: Listagem filtrada por salão ou profissional.
- `buscarServicoPorNomeESalao(String, Long)`: Busca serviço por nome e salão.
- `existeServicoComNomeESalao(String, Long)`: Valida duplicidade de serviço no salão.

**Integrações:**
- Usa repositórios de serviço, salão e profissional.

**Validações:**
- Salão e profissional devem existir
- Nome único por salão

---

## Outros Serviços

- **ClienteService, ProfissionalService, ProprietarioService, AdminService:** CRUD e regras específicas para cada tipo de usuário.
- **HorarioBloqueadoService:** Gerencia bloqueios de horários para profissionais.
- **MenuService:** Gerencia menus e navegação (útil para interface CLI ou administrativa).
- **AgendamentoRetryService, AgendamentoSchedulerService:** Serviços auxiliares para agendamento automático, tentativas e agendamento em background.

---

## Considerações de Design
- Os serviços seguem o padrão Service Layer do Spring, promovendo separação de responsabilidades.
- Validações e regras de negócio centralizadas, facilitando manutenção e testes.
- Integração forte com a camada de repositório e entidades.
- Prontos para extensão e customização conforme o crescimento do sistema.

---

## Próximos Passos Sugeridos
1. Adicionar testes unitários para os principais serviços.
2. Documentar exemplos de uso dos métodos críticos.
3. Expandir a documentação para fluxos de negócio completos (ex: fluxo de agendamento do cliente ao profissional).
4. Adicionar JavaDoc detalhado nos métodos públicos dos serviços. 