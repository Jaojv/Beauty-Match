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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private SalaoService salaoService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private HorarioTrabalhoService horarioTrabalhoService;

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private HorarioFuncionamentoSalaoService horarioFuncionamentoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarTodosAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarAgendamentos();
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO', 'ADMIN')")
    public ResponseEntity<AgendamentoDTO.Response> buscarAgendamentoPorId(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        
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

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoDTO.Request request) {
        try {
            // Buscar as entidades pelo ID
            Usuario cliente = usuarioRepository.findById(request.getClienteId())
                .orElseThrow(() -> new AgendamentoException("Cliente não encontrado com ID: " + request.getClienteId()));
            
            Usuario profissional = usuarioRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new AgendamentoException("Profissional não encontrado com ID: " + request.getProfissionalId()));

            Servico servico = servicoService.buscarServico(request.getServicoId())
                .orElseThrow(() -> new AgendamentoException("Serviço não encontrado com ID: " + request.getServicoId()));

            Salao salao = salaoService.buscarSalao(request.getSalaoId());

            // Criar o objeto Agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(request.getDataHora());
            agendamento.setCliente(cliente);
            agendamento.setProfissional(profissional);
            agendamento.setServico(servico);
            agendamento.setSalao(salao);
        agendamento.setObservacoes(request.getObservacoes());
            agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);

        Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamento);
            AgendamentoDTO.Response response = AgendamentoDTO.Response.fromEntity(novoAgendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (AgendamentoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Adicionando log do erro para depuração
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao criar agendamento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<AgendamentoDTO.Response> cancelarAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
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

    @PutMapping("/{id}/concluir")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<AgendamentoDTO.Response> concluirAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        
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

    /**
     * Lista horários disponíveis para agendamento
     */
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
            
            // Buscar o profissional
            Usuario profissional = usuarioRepository.findById(profissionalId)
                .orElseThrow(() -> new AgendamentoException("Profissional não encontrado"));
            
            // Gerar slots disponíveis baseados no horário de funcionamento do salão
            List<LocalTime> slotsDisponiveis = horarioFuncionamentoService.gerarSlotsDisponiveis(salaoId, diaSemana);
            
            // Filtrar horários já agendados
            List<LocalTime> horariosOcupados = agendamentoService.buscarHorariosOcupados(profissionalId, dataAgendamento);
            
            // Remover horários ocupados dos slots disponíveis
            slotsDisponiveis.removeAll(horariosOcupados);
            
            // Converter para formato de string
            List<String> horariosFormatados = slotsDisponiveis.stream()
                .map(horario -> horario.format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());
        
            return ResponseEntity.ok(horariosFormatados);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

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