package com.beauty.com.MatchBeauty.controller;

import com.beauty.com.MatchBeauty.dto.AdminDTO;
import com.beauty.com.MatchBeauty.dto.CriarUsuarioDTO;
import com.beauty.com.MatchBeauty.dto.DashboardStatsDTO;
import com.beauty.com.MatchBeauty.dto.UsuarioAdminDTO;
import com.beauty.com.MatchBeauty.dto.SalaoAdminDTO;
import com.beauty.com.MatchBeauty.dto.AprovarSalaoDTO;
import com.beauty.com.MatchBeauty.dto.EditarSalaoDTO;
import com.beauty.com.MatchBeauty.dto.EditarUsuarioDTO;
import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.security.SecurityService;
import com.beauty.com.MatchBeauty.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller responsável por gerenciar operações relacionadas aos administradores
// Fornece endpoints para CRUD de administradores com controle de acesso baseado em roles
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Serviço para operações de administrador
    @Autowired
    private AdminService adminService;

    // Serviço de segurança para validação de permissões
    @Autowired
    private SecurityService securityService;

    // Endpoint para listar todos os administradores
    // Apenas administradores podem acessar esta funcionalidade
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Admin>> listarAdmins() {
        return ResponseEntity.ok(adminService.listarAdmins());
    }

    // Endpoint para buscar um administrador específico por ID
    // Apenas administradores podem acessar e apenas o próprio admin pode ver seus dados
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Admin> buscarAdmin(@PathVariable Long id) {
        Admin admin = adminService.buscarAdmin(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para criar um novo administrador
    // Apenas administradores podem criar outros administradores
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Admin> criarAdmin(@RequestBody AdminDTO dto) {
        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        admin.setTelefone(dto.getTelefone());
        admin.setNome(dto.getNome());
        admin.setNivelAcesso(dto.getNivelAcesso());
        
        Admin novoAdmin = adminService.criarAdmin(admin);
        return ResponseEntity.ok(novoAdmin);
    }

    // Endpoint para atualizar dados de um administrador
    // Apenas administradores podem atualizar e apenas o próprio admin pode atualizar seus dados
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Admin> atualizarAdmin(@PathVariable Long id, @RequestBody AdminDTO dto) {
        Admin admin = adminService.buscarAdmin(id);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        admin.setUsername(dto.getUsername());
        admin.setPassword(dto.getPassword());
        admin.setEmail(dto.getEmail());
        admin.setTelefone(dto.getTelefone());
        admin.setNome(dto.getNome());
        admin.setNivelAcesso(dto.getNivelAcesso());

        Admin adminAtualizado = adminService.atualizarAdmin(admin);
        return ResponseEntity.ok(adminAtualizado);
    }

    // Endpoint para deletar um administrador
    // Apenas administradores podem deletar e apenas o próprio admin pode deletar sua conta
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN') and @securityService.isAdminLogado(#id)")
    public ResponseEntity<Void> deletarAdmin(@PathVariable Long id) {
        if (adminService.deletarAdmin(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para buscar estatísticas do dashboard
    // Apenas administradores podem acessar as estatísticas
    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDTO> buscarEstatisticasDashboard() {
        DashboardStatsDTO stats = adminService.buscarEstatisticasDashboard();
        return ResponseEntity.ok(stats);
    }

    // Endpoint para listar todos os usuários
    // Apenas administradores podem acessar
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioAdminDTO>> listarTodosUsuarios() {
        List<UsuarioAdminDTO> usuarios = adminService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para criar novo usuário
    // Apenas administradores podem criar usuários
    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioAdminDTO> criarUsuario(@RequestBody CriarUsuarioDTO dto) {
        try {
            System.out.println("Controller: Recebendo requisição para criar usuário");
            System.out.println("Controller: Username: " + dto.getUsername());
            System.out.println("Controller: Nome: " + dto.getNome());
            System.out.println("Controller: Email: " + dto.getEmail());
            System.out.println("Controller: Tipo: " + dto.getTipoUsuario());
            
            UsuarioAdminDTO usuario = adminService.criarUsuario(dto);
            System.out.println("Controller: Usuário criado com sucesso");
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            System.err.println("Controller: Erro ao criar usuário: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("Controller: Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para deletar usuário
    // Apenas administradores podem deletar usuários
    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id, @RequestParam String tipoUsuario) {
        boolean deletado = adminService.deletarUsuario(id, tipoUsuario);
        if (deletado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * BUSCA UM USUÁRIO ESPECÍFICO POR ID
     * 
     * Retorna as informações detalhadas de um usuário específico
     * Apenas administradores podem acessar esta funcionalidade
     */
    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioAdminDTO> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            System.out.println("Controller: Recebendo requisição para buscar usuário ID: " + id);
            
            UsuarioAdminDTO usuario = adminService.buscarUsuarioPorId(id);
            
            if (usuario != null) {
                System.out.println("Controller: Usuário encontrado: " + usuario.getNome());
                return ResponseEntity.ok(usuario);
            } else {
                System.out.println("Controller: Usuário não encontrado para ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Controller: Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * EDITA INFORMAÇÕES DE UM USUÁRIO
     * 
     * Atualiza as informações de um usuário existente
     * Apenas administradores podem acessar esta funcionalidade
     */
    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioAdminDTO> editarUsuario(@PathVariable Long id, @RequestBody EditarUsuarioDTO dto) {
        try {
            System.out.println("Controller: Recebendo requisição para editar usuário");
            System.out.println("Controller: Usuário ID: " + id);
            
            // Garantir que o ID do path corresponde ao ID do DTO
            dto.setId(id);
            
            UsuarioAdminDTO usuarioEditado = adminService.editarUsuario(dto);
            System.out.println("Controller: Usuário editado com sucesso");
            
            return ResponseEntity.ok(usuarioEditado);
        } catch (IllegalArgumentException e) {
            System.err.println("Controller: Usuário não encontrado: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Controller: Erro ao editar usuário: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== GESTÃO DE SALÕES ====================

    /**
     * LISTA TODOS OS SALÕES
     * 
     * Retorna uma lista de todos os salões com informações detalhadas
     * Apenas administradores podem acessar esta funcionalidade
     */
    @GetMapping("/saloes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SalaoAdminDTO>> listarTodosSaloes() {
        try {
            System.out.println("Controller: Recebendo requisição para listar salões");
            
            List<SalaoAdminDTO> saloes = adminService.listarTodosSaloes();
            System.out.println("Controller: Salões listados com sucesso - " + saloes.size() + " salões");
            
            return ResponseEntity.ok(saloes);
        } catch (Exception e) {
            System.err.println("Controller: Erro ao listar salões: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * BUSCA UM SALÃO ESPECÍFICO POR ID
     * 
     * Retorna as informações detalhadas de um salão específico
     * Apenas administradores podem acessar esta funcionalidade
     */
    @GetMapping("/saloes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaoAdminDTO> buscarSalaoPorId(@PathVariable Long id) {
        try {
            System.out.println("Controller: Recebendo requisição para buscar salão ID: " + id);
            
            SalaoAdminDTO salao = adminService.buscarSalaoPorId(id);
            
            if (salao != null) {
                System.out.println("Controller: Salão encontrado: " + salao.getNome());
                return ResponseEntity.ok(salao);
            } else {
                System.out.println("Controller: Salão não encontrado para ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Controller: Erro ao buscar salão: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * APROVA OU REJEITA UM SALÃO
     * 
     * Altera o status de um salão para APROVADO ou REJEITADO
     * Apenas administradores podem acessar esta funcionalidade
     */
    @PostMapping("/saloes/aprovar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> aprovarRejeitarSalao(@RequestBody AprovarSalaoDTO dto) {
        try {
            System.out.println("Controller: Recebendo requisição para aprovar/rejeitar salão");
            System.out.println("Controller: Salão ID: " + dto.getSalaoId());
            System.out.println("Controller: Status: " + dto.getStatus());
            
            boolean resultado = adminService.aprovarRejeitarSalao(dto);
            
            if (resultado) {
                System.out.println("Controller: Salão aprovado/rejeitado com sucesso");
                return ResponseEntity.ok(true);
            } else {
                System.out.println("Controller: Salão não encontrado");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Controller: Erro ao aprovar/rejeitar salão: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * EDITA INFORMAÇÕES DE UM SALÃO
     * 
     * Atualiza as informações de um salão existente
     * Apenas administradores podem acessar esta funcionalidade
     */
    @PutMapping("/saloes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaoAdminDTO> editarSalao(@PathVariable Long id, @RequestBody EditarSalaoDTO dto) {
        try {
            System.out.println("Controller: Recebendo requisição para editar salão");
            System.out.println("Controller: Salão ID: " + id);
            
            // Garantir que o ID do path corresponde ao ID do DTO
            dto.setId(id);
            
            SalaoAdminDTO salaoEditado = adminService.editarSalao(dto);
            System.out.println("Controller: Salão editado com sucesso");
            
            return ResponseEntity.ok(salaoEditado);
        } catch (IllegalArgumentException e) {
            System.err.println("Controller: Salão não encontrado: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Controller: Erro ao editar salão: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
} 