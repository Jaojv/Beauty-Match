package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.AgendamentoDTO;
import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.entity.HorarioTrabalho;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.exception.AgendamentoException;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import com.beauty.com.MatchBeauty.repository.UsuarioRepository;
import com.beauty.com.MatchBeauty.service.AgendamentoService;
import com.beauty.com.MatchBeauty.service.ClienteService;
import com.beauty.com.MatchBeauty.service.HorarioTrabalhoService;
import com.beauty.com.MatchBeauty.service.ProfissionalService;
import com.beauty.com.MatchBeauty.service.SalaoService;
import com.beauty.com.MatchBeauty.service.ServicoService;
import com.beauty.com.MatchBeauty.service.ProprietarioService;
import com.beauty.com.MatchBeauty.service.HorarioFuncionamentoSalaoService;
import com.beauty.com.MatchBeauty.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Controller responsável por gerenciar operações relacionadas aos agendamentos
// Fornece endpoints para CRUD de agendamentos, consultas por usuário e gerenciamento de horários
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    // Serviço para operações de agendamento
    @Autowired
    private AgendamentoService agendamentoService;

    // Repositório para operações de cliente
    @Autowired
    private ClienteRepository clienteRepository;

    // Repositório para operações de usuário
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Serviço para operações de cliente
    @Autowired
    private ClienteService clienteService;

    // Serviço para operações de profissional
    @Autowired
    private ProfissionalService profissionalService;

    // Serviço para operações de salão
    @Autowired
    private SalaoService salaoService;

    // Serviço para operações de serviço
    @Autowired
    private ServicoService servicoService;

    // Serviço para operações de horário de trabalho
    @Autowired
    private HorarioTrabalhoService horarioTrabalhoService;

    // Serviço para operações de proprietário
    @Autowired
    private ProprietarioService proprietarioService;

    // Serviço para operações de horário de funcionamento
    @Autowired
    private HorarioFuncionamentoSalaoService horarioFuncionamentoService;

    // Endpoint para listar todos os agendamentos (apenas admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarTodosAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentos();
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para buscar um agendamento específico por ID
    // Verifica permissões baseadas no tipo de usuário logado
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO', 'ADMIN')")
    public ResponseEntity<AgendamentoDTO.Response> buscarAgendamentoPorId(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica permissões do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        String tipoUsuario = userPrincipal.getTipoUsuario();
        Usuario usuarioLogado;
        if ("CLIENTE".equalsIgnoreCase(tipoUsuario)) {
            usuarioLogado = clienteRepository.findById(usuarioId).orElse(null);
        } else if ("PROFISSIONAL".equalsIgnoreCase(tipoUsuario)) {
            usuarioLogado = profissionalService.buscarProfissional(usuarioId);
        } else if ("PROPRIETARIO".equalsIgnoreCase(tipoUsuario)) {
            usuarioLogado = proprietarioService.buscarPorId(usuarioId);
        } else {
            usuarioLogado = null;
        }
        if (usuarioLogado == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(AgendamentoDTO.Response.fromEntity(agendamento));
    }

    // Endpoint para listar agendamentos do cliente logado
    @GetMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorClienteLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorCliente(usuarioId);
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar agendamentos do profissional logado
    @GetMapping("/profissional")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorProfissionalLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorProfissional(usuarioId);
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar agendamentos dos salões do proprietário logado
    @GetMapping("/salao")
    @PreAuthorize("hasRole('PROPRIETARIO')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorSalaoDoProprietarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        List<Long> salaoIds = salaoService.buscarSaloesPorProprietario(usuarioId)
                .stream()
                .map(salao -> salao.getId())
                .collect(Collectors.toList());
        if (salaoIds.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<Agendamento> agendamentos = salaoIds.stream()
                .flatMap(salaoId -> agendamentoService.buscarAgendamentosPorSalao(salaoId).stream())
                .collect(Collectors.toList());
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar agendamentos ativos do cliente logado
    @GetMapping("/cliente/ativos")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosAtivosPorClienteLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        List<Agendamento> todosAgendamentos = agendamentoService.buscarAgendamentosPorCliente(usuarioId);
        List<Agendamento> agendamentosAtivos = todosAgendamentos.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .collect(Collectors.toList());
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentosAtivos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar histórico de agendamentos do cliente logado
    @GetMapping("/cliente/historico")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarHistoricoAgendamentosPorClienteLogado(
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        List<Agendamento> historico = agendamentoService.buscarHistoricoCliente(
            usuarioId,
            inicio,
            fim
        );
        List<AgendamentoDTO.Response> agendamentosDTO = historico.stream()
            .map(AgendamentoDTO.Response::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar histórico de agendamentos do profissional logado
    @GetMapping("/profissional/historico")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarHistoricoAgendamentosPorProfissionalLogado(
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        List<Agendamento> historico = agendamentoService.buscarHistoricoProfissional(
            usuarioId,
            inicio,
            fim
        );
        List<AgendamentoDTO.Response> agendamentosDTO = historico.stream()
            .map(AgendamentoDTO.Response::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para listar histórico de agendamentos de um salão específico
    @GetMapping("/salao/historico")
    @PreAuthorize("hasRole('PROPRIETARIO')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarHistoricoAgendamentosPorSalaoLogado(
        @RequestParam Long salaoId,
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        // Verifica se o salão pertence ao proprietário logado
        boolean salaoPertenceProprietario = salaoService.buscarSaloesPorProprietario(usuarioId)
            .stream()
            .anyMatch(salao -> salao.getId().equals(salaoId));
        if (!salaoPertenceProprietario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        List<Agendamento> historico = agendamentoService.buscarHistoricoSalao(
            salaoId,
            inicio,
            fim
        );
        List<AgendamentoDTO.Response> agendamentosDTO = historico.stream()
            .map(AgendamentoDTO.Response::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    // Endpoint para buscar estatísticas de agendamentos do cliente logado
    @GetMapping("/cliente/estatisticas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<AgendamentoService.AgendamentoEstatisticas> buscarEstatisticasClienteLogado(
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        AgendamentoService.AgendamentoEstatisticas estatisticas = agendamentoService.buscarEstatisticasCliente(
            usuarioId,
            inicio,
            fim
        );
        
        return ResponseEntity.ok(estatisticas);
    }

    // Endpoint para buscar estatísticas de agendamentos do profissional logado
    @GetMapping("/profissional/estatisticas")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<AgendamentoService.AgendamentoEstatisticas> buscarEstatisticasProfissionalLogado(
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        AgendamentoService.AgendamentoEstatisticas estatisticas = agendamentoService.buscarEstatisticasProfissional(
            usuarioId,
            inicio,
            fim
        );
        
        return ResponseEntity.ok(estatisticas);
    }

    // Endpoint para buscar estatísticas de agendamentos de um salão específico
    @GetMapping("/salao/estatisticas")
    @PreAuthorize("hasRole('PROPRIETARIO')")
    public ResponseEntity<AgendamentoService.AgendamentoEstatisticas> buscarEstatisticasSalaoLogado(
        @RequestParam Long salaoId,
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        
        // Verifica se o salão pertence ao proprietário logado
        boolean salaoPertenceProprietario = salaoService.buscarSaloesPorProprietario(usuarioId)
            .stream()
            .anyMatch(salao -> salao.getId().equals(salaoId));
            
        if (!salaoPertenceProprietario) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        AgendamentoService.AgendamentoEstatisticas estatisticas = agendamentoService.buscarEstatisticasSalao(
            salaoId,
            inicio,
            fim
        );
        
        return ResponseEntity.ok(estatisticas);
    }

    // Endpoint para criar um novo agendamento
    // Apenas clientes podem criar agendamentos
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoDTO.Request request) {
        try {
            // Processar a data/hora corretamente
            LocalDateTime dataHoraProcessada = processarDataHora(request.getDataHora());
            
            System.out.println("🔍 DEBUG: Data/Hora original: " + request.getDataHora());
            System.out.println("🔍 DEBUG: Data/Hora processada: " + dataHoraProcessada);
            
            // Extrair clienteId do token JWT
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            Long clienteId = userPrincipal.getId();
            
            // Verificar se o usuário logado é realmente um cliente
            if (!"CLIENTE".equalsIgnoreCase(userPrincipal.getTipoUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Apenas clientes podem criar agendamentos");
            }
            
            // Buscar o cliente pelo ID do token
            Usuario cliente = usuarioRepository.findById(clienteId)
                .orElseThrow(() -> new AgendamentoException("Cliente não encontrado"));
            
            // Buscar o profissional
            Usuario profissional = usuarioRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new AgendamentoException("Profissional não encontrado com ID: " + request.getProfissionalId()));

            // Verificar se o profissional pertence ao salão
            if (!(profissional instanceof com.beauty.com.MatchBeauty.entity.Profissional)) {
                return ResponseEntity.badRequest().body("Usuário não é um profissional");
            }
            
            com.beauty.com.MatchBeauty.entity.Profissional prof = (com.beauty.com.MatchBeauty.entity.Profissional) profissional;
            if (prof.getSalao() == null || !prof.getSalao().getId().equals(request.getSalaoId())) {
                return ResponseEntity.badRequest().body("Profissional não pertence ao salão informado");
            }

            // Buscar o serviço
            Servico servico = servicoService.buscarServico(request.getServicoId())
                .orElseThrow(() -> new AgendamentoException("Serviço não encontrado com ID: " + request.getServicoId()));

            // Verificar se o serviço pertence ao salão
            if (!servico.getSalao().getId().equals(request.getSalaoId())) {
                return ResponseEntity.badRequest().body("Serviço não pertence ao salão informado");
            }

            // Buscar o salão
            Salao salao = salaoService.buscarSalao(request.getSalaoId());

            // Verificar se a data/hora não é no passado
            if (dataHoraProcessada.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Não é possível agendar para datas/horários no passado");
            }

            // Criar o objeto Agendamento
            Agendamento agendamento = new Agendamento();
            agendamento.setDataHora(dataHoraProcessada);
            agendamento.setCliente(cliente);
            agendamento.setProfissional(profissional);
            agendamento.setServico(servico);
            agendamento.setSalao(salao);
            agendamento.setObservacoes(request.getObservacoes());
            agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);

            Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamento);
            System.out.println("🔍 DEBUG: Data/Hora salva no banco: " + novoAgendamento.getDataHora());
            
            AgendamentoDTO.Response response = AgendamentoDTO.Response.fromEntity(novoAgendamento);
            System.out.println("🔍 DEBUG: Data/Hora no response: " + response.getDataHora());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AgendamentoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Adicionando log do erro para depuração
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao criar agendamento: " + e.getMessage());
        }
    }
    
    /**
     * Processa a data/hora recebida do frontend para garantir o timezone correto
     */
    private LocalDateTime processarDataHora(String dataHoraString) {
        if (dataHoraString == null || dataHoraString.trim().isEmpty()) {
            throw new AgendamentoException("Data/Hora não pode ser nula ou vazia");
        }
        
        System.out.println("🔍 DEBUG: Processando string de data/hora: " + dataHoraString);
        
        try {
            // Fazer parse da string diretamente como LocalDateTime
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraString);
            System.out.println("🔍 DEBUG: Data/Hora após parse: " + dataHora);
            return dataHora;
        } catch (Exception e) {
            System.out.println("❌ DEBUG: Erro ao fazer parse da data/hora: " + e.getMessage());
            throw new AgendamentoException("Formato de data/hora inválido: " + dataHoraString);
        }
    }

    // Endpoint para cancelar um agendamento
    // Clientes, profissionais e proprietários podem cancelar (com permissões específicas)
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<AgendamentoDTO.Response> cancelarAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        // Verifica permissões para cancelar o agendamento
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        String tipoUsuario = userPrincipal.getTipoUsuario();
        boolean podeEditar = false;
        if ("CLIENTE".equalsIgnoreCase(tipoUsuario) && 
            agendamento.getCliente().getIdUsuario().equals(usuarioId)) {
            podeEditar = true;
        }
        if ("PROFISSIONAL".equalsIgnoreCase(tipoUsuario) && 
            agendamento.getProfissional().getIdUsuario().equals(usuarioId)) {
            podeEditar = true;
        }
        if ("PROPRIETARIO".equalsIgnoreCase(tipoUsuario) && 
            agendamento.getSalao().getProprietario().getIdUsuario().equals(usuarioId)) {
            podeEditar = true;
        }
        if (!podeEditar) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        Agendamento agendamentoAtualizado = agendamentoService.atualizarAgendamento(agendamento);
        return ResponseEntity.ok(AgendamentoDTO.Response.fromEntity(agendamentoAtualizado));
    }

    // Endpoint para concluir um agendamento
    // Apenas profissionais e proprietários podem concluir agendamentos
    @PutMapping("/{id}/concluir")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<AgendamentoDTO.Response> concluirAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica permissões para concluir o agendamento
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        String tipoUsuario = userPrincipal.getTipoUsuario();
        boolean podeEditar = false;
        
        if ("PROFISSIONAL".equalsIgnoreCase(tipoUsuario) && 
            agendamento.getProfissional().getIdUsuario().equals(usuarioId)) {
            podeEditar = true;
        }
        
        if ("PROPRIETARIO".equalsIgnoreCase(tipoUsuario) && 
            agendamento.getSalao().getProprietario().getIdUsuario().equals(usuarioId)) {
            podeEditar = true;
        }
        
        if (!podeEditar) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        Agendamento agendamentoAtualizado = agendamentoService.atualizarAgendamento(agendamento);
        
        return ResponseEntity.ok(AgendamentoDTO.Response.fromEntity(agendamentoAtualizado));
    }

    // Endpoint para listar horários disponíveis para agendamento
    // Retorna horários livres de um profissional em uma data específica
    @GetMapping("/horarios-disponiveis")
    @Operation(summary = "Listar horários disponíveis para agendamento")
    public ResponseEntity<List<String>> listarHorariosDisponiveis(
            @RequestParam Long salaoId,
            @RequestParam Long profissionalId,
            @RequestParam String data) {
        
        try {
            // Converter a data string para LocalDate
            LocalDate dataAgendamento = LocalDate.parse(data);
            DayOfWeek diaSemana = dataAgendamento.getDayOfWeek();
            
            // Verificar se a data não é no passado
            if (dataAgendamento.isBefore(LocalDate.now())) {
                return ResponseEntity.ok(List.of());
            }
            
            // Buscar o profissional
            Usuario profissional = usuarioRepository.findById(profissionalId)
                .orElseThrow(() -> new AgendamentoException("Profissional não encontrado"));
            
            // Verificar se o profissional pertence ao salão
            if (!(profissional instanceof com.beauty.com.MatchBeauty.entity.Profissional)) {
                return ResponseEntity.badRequest().build();
            }
            
            com.beauty.com.MatchBeauty.entity.Profissional prof = (com.beauty.com.MatchBeauty.entity.Profissional) profissional;
            if (prof.getSalao() == null || !prof.getSalao().getId().equals(salaoId)) {
                return ResponseEntity.badRequest().build();
            }
            
            // Gerar slots disponíveis baseados no horário de funcionamento do salão
            List<LocalTime> slotsDisponiveis = horarioFuncionamentoService.gerarSlotsDisponiveis(salaoId, diaSemana);
            
            // Filtrar horários já agendados
            List<LocalTime> horariosOcupados = agendamentoService.buscarHorariosOcupados(profissionalId, dataAgendamento);
            
            // Filtrar horários bloqueados pelo profissional
            List<LocalTime> horariosBloqueados = horarioTrabalhoService.buscarHorariosTrabalhoProfissionalPorDia(profissionalId, diaSemana)
                .stream()
                .filter(HorarioTrabalho::isBloqueado)
                .flatMap(horario -> {
                    List<LocalTime> slots = new ArrayList<>();
                    LocalTime hora = horario.getHoraInicio();
                    while (!hora.isAfter(horario.getHoraFim())) {
                        slots.add(hora);
                        hora = hora.plusMinutes(15);
                    }
                    return slots.stream();
                })
                .collect(Collectors.toList());
            
            // Remover horários ocupados e bloqueados dos slots disponíveis
            slotsDisponiveis.removeAll(horariosOcupados);
            slotsDisponiveis.removeAll(horariosBloqueados);
            
            // Converter para formato de string
            List<String> horariosFormatados = slotsDisponiveis.stream()
                .map(horario -> horario.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
        
            return ResponseEntity.ok(horariosFormatados);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para bloquear horário de trabalho
    // Profissionais e proprietários podem bloquear horários
    @PostMapping("/horarios/bloquear")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<?> bloquearHorario(
        @RequestParam Long profissionalId,
        @RequestParam String dataHora
    ) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            Long usuarioId = userPrincipal.getId();
            
            Usuario profissional = profissionalService.buscarProfissional(profissionalId);
            if (profissional == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Verifica permissões para bloquear horário
            boolean podeBloquear = false;
            
            if (userPrincipal.getTipoUsuario().equals("PROFISSIONAL") && 
                profissional.getIdUsuario().equals(usuarioId)) {
                podeBloquear = true;
            }
            
            if (userPrincipal.getTipoUsuario().equals("PROPRIETARIO")) {
                boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioId)
                    .stream()
                    .anyMatch(salao -> salao.getProfissionais().contains(profissional));
                
                if (profissionalPertenceSalao) {
                    podeBloquear = true;
                }
            }
            
            if (!podeBloquear) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            LocalDateTime dataHoraLocal = LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_DATE_TIME);
            
            HorarioTrabalho horarioBloqueado = horarioTrabalhoService.bloquearHorario(profissional, dataHoraLocal);
            if (horarioBloqueado == null) {
                return ResponseEntity.badRequest().build();
            }
            
            return ResponseEntity.ok(horarioBloqueado);
        } catch (AgendamentoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para desbloquear horário de trabalho
    // Profissionais e proprietários podem desbloquear horários
    @PostMapping("/horarios/desbloquear")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<HorarioTrabalho> desbloquearHorario(
        @RequestParam Long profissionalId,
        @RequestParam String dataHora
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        
        Usuario profissional = profissionalService.buscarProfissional(profissionalId);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica permissões para desbloquear horário
        boolean podeDesbloquear = false;
        
        if (userPrincipal.getTipoUsuario().equals("PROFISSIONAL") && 
            profissional.getIdUsuario().equals(usuarioId)) {
            podeDesbloquear = true;
        }
        
        if (userPrincipal.getTipoUsuario().equals("PROPRIETARIO")) {
            boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioId)
                .stream()
                .anyMatch(salao -> salao.getProfissionais().contains(profissional));
            
            if (profissionalPertenceSalao) {
                podeDesbloquear = true;
            }
        }
        
        if (!podeDesbloquear) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        LocalDateTime dataHoraLocal = LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_DATE_TIME);
        
        HorarioTrabalho horarioDesbloqueado = horarioTrabalhoService.desbloquearHorario(profissional, dataHoraLocal);
        if (horarioDesbloqueado == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(horarioDesbloqueado);
    }

    // Endpoint para listar horários bloqueados de um profissional
    @GetMapping("/horarios/bloqueados")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<List<HorarioTrabalho>> listarHorariosBloqueados(
        @RequestParam Long profissionalId,
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFim
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        Long usuarioId = userPrincipal.getId();
        
        Usuario profissional = profissionalService.buscarProfissional(profissionalId);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica permissões para visualizar horários bloqueados
        boolean podeVer = false;
        
        if (userPrincipal.getTipoUsuario().equals("PROFISSIONAL") && 
            profissional.getIdUsuario().equals(usuarioId)) {
            podeVer = true;
        }
        
        if (userPrincipal.getTipoUsuario().equals("PROPRIETARIO")) {
            boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioId)
                .stream()
                .anyMatch(salao -> salao.getProfissionais().contains(profissional));
            
            if (profissionalPertenceSalao) {
                podeVer = true;
            }
        }
        
        if (!podeVer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        List<HorarioTrabalho> horarios = horarioTrabalhoService.buscarHorariosTrabalhoProfissional(profissionalId);
        
        List<HorarioTrabalho> horariosBloqueados = horarios.stream()
            .filter(HorarioTrabalho::isBloqueado)
            .collect(Collectors.toList());
        
        // Filtrar por período se fornecido
        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime fim = LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME);
            
            horariosBloqueados = horariosBloqueados.stream()
                .filter(horario -> {
                    LocalDateTime dataHora = LocalDateTime.of(
                        inicio.toLocalDate(),
                        horario.getHoraInicio()
                    );
                    return !dataHora.isBefore(inicio) && !dataHora.isAfter(fim);
                })
                .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(horariosBloqueados);
    }
}