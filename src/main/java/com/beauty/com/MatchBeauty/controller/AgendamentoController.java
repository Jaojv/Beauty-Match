package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.AgendamentoDTO;
import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import com.beauty.com.MatchBeauty.entity.HorarioTrabalho;
import com.beauty.com.MatchBeauty.entity.Usuario;
import com.beauty.com.MatchBeauty.exception.AgendamentoException;
import com.beauty.com.MatchBeauty.service.AgendamentoService;
import com.beauty.com.MatchBeauty.service.ClienteService;
import com.beauty.com.MatchBeauty.service.HorarioTrabalhoService;
import com.beauty.com.MatchBeauty.service.ProfissionalService;
import com.beauty.com.MatchBeauty.service.SalaoService;
import com.beauty.com.MatchBeauty.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

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
        
        // Verificar se o usuário tem permissão para ver este agendamento
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        if (usuarioLogado.getTipoUsuario().equals("CLIENTE") && 
            !agendamento.getCliente().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
            !agendamento.getProfissional().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO") && 
            !agendamento.getSalao().getProprietario().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(AgendamentoDTO.Response.fromEntity(agendamento));
    }

    @GetMapping("/cliente")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorClienteLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorCliente(usuarioLogado.getIdUsuario());
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    @GetMapping("/profissional")
    @PreAuthorize("hasRole('PROFISSIONAL')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorProfissionalLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        List<Agendamento> agendamentos = agendamentoService.buscarAgendamentosPorProfissional(usuarioLogado.getIdUsuario());
        List<AgendamentoDTO.Response> agendamentosDTO = agendamentos.stream()
                .map(AgendamentoDTO.Response::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(agendamentosDTO);
    }

    @GetMapping("/salao")
    @PreAuthorize("hasRole('PROPRIETARIO')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarAgendamentosPorSalaoDoProprietarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Buscar salões do proprietário
        List<Long> salaoIds = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        List<Agendamento> todosAgendamentos = agendamentoService.buscarAgendamentosPorCliente(usuarioLogado.getIdUsuario());
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        List<Agendamento> historico = agendamentoService.buscarHistoricoCliente(
            usuarioLogado.getIdUsuario(),
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        List<Agendamento> historico = agendamentoService.buscarHistoricoProfissional(
            usuarioLogado.getIdUsuario(),
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Verificar se o salão pertence ao proprietário
        boolean salaoPertenceProprietario = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        AgendamentoService.AgendamentoEstatisticas estatisticas = agendamentoService.buscarEstatisticasCliente(
            usuarioLogado.getIdUsuario(),
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        LocalDateTime inicio = dataInicio != null ? 
            LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now().minusMonths(1);
            
        LocalDateTime fim = dataFim != null ? 
            LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME) :
            LocalDateTime.now();
        
        AgendamentoService.AgendamentoEstatisticas estatisticas = agendamentoService.buscarEstatisticasProfissional(
            usuarioLogado.getIdUsuario(),
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
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Verificar se o salão pertence ao proprietário
        boolean salaoPertenceProprietario = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
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
    public ResponseEntity<AgendamentoDTO.Response> criarAgendamento(@RequestBody AgendamentoDTO.Request request) {
        // Verificar se o cliente está logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Validar se o profissional já tem agendamento no mesmo horário
        List<Agendamento> agendamentosProfissional = agendamentoService.buscarAgendamentosPorProfissional(request.getProfissionalId());
        boolean horarioOcupado = agendamentosProfissional.stream()
                .filter(a -> a.getStatus() == StatusAgendamento.AGENDADO)
                .anyMatch(a -> a.getDataHora().equals(request.getDataHora()));
        
        if (horarioOcupado) {
            return ResponseEntity.badRequest().build();
        }
        
        // Criar o agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(request.getDataHora());
        agendamento.setCliente(clienteService.buscarCliente(usuarioLogado.getIdUsuario()));
        agendamento.setProfissional(profissionalService.buscarProfissional(request.getProfissionalId()));
        agendamento.setServico(servicoService.buscarServico(request.getServicoId()));
        agendamento.setSalao(salaoService.buscarSalao(request.getSalaoId()));
        agendamento.setObservacoes(request.getObservacoes());
        
        Agendamento novoAgendamento = agendamentoService.criarAgendamento(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoDTO.Response.fromEntity(novoAgendamento));
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyRole('CLIENTE', 'PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<AgendamentoDTO.Response> cancelarAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.buscarAgendamento(id);
        if (agendamento == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verificar se o usuário tem permissão para cancelar este agendamento
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        boolean podeEditar = false;
        
        if (usuarioLogado.getTipoUsuario().equals("CLIENTE") && 
            agendamento.getCliente().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeEditar = true;
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
            agendamento.getProfissional().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeEditar = true;
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO") && 
            agendamento.getSalao().getProprietario().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeEditar = true;
        }
        
        if (!podeEditar) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Cancelar agendamento
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
        
        // Verificar se o usuário tem permissão para concluir este agendamento
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        boolean podeEditar = false;
        
        if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
            agendamento.getProfissional().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeEditar = true;
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO") && 
            agendamento.getSalao().getProprietario().getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeEditar = true;
        }
        
        if (!podeEditar) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Concluir agendamento
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        Agendamento agendamentoAtualizado = agendamentoService.atualizarAgendamento(agendamento);
        
        return ResponseEntity.ok(AgendamentoDTO.Response.fromEntity(agendamentoAtualizado));
    }

    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<LocalDateTime>> listarHorariosDisponiveis(
            @RequestParam Long profissionalId,
            @RequestParam Long salaoId,
            @RequestParam String data) {
        
        // Converter a string de data para LocalDate
        LocalDate localDate = LocalDate.parse(data, DateTimeFormatter.ISO_DATE);
        
        // Buscar agendamentos do profissional para esta data
        LocalDateTime inicioDia = localDate.atStartOfDay();
        LocalDateTime fimDia = localDate.atTime(23, 59, 59);
        
        List<Agendamento> agendamentosDoDia = agendamentoService.buscarAgendamentosPorProfissionalEPeriodo(
                profissionalId, inicioDia, fimDia);
        
        // Horários padrão de funcionamento (exemplo: 8h às 18h, com intervalos de 1h)
        List<LocalDateTime> todosHorarios = new ArrayList<>();
        for (int hora = 8; hora <= 18; hora++) {
            todosHorarios.add(localDate.atTime(hora, 0));
        }
        
        // Filtrar horários já agendados
        List<LocalDateTime> horariosDisponiveis = todosHorarios.stream()
                .filter(horario -> agendamentosDoDia.stream()
                        .noneMatch(a -> a.getDataHora().equals(horario) && 
                                 a.getStatus() == StatusAgendamento.AGENDADO))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(horariosDisponiveis);
    }

    @PostMapping("/horarios/bloquear")
    @PreAuthorize("hasAnyRole('PROFISSIONAL', 'PROPRIETARIO')")
    public ResponseEntity<?> bloquearHorario(
        @RequestParam Long profissionalId,
        @RequestParam String dataHora
    ) {
        try {
            // Verificar se o usuário tem permissão para bloquear horários
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioLogado = (Usuario) auth.getPrincipal();
            
            // Buscar o profissional
            Usuario profissional = profissionalService.buscarProfissional(profissionalId);
            if (profissional == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Validar permissões
            boolean podeBloquear = false;
            
            if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
                profissional.getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
                podeBloquear = true;
            }
            
            if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO")) {
                // Verificar se o profissional pertence a algum salão do proprietário
                boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
                    .stream()
                    .anyMatch(salao -> salao.getProfissionais().contains(profissional));
                
                if (profissionalPertenceSalao) {
                    podeBloquear = true;
                }
            }
            
            if (!podeBloquear) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Converter a data/hora
            LocalDateTime dataHoraLocal = LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_DATE_TIME);
            
            // Bloquear o horário
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
        // Verificar se o usuário tem permissão para desbloquear horários
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Buscar o profissional
        Usuario profissional = profissionalService.buscarProfissional(profissionalId);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Validar permissões
        boolean podeDesbloquear = false;
        
        if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
            profissional.getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeDesbloquear = true;
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO")) {
            // Verificar se o profissional pertence a algum salão do proprietário
            boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
                .stream()
                .anyMatch(salao -> salao.getProfissionais().contains(profissional));
            
            if (profissionalPertenceSalao) {
                podeDesbloquear = true;
            }
        }
        
        if (!podeDesbloquear) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Converter a data/hora
        LocalDateTime dataHoraLocal = LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_DATE_TIME);
        
        // Desbloquear o horário
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
        // Verificar se o usuário tem permissão para ver horários bloqueados
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();
        
        // Buscar o profissional
        Usuario profissional = profissionalService.buscarProfissional(profissionalId);
        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Validar permissões
        boolean podeVer = false;
        
        if (usuarioLogado.getTipoUsuario().equals("PROFISSIONAL") && 
            profissional.getIdUsuario().equals(usuarioLogado.getIdUsuario())) {
            podeVer = true;
        }
        
        if (usuarioLogado.getTipoUsuario().equals("PROPRIETARIO")) {
            // Verificar se o profissional pertence a algum salão do proprietário
            boolean profissionalPertenceSalao = salaoService.buscarSaloesPorProprietario(usuarioLogado.getIdUsuario())
                .stream()
                .anyMatch(salao -> salao.getProfissionais().contains(profissional));
            
            if (profissionalPertenceSalao) {
                podeVer = true;
            }
        }
        
        if (!podeVer) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        // Buscar horários bloqueados
        List<HorarioTrabalho> horarios = horarioTrabalhoService.buscarHorariosTrabalhoProfissional(profissionalId);
        
        // Filtrar apenas horários bloqueados
        List<HorarioTrabalho> horariosBloqueados = horarios.stream()
            .filter(HorarioTrabalho::isBloqueado)
            .collect(Collectors.toList());
        
        // Aplicar filtro de data se fornecido
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