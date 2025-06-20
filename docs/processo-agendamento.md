# Processo de Agendamento - BeautyMatch

## 1. Cadastro de Profissional

### 1.1 Cadastro Inicial
1. O profissional é cadastrado no sistema com informações básicas:
   - Nome completo
   - CPF
   - Data de nascimento
   - Telefone
   - Email
   - Senha
   - Foto de perfil
   - Biografia
   - Especialidades

### 1.2 Vinculação ao Salão
1. O profissional é vinculado a um ou mais salões
2. O profissional pode realizar qualquer serviço cadastrado no salão

## 2. Configuração de Horários

### 2.1 Horários do Salão
1. O proprietário do salão define:
   - Horário de funcionamento (início e fim)
   - Intervalo entre agendamentos (padrão: 15 minutos)
   - Exemplo de configuração:
     ```
     Horário de funcionamento: 08:00 às 18:00
     Intervalo entre agendamentos: 15 minutos
     Horários disponíveis: 08:00, 08:15, 08:30, 08:45, 09:00...
     ```

### 2.2 Bloqueio de Horários
1. Profissional pode:
   - Bloquear múltiplos horários de uma vez
   - Bloquear horários para datas futuras
   - Não é necessário informar motivo do bloqueio
   - Bloqueio é temporário (não recorrente)

2. Proprietário do salão pode:
   - Bloquear horários individualmente para cada profissional
   - Bloquear horários para datas futuras
   - Não é necessário informar motivo do bloqueio

### 2.3 Serviços
1. O proprietário do salão cadastra:
   - Nome do serviço
   - Descrição
   - Duração em minutos
   - Preço
   - Categoria

2. Todos os profissionais vinculados ao salão podem realizar qualquer serviço cadastrado

## 3. Processo de Agendamento

### 3.1 Busca de Disponibilidade
1. O cliente seleciona:
   - Salão
   - Serviço desejado
   - Profissional
   - Data desejada

2. O sistema verifica:
   - Horários disponíveis do salão
   - Horários não bloqueados pelo profissional
   - Duração do serviço
   - Intervalos configurados

### 3.2 Validações do Agendamento
1. Validações de Horário:
   - Salão está aberto no horário
   - Profissional não bloqueou o horário
   - Não há conflito com outros agendamentos
   - Respeita intervalo configurado entre agendamentos

2. Validações de Negócio:
   - Cliente não tem agendamento conflitante
   - Profissional está ativo
   - Serviço está ativo
   - Salão está aberto

### 3.3 Confirmação do Agendamento
1. O cliente confirma:
   - Profissional
   - Data e hora
   - Serviço
   - Preço

2. O sistema:
   - Cria o agendamento
   - Marca o horário como indisponível
   - Confirma automaticamente o agendamento

## 4. Estados do Agendamento

### 4.1 Fluxo de Estados
```
AGENDADO -> CONCLUIDO
     |
     v
   CANCELADO
     |
     v
   FALTANTE
```

### 4.2 Descrição dos Estados
- **AGENDADO**: Agendamento criado e confirmado automaticamente
- **CONCLUIDO**: Serviço realizado com sucesso ou automaticamente após passar do horário
- **CANCELADO**: Agendamento cancelado (pode ser por cliente, profissional ou salão)
- **FALTANTE**: Cliente não compareceu ao agendamento

## 5. Regras de Negócio

### 5.1 Cancelamentos
1. **Pelo Cliente**:
   - Pode cancelar quando quiser
   - Sem limite de cancelamento
   - Sem taxa de cancelamento

2. **Pelo Profissional/Proprietário**:
   - Pode cancelar quando quiser
   - Sem limite de horário
   - Sem notificação ao cliente

### 5.2 Faltas
1. **Do Cliente**:
   - Pode ser marcado como faltante
   - Sem taxa
   - Sem bloqueio de novos agendamentos

2. **Do Profissional/Proprietário**:
   - Sem reporte
   - Sem multa

## 6. Histórico de Agendamentos

### 6.1 Visualização
1. **Agendamentos Futuros**:
   - Página separada
   - Mostra próximos agendamentos
   - Inclui todos os status

2. **Histórico**:
   - Mostra apenas agendamentos passados
   - Filtro por data
   - Exclui agendamentos cancelados e faltantes
   - Sem distinção visual entre concluídos e faltantes

### 6.2 Relatórios
1. **Profissional**:
   - Horários trabalhados
   - Serviços realizados

2. **Salão**:
   - Agendamentos por período
   - Serviços realizados

3. **Cliente**:
   - Histórico de agendamentos
   - Serviços realizados

## 7. Considerações Técnicas

### 7.1 Validações
- Horários são validados em tempo real
- Conflitos são verificados considerando:
  - Duração do serviço
  - Intervalos configurados
  - Horários bloqueados
  - Agendamentos existentes

### 7.2 Performance
- Cache de horários disponíveis para melhorar tempo de resposta
- Validações em batch para relatórios

### 7.3 Segurança
- Logs de todas as alterações importantes no sistema
- Criptografia de dados sensíveis (senhas, dados pessoais) 