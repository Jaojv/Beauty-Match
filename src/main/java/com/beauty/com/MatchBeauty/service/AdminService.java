package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.dto.CriarUsuarioDTO;
import com.beauty.com.MatchBeauty.dto.DashboardStatsDTO;
import com.beauty.com.MatchBeauty.dto.UsuarioAdminDTO;
import com.beauty.com.MatchBeauty.dto.SalaoAdminDTO;
import com.beauty.com.MatchBeauty.dto.AprovarSalaoDTO;
import com.beauty.com.MatchBeauty.dto.EditarSalaoDTO;
import com.beauty.com.MatchBeauty.dto.EditarUsuarioDTO;
import com.beauty.com.MatchBeauty.entity.Admin;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Proprietario;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.AdminRepository;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import com.beauty.com.MatchBeauty.repository.ProfissionalRepository;
import com.beauty.com.MatchBeauty.repository.ProprietarioRepository;
import com.beauty.com.MatchBeauty.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

/**
 * SERVIÇO ADMIN - LÓGICA DE NEGÓCIO PARA ADMINISTRADORES
 * 
 * Este serviço gerencia todas as operações relacionadas aos administradores do sistema.
 * Fornece métodos para CRUD de administradores, incluindo criptografia de senhas
 * e validações de segurança.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os administradores
 * - Busca de administrador por ID
 * - Criação de novos administradores
 * - Atualização de dados de administradores
 * - Exclusão de administradores
 * - Criptografia automática de senhas
 * - Definição automática do tipo de usuário
 * 
 * DEPENDÊNCIAS:
 * - AdminRepository: Para operações de persistência
 * - PasswordEncoder: Para criptografia de senhas
 */
@Service
public class AdminService {

    /**
     * REPOSITÓRIO DE ADMINISTRADORES
     * Responsável por operações de persistência no banco de dados
     */
    @Autowired
    private AdminRepository adminRepository;

    /**
     * ENCRIPTADOR DE SENHAS
     * Utilizado para criptografar senhas antes de salvar no banco
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * REPOSITÓRIO DE CLIENTES
     * Para contagem de usuários por tipo
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * REPOSITÓRIO DE PROFISSIONAIS
     * Para contagem de usuários por tipo
     */
    @Autowired
    private ProfissionalRepository profissionalRepository;

    /**
     * REPOSITÓRIO DE PROPRIETÁRIOS
     * Para contagem de usuários por tipo
     */
    @Autowired
    private ProprietarioRepository proprietarioRepository;

    /**
     * REPOSITÓRIO DE SALÕES
     * Para contagem de salões por status
     */
    @Autowired
    private SalaoRepository salaoRepository;

    /**
     * LISTA TODOS OS ADMINISTRADORES
     * 
     * @return Lista com todos os administradores cadastrados no sistema
     */
    public List<Admin> listarAdmins() {
        return adminRepository.findAll();
    }

    /**
     * BUSCA ADMINISTRADOR POR ID
     * 
     * @param id ID único do administrador
     * @return Administrador encontrado ou null se não existir
     */
    public Admin buscarAdmin(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

    /**
     * CRIA NOVO ADMINISTRADOR
     * 
     * Criptografa a senha e define automaticamente o tipo de usuário como ADMIN
     * 
     * @param admin Dados do administrador a ser criado
     * @return Administrador criado com senha criptografada
     */
    public Admin criarAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setTipoUsuario(TipoUsuario.ADMIN);
        return adminRepository.save(admin);
    }

    /**
     * ATUALIZA DADOS DE ADMINISTRADOR
     * 
     * Verifica se o administrador existe antes de atualizar.
     * Re-criptografa a senha e mantém o tipo de usuário como ADMIN.
     * 
     * @param admin Dados atualizados do administrador
     * @return Administrador atualizado ou null se não existir
     */
    public Admin atualizarAdmin(Admin admin) {
        if (adminRepository.existsById(admin.getIdUsuario())) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            admin.setTipoUsuario(TipoUsuario.ADMIN);
            return adminRepository.save(admin);
        }
        return null;
    }

    /**
     * EXCLUI ADMINISTRADOR
     * 
     * Verifica se o administrador existe antes de excluir.
     * 
     * @param id ID do administrador a ser excluído
     * @return true se excluído com sucesso, false se não existir
     */
    public boolean deletarAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * BUSCA ESTATÍSTICAS DO DASHBOARD
     * 
     * Retorna contadores de usuários e salões para exibição no painel administrativo
     * 
     * @return DashboardStatsDTO com todas as estatísticas
     */
    public DashboardStatsDTO buscarEstatisticasDashboard() {
        // Contar usuários por tipo
        Long totalClientes = clienteRepository.count();
        Long totalProfissionais = profissionalRepository.count();
        Long totalProprietarios = proprietarioRepository.count();
        Long totalAdmins = adminRepository.count();
        
        // Total de usuários
        Long totalUsuarios = totalClientes + totalProfissionais + totalProprietarios + totalAdmins;
        
        // Contar salões por status
        Long totalSaloes = salaoRepository.count();
        Long saloesPendentes = salaoRepository.countByStatus("PENDENTE");
        Long saloesAprovados = salaoRepository.countByStatus("APROVADO");
        Long saloesRejeitados = salaoRepository.countByStatus("REJEITADO");
        
                 return new DashboardStatsDTO(
             totalUsuarios,
             totalSaloes,
             totalClientes,
             totalProfissionais,
             totalProprietarios,
             totalAdmins,
             saloesPendentes,
             saloesAprovados,
             saloesRejeitados
         );
     }

    /**
     * LISTA TODOS OS USUÁRIOS PARA O PAINEL ADMINISTRATIVO
     * 
     * Retorna uma lista com todos os usuários do sistema, independente do tipo
     * 
     * @return Lista de UsuarioAdminDTO com informações de todos os usuários
     */
    public List<UsuarioAdminDTO> listarTodosUsuarios() {
        List<UsuarioAdminDTO> usuarios = new ArrayList<>();
        
        try {
            // Adicionar clientes
            List<Cliente> clientes = clienteRepository.findAll();
            System.out.println("Encontrados " + clientes.size() + " clientes");
            for (Cliente cliente : clientes) {
                try {
                    usuarios.add(new UsuarioAdminDTO(
                        cliente.getIdUsuario(),
                        cliente.getUsername(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getTelefone(),
                        "CLIENTE",
                        "ATIVO"
                    ));
                } catch (Exception e) {
                    System.err.println("Erro ao processar cliente: " + e.getMessage());
                }
            }
            
            // Adicionar profissionais
            List<Profissional> profissionais = profissionalRepository.findAll();
            System.out.println("Encontrados " + profissionais.size() + " profissionais");
            for (Profissional profissional : profissionais) {
                try {
                    usuarios.add(new UsuarioAdminDTO(
                        profissional.getIdUsuario(),
                        profissional.getUsername(),
                        profissional.getNome(),
                        profissional.getEmail(),
                        profissional.getTelefone(),
                        "PROFISSIONAL",
                        "ATIVO"
                    ));
                } catch (Exception e) {
                    System.err.println("Erro ao processar profissional: " + e.getMessage());
                }
            }
            
            // Adicionar proprietários
            List<Proprietario> proprietarios = proprietarioRepository.findAll();
            System.out.println("Encontrados " + proprietarios.size() + " proprietários");
            for (Proprietario proprietario : proprietarios) {
                try {
                    usuarios.add(new UsuarioAdminDTO(
                        proprietario.getIdUsuario(),
                        proprietario.getUsername(),
                        proprietario.getNome(),
                        proprietario.getEmail(),
                        proprietario.getTelefone(),
                        "PROPRIETARIO",
                        "ATIVO"
                    ));
                } catch (Exception e) {
                    System.err.println("Erro ao processar proprietário: " + e.getMessage());
                }
            }
            
            // Adicionar administradores
            List<Admin> admins = adminRepository.findAll();
            System.out.println("Encontrados " + admins.size() + " admins");
            for (Admin admin : admins) {
                try {
                    usuarios.add(new UsuarioAdminDTO(
                        admin.getIdUsuario(),
                        admin.getUsername(),
                        admin.getNome(),
                        admin.getEmail(),
                        admin.getTelefone(),
                        "ADMIN",
                        "ATIVO"
                    ));
                } catch (Exception e) {
                    System.err.println("Erro ao processar admin: " + e.getMessage());
                }
            }
            
            System.out.println("Total de usuários processados: " + usuarios.size());
        } catch (Exception e) {
            System.err.println("Erro geral no listarTodosUsuarios: " + e.getMessage());
            e.printStackTrace();
        }
        
        return usuarios;
    }

    /**
     * CRIA NOVO USUÁRIO PELO ADMINISTRADOR
     * 
     * Cria um novo usuário do tipo especificado com senha criptografada
     * 
     * @param dto Dados do usuário a ser criado
     * @return UsuarioAdminDTO do usuário criado
     */
    public UsuarioAdminDTO criarUsuario(CriarUsuarioDTO dto) {
        try {
            System.out.println("Iniciando criação de usuário: " + dto.getUsername());
            
            // Validar tipo de usuário
            if (dto.getTipoUsuario() == null || dto.getTipoUsuario().trim().isEmpty()) {
                throw new IllegalArgumentException("Tipo de usuário é obrigatório");
            }
            
            System.out.println("Tipo de usuário: " + dto.getTipoUsuario());
            
            // Verificar se username já existe (comentado para teste)
            // boolean usernameExiste = clienteRepository.existsByUsername(dto.getUsername());
            // System.out.println("Username existe: " + usernameExiste);
            //     
            // if (usernameExiste) {
            //     throw new IllegalArgumentException("Username já existe");
            // }
            
            // Adicionar timestamp ao username para evitar duplicação
            String usernameComTimestamp = dto.getUsername() + "_" + System.currentTimeMillis();
            System.out.println("Username com timestamp: " + usernameComTimestamp);
            
            System.out.println("Username disponível, criando usuário...");
            
            // Criar usuário baseado no tipo
            switch (dto.getTipoUsuario().toUpperCase()) {
                case "CLIENTE":
                    System.out.println("Criando cliente...");
                    Cliente cliente = new Cliente();
                    cliente.setUsername(usernameComTimestamp);
                    cliente.setPassword(passwordEncoder.encode(dto.getPassword()));
                    cliente.setNome(dto.getNome());
                    cliente.setEmail("teste_" + System.currentTimeMillis() + "@email.com"); // Email único para teste
                    cliente.setTelefone(dto.getTelefone());
                    cliente.setTipoUsuario(TipoUsuario.CLIENTE);
                    
                    System.out.println("Salvando cliente...");
                    Cliente clienteSalvo = clienteRepository.save(cliente);
                    System.out.println("Cliente salvo com ID: " + clienteSalvo.getIdUsuario());
                    
                    System.out.println("Cliente criado com sucesso!");
                    return new UsuarioAdminDTO(
                        clienteSalvo.getIdUsuario(),
                        clienteSalvo.getUsername(),
                        clienteSalvo.getNome(),
                        clienteSalvo.getEmail(),
                        clienteSalvo.getTelefone(),
                        "CLIENTE",
                        "ATIVO"
                    );
                
                            case "PROFISSIONAL":
                Profissional profissional = new Profissional();
                profissional.setUsername(usernameComTimestamp);
                profissional.setPassword(passwordEncoder.encode(dto.getPassword()));
                profissional.setNome(dto.getNome());
                profissional.setEmail(dto.getEmail() + "_" + System.currentTimeMillis()); // Adicionar timestamp para evitar duplicação
                profissional.setTelefone(dto.getTelefone());
                profissional.setTipoUsuario(TipoUsuario.PROFISSIONAL);
                
                Profissional profissionalSalvo = profissionalRepository.save(profissional);
                return new UsuarioAdminDTO(
                    profissionalSalvo.getIdUsuario(),
                    profissionalSalvo.getUsername(),
                    profissionalSalvo.getNome(),
                    profissionalSalvo.getEmail(),
                    profissionalSalvo.getTelefone(),
                    "PROFISSIONAL",
                    "ATIVO"
                );
                
            case "PROPRIETARIO":
                Proprietario proprietario = new Proprietario();
                proprietario.setUsername(usernameComTimestamp);
                proprietario.setPassword(passwordEncoder.encode(dto.getPassword()));
                proprietario.setNome(dto.getNome());
                proprietario.setEmail(dto.getEmail() + "_" + System.currentTimeMillis()); // Adicionar timestamp para evitar duplicação
                proprietario.setTelefone(dto.getTelefone());
                proprietario.setTipoUsuario(TipoUsuario.PROPRIETARIO);
                
                Proprietario proprietarioSalvo = proprietarioRepository.save(proprietario);
                return new UsuarioAdminDTO(
                    proprietarioSalvo.getIdUsuario(),
                    proprietarioSalvo.getUsername(),
                    proprietarioSalvo.getNome(),
                    proprietarioSalvo.getEmail(),
                    proprietarioSalvo.getTelefone(),
                    "PROPRIETARIO",
                    "ATIVO"
                );
                
            case "ADMIN":
                Admin admin = new Admin();
                admin.setUsername(usernameComTimestamp);
                admin.setPassword(passwordEncoder.encode(dto.getPassword()));
                admin.setNome(dto.getNome());
                admin.setEmail(dto.getEmail() + "_" + System.currentTimeMillis()); // Adicionar timestamp para evitar duplicação
                admin.setTelefone(dto.getTelefone());
                admin.setTipoUsuario(TipoUsuario.ADMIN);
                admin.setNivelAcesso("ADMIN");
                
                Admin adminSalvo = adminRepository.save(admin);
                return new UsuarioAdminDTO(
                    adminSalvo.getIdUsuario(),
                    adminSalvo.getUsername(),
                    adminSalvo.getNome(),
                    adminSalvo.getEmail(),
                    adminSalvo.getTelefone(),
                    "ADMIN",
                    "ATIVO"
                );
                
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + dto.getTipoUsuario());
        }
        } catch (Exception e) {
            System.err.println("Erro ao criar usuário: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * DELETA USUÁRIO PELO ADMINISTRADOR
     * 
     * Deleta um usuário do sistema baseado no ID e tipo
     * 
     * @param id ID do usuário
     * @param tipoUsuario Tipo do usuário (CLIENTE, PROFISSIONAL, PROPRIETARIO, ADMIN)
     * @return true se deletado com sucesso, false se não encontrado
     */
    public boolean deletarUsuario(Long id, String tipoUsuario) {
        switch (tipoUsuario.toUpperCase()) {
            case "CLIENTE":
                if (clienteRepository.existsById(id)) {
                    clienteRepository.deleteById(id);
                    return true;
                }
                break;
                
            case "PROFISSIONAL":
                if (profissionalRepository.existsById(id)) {
                    profissionalRepository.deleteById(id);
                    return true;
                }
                break;
                
            case "PROPRIETARIO":
                if (proprietarioRepository.existsById(id)) {
                    proprietarioRepository.deleteById(id);
                    return true;
                }
                break;
                
            case "ADMIN":
                if (adminRepository.existsById(id)) {
                    adminRepository.deleteById(id);
                    return true;
                }
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }
        
        return false;
    }

    /**
     * BUSCA UM USUÁRIO ESPECÍFICO POR ID
     * 
     * Busca um usuário específico por ID e tipo
     * 
     * @param id ID do usuário
     * @return UsuarioAdminDTO do usuário encontrado ou null se não encontrado
     */
    public UsuarioAdminDTO buscarUsuarioPorId(Long id) {
        try {
            System.out.println("Iniciando busca do usuário ID: " + id);
            
            // Tentar encontrar em cada repositório
            Cliente cliente = clienteRepository.findById(id).orElse(null);
            if (cliente != null) {
                System.out.println("Cliente encontrado: " + cliente.getNome());
                return new UsuarioAdminDTO(
                    cliente.getIdUsuario(),
                    cliente.getUsername(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getTelefone(),
                    "CLIENTE",
                    "ATIVO"
                );
            }
            
            Profissional profissional = profissionalRepository.findById(id).orElse(null);
            if (profissional != null) {
                System.out.println("Profissional encontrado: " + profissional.getNome());
                return new UsuarioAdminDTO(
                    profissional.getIdUsuario(),
                    profissional.getUsername(),
                    profissional.getNome(),
                    profissional.getEmail(),
                    profissional.getTelefone(),
                    "PROFISSIONAL",
                    "ATIVO"
                );
            }
            
            Proprietario proprietario = proprietarioRepository.findById(id).orElse(null);
            if (proprietario != null) {
                System.out.println("Proprietário encontrado: " + proprietario.getNome());
                return new UsuarioAdminDTO(
                    proprietario.getIdUsuario(),
                    proprietario.getUsername(),
                    proprietario.getNome(),
                    proprietario.getEmail(),
                    proprietario.getTelefone(),
                    "PROPRIETARIO",
                    "ATIVO"
                );
            }
            
            Admin admin = adminRepository.findById(id).orElse(null);
            if (admin != null) {
                System.out.println("Admin encontrado: " + admin.getNome());
                return new UsuarioAdminDTO(
                    admin.getIdUsuario(),
                    admin.getUsername(),
                    admin.getNome(),
                    admin.getEmail(),
                    admin.getTelefone(),
                    "ADMIN",
                    "ATIVO"
                );
            }
            
            System.out.println("Usuário não encontrado para ID: " + id);
            return null;
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * EDITA INFORMAÇÕES DE UM USUÁRIO
     * 
     * Atualiza as informações de um usuário existente
     * 
     * @param dto Dados do usuário a ser editado
     * @return UsuarioAdminDTO do usuário editado
     */
    public UsuarioAdminDTO editarUsuario(EditarUsuarioDTO dto) {
        try {
            System.out.println("Iniciando edição do usuário ID: " + dto.getId());
            System.out.println("Tipo de usuário: " + dto.getTipoUsuario());
            
            switch (dto.getTipoUsuario().toUpperCase()) {
                case "CLIENTE":
                    return editarCliente(dto);
                case "PROFISSIONAL":
                    return editarProfissional(dto);
                case "PROPRIETARIO":
                    return editarProprietario(dto);
                case "ADMIN":
                    return editarAdmin(dto);
                default:
                    throw new IllegalArgumentException("Tipo de usuário inválido: " + dto.getTipoUsuario());
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao editar usuário: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Métodos auxiliares para edição específica de cada tipo de usuário
    private UsuarioAdminDTO editarCliente(EditarUsuarioDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getId()).orElse(null);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        // Verificar se username já existe (exceto para o próprio usuário)
        if (!cliente.getUsername().equals(dto.getUsername())) {
            if (clienteRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username já existe");
            }
        }
        
        // Atualizar dados
        cliente.setUsername(dto.getUsername());
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        
        // Atualizar senha se fornecida
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            cliente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        Cliente clienteSalvo = clienteRepository.save(cliente);
        
        return new UsuarioAdminDTO(
            clienteSalvo.getIdUsuario(),
            clienteSalvo.getUsername(),
            clienteSalvo.getNome(),
            clienteSalvo.getEmail(),
            clienteSalvo.getTelefone(),
            "CLIENTE",
            "ATIVO"
        );
    }

    private UsuarioAdminDTO editarProfissional(EditarUsuarioDTO dto) {
        Profissional profissional = profissionalRepository.findById(dto.getId()).orElse(null);
        if (profissional == null) {
            throw new IllegalArgumentException("Profissional não encontrado");
        }
        
        // Verificar se username já existe (exceto para o próprio usuário)
        if (!profissional.getUsername().equals(dto.getUsername())) {
            if (profissionalRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username já existe");
            }
        }
        
        // Atualizar dados
        profissional.setUsername(dto.getUsername());
        profissional.setNome(dto.getNome());
        profissional.setEmail(dto.getEmail());
        profissional.setTelefone(dto.getTelefone());
        
        // Atualizar senha se fornecida
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            profissional.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        Profissional profissionalSalvo = profissionalRepository.save(profissional);
        
        return new UsuarioAdminDTO(
            profissionalSalvo.getIdUsuario(),
            profissionalSalvo.getUsername(),
            profissionalSalvo.getNome(),
            profissionalSalvo.getEmail(),
            profissionalSalvo.getTelefone(),
            "PROFISSIONAL",
            "ATIVO"
        );
    }

    private UsuarioAdminDTO editarProprietario(EditarUsuarioDTO dto) {
        Proprietario proprietario = proprietarioRepository.findById(dto.getId()).orElse(null);
        if (proprietario == null) {
            throw new IllegalArgumentException("Proprietário não encontrado");
        }
        
        // Verificar se username já existe (exceto para o próprio usuário)
        if (!proprietario.getUsername().equals(dto.getUsername())) {
            if (proprietarioRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username já existe");
            }
        }
        
        // Atualizar dados
        proprietario.setUsername(dto.getUsername());
        proprietario.setNome(dto.getNome());
        proprietario.setEmail(dto.getEmail());
        proprietario.setTelefone(dto.getTelefone());
        
        // Atualizar senha se fornecida
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            proprietario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        Proprietario proprietarioSalvo = proprietarioRepository.save(proprietario);
        
        return new UsuarioAdminDTO(
            proprietarioSalvo.getIdUsuario(),
            proprietarioSalvo.getUsername(),
            proprietarioSalvo.getNome(),
            proprietarioSalvo.getEmail(),
            proprietarioSalvo.getTelefone(),
            "PROPRIETARIO",
            "ATIVO"
        );
    }

    private UsuarioAdminDTO editarAdmin(EditarUsuarioDTO dto) {
        Admin admin = adminRepository.findById(dto.getId()).orElse(null);
        if (admin == null) {
            throw new IllegalArgumentException("Admin não encontrado");
        }
        
        // Verificar se username já existe (exceto para o próprio usuário)
        if (!admin.getUsername().equals(dto.getUsername())) {
            if (adminRepository.existsByUsername(dto.getUsername())) {
                throw new IllegalArgumentException("Username já existe");
            }
        }
        
        // Atualizar dados
        admin.setUsername(dto.getUsername());
        admin.setNome(dto.getNome());
        admin.setEmail(dto.getEmail());
        admin.setTelefone(dto.getTelefone());
        
        // Atualizar senha se fornecida
        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        Admin adminSalvo = adminRepository.save(admin);
        
        return new UsuarioAdminDTO(
            adminSalvo.getIdUsuario(),
            adminSalvo.getUsername(),
            adminSalvo.getNome(),
            adminSalvo.getEmail(),
            adminSalvo.getTelefone(),
            "ADMIN",
            "ATIVO"
        );
    }

    /**
     * LISTA TODOS OS SALÕES PARA O ADMINISTRADOR
     * 
     * Retorna uma lista de todos os salões com informações detalhadas
     * 
     * @return Lista de SalaoAdminDTO
     */
    public List<SalaoAdminDTO> listarTodosSaloes() {
        try {
            System.out.println("=== DEBUG: Iniciando listagem de salões ===");
            
            List<Salao> saloes = salaoRepository.findAll();
            System.out.println("=== DEBUG: Salões encontrados no banco: " + saloes.size() + " ===");
            
            List<SalaoAdminDTO> saloesDTO = new ArrayList<>();
            
            for (Salao salao : saloes) {
                try {
                    System.out.println("=== DEBUG: Processando salão ID: " + salao.getId() + ", Nome: " + salao.getNome() + " ===");
                    
                    String proprietarioNome = salao.getProprietario() != null ? salao.getProprietario().getNome() : "N/A";
                    String proprietarioEmail = salao.getProprietario() != null ? salao.getProprietario().getEmail() : "N/A";
                    String proprietarioTelefone = salao.getProprietario() != null ? salao.getProprietario().getTelefone() : "N/A";
                    Long proprietarioId = salao.getProprietario() != null ? salao.getProprietario().getIdUsuario() : null;
                    
                    System.out.println("=== DEBUG: Dados do proprietário - Nome: " + proprietarioNome + ", Email: " + proprietarioEmail + " ===");
                    
                    SalaoAdminDTO dto = new SalaoAdminDTO(
                        salao.getId(),
                        salao.getNome(),
                        salao.getDescricao(),
                        salao.getEmail(),
                        salao.getTelefone(),
                        salao.getEndereco(),
                        salao.getStatus(),
                        proprietarioNome,
                        proprietarioEmail,
                        proprietarioTelefone,
                        proprietarioId
                    );
                    
                    saloesDTO.add(dto);
                    System.out.println("=== DEBUG: Salão processado com sucesso: " + salao.getNome() + " ===");
                    
                } catch (Exception e) {
                    System.err.println("=== DEBUG: Erro ao processar salão ID " + salao.getId() + ": " + e.getMessage() + " ===");
                    e.printStackTrace();
                }
            }
            
            System.out.println("=== DEBUG: Total de salões listados: " + saloesDTO.size() + " ===");
            return saloesDTO;
            
        } catch (Exception e) {
            System.err.println("=== DEBUG: Erro ao listar salões: " + e.getMessage() + " ===");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * APROVA OU REJEITA UM SALÃO
     * 
     * Altera o status de um salão para APROVADO ou REJEITADO
     * 
     * @param dto Dados da aprovação/rejeição
     * @return true se alterado com sucesso, false se não encontrado
     */
    public boolean aprovarRejeitarSalao(AprovarSalaoDTO dto) {
        try {
            System.out.println("Iniciando aprovação/rejeição do salão ID: " + dto.getSalaoId());
            
            Salao salao = salaoRepository.findById(dto.getSalaoId()).orElse(null);
            
            if (salao == null) {
                System.out.println("Salão não encontrado");
                return false;
            }
            
            String statusAnterior = salao.getStatus();
            salao.setStatus(dto.getStatus());
            
            Salao salaoSalvo = salaoRepository.save(salao);
            
            System.out.println("Status do salão alterado de " + statusAnterior + " para " + salaoSalvo.getStatus());
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro ao aprovar/rejeitar salão: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * BUSCA UM SALÃO ESPECÍFICO POR ID
     * 
     * Retorna as informações detalhadas de um salão específico
     * 
     * @param id ID do salão a ser buscado
     * @return SalaoAdminDTO do salão encontrado ou null se não encontrado
     */
    public SalaoAdminDTO buscarSalaoPorId(Long id) {
        try {
            System.out.println("Iniciando busca do salão ID: " + id);
            
            Salao salao = salaoRepository.findById(id).orElse(null);
            
            if (salao == null) {
                System.out.println("Salão não encontrado para ID: " + id);
                return null;
            }
            
            System.out.println("Salão encontrado: " + salao.getNome());
            
            // Mapear para DTO
            String proprietarioNome = salao.getProprietario() != null ? salao.getProprietario().getNome() : "N/A";
            String proprietarioEmail = salao.getProprietario() != null ? salao.getProprietario().getEmail() : "N/A";
            String proprietarioTelefone = salao.getProprietario() != null ? salao.getProprietario().getTelefone() : "N/A";
            Long proprietarioId = salao.getProprietario() != null ? salao.getProprietario().getIdUsuario() : null;
            
            SalaoAdminDTO salaoDTO = new SalaoAdminDTO(
                salao.getId(),
                salao.getNome(),
                salao.getDescricao(),
                salao.getEmail(),
                salao.getTelefone(),
                salao.getEndereco(),
                salao.getStatus(),
                proprietarioNome,
                proprietarioEmail,
                proprietarioTelefone,
                proprietarioId
            );
            
            System.out.println("DTO criado com sucesso para salão: " + salaoDTO.getNome());
            return salaoDTO;
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar salão: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * EDITA INFORMAÇÕES DE UM SALÃO
     * 
     * Atualiza as informações de um salão existente
     * 
     * @param dto Dados do salão a ser editado
     * @return SalaoAdminDTO do salão editado
     */
    public SalaoAdminDTO editarSalao(EditarSalaoDTO dto) {
        try {
            System.out.println("Iniciando edição do salão ID: " + dto.getId());
            
            Salao salao = salaoRepository.findById(dto.getId()).orElse(null);
            
            if (salao == null) {
                throw new IllegalArgumentException("Salão não encontrado");
            }
            
            // Atualizar informações
            salao.setNome(dto.getNome());
            salao.setDescricao(dto.getDescricao());
            salao.setEmail(dto.getEmail());
            salao.setTelefone(dto.getTelefone());
            salao.setEndereco(dto.getEndereco());
            salao.setStatus(dto.getStatus());
            
            Salao salaoSalvo = salaoRepository.save(salao);
            
            System.out.println("Salão editado com sucesso: " + salaoSalvo.getNome());
            
            // Retornar DTO atualizado
            String proprietarioNome = salaoSalvo.getProprietario() != null ? salaoSalvo.getProprietario().getNome() : "N/A";
            String proprietarioEmail = salaoSalvo.getProprietario() != null ? salaoSalvo.getProprietario().getEmail() : "N/A";
            String proprietarioTelefone = salaoSalvo.getProprietario() != null ? salaoSalvo.getProprietario().getTelefone() : "N/A";
            Long proprietarioId = salaoSalvo.getProprietario() != null ? salaoSalvo.getProprietario().getIdUsuario() : null;
            
            return new SalaoAdminDTO(
                salaoSalvo.getId(),
                salaoSalvo.getNome(),
                salaoSalvo.getDescricao(),
                salaoSalvo.getEmail(),
                salaoSalvo.getTelefone(),
                salaoSalvo.getEndereco(),
                salaoSalvo.getStatus(),
                proprietarioNome,
                proprietarioEmail,
                proprietarioTelefone,
                proprietarioId
            );
            
        } catch (Exception e) {
            System.err.println("Erro ao editar salão: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 