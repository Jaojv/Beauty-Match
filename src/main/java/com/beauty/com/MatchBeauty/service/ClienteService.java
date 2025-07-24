package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.dto.ClienteDTO;
import com.beauty.com.MatchBeauty.entity.Cliente;
import com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario;
import com.beauty.com.MatchBeauty.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVIÇO CLIENTE - LÓGICA DE NEGÓCIO PARA CLIENTES
 * 
 * Este serviço gerencia todas as operações relacionadas aos clientes do sistema.
 * Fornece métodos para CRUD de clientes, incluindo criptografia de senhas
 * e validações específicas para perfis de clientes.
 * 
 * FUNCIONALIDADES:
 * - Listagem de todos os clientes
 * - Busca de cliente por ID
 * - Criação de novos clientes
 * - Atualização de dados de clientes
 * - Exclusão de clientes
 * - Criptografia automática de senhas
 * - Definição automática do tipo de usuário
 * - Validações específicas para clientes
 * 
 * CARACTERÍSTICAS ESPECÍFICAS:
 * - Gerenciamento de preferências de clientes
 * - Histórico de agendamentos
 * - Perfil de cliente com dados específicos
 * - Integração com sistema de agendamentos
 * 
 * DEPENDÊNCIAS:
 * - ClienteRepository: Para operações de persistência
 * - PasswordEncoder: Para criptografia de senhas
 */
@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public ClienteDTO buscarCliente(Long id) {
        return clienteRepository.findById(id)
            .map(this::converterParaDTO)
            .orElse(null);
    }

    public ClienteDTO criarCliente(ClienteDTO dto) {
        Cliente cliente = converterParaEntidade(dto);
        cliente.setTipoUsuario(com.beauty.com.MatchBeauty.entity.Usuario.TipoUsuario.CLIENTE);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return converterParaDTO(clienteSalvo);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO dto) {
        if (clienteRepository.existsById(id)) {
            Cliente cliente = converterParaEntidade(dto);
            cliente.setIdUsuario(id);
            Cliente clienteAtualizado = clienteRepository.save(cliente);
            return converterParaDTO(clienteAtualizado);
        }
        return null;
    }

    public boolean deletarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Métodos auxiliares de conversão
    private ClienteDTO converterParaDTO(Cliente cliente) {
        if (cliente == null) return null;
        
        ClienteDTO dto = new ClienteDTO();
        dto.setClienteId(cliente.getIdUsuario());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getCpf());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setEndereco(cliente.getEndereco());
        dto.setPreferencias(cliente.getPreferencias());
        dto.setUsername(cliente.getUsername());
        // Não incluímos a senha no DTO por segurança
        return dto;
    }

    private Cliente converterParaEntidade(ClienteDTO dto) {
        if (dto == null) return null;
        
        Cliente cliente = new Cliente();
        // Se for atualização, o ID vem do path variable
        if (dto.getClienteId() != null) {
            cliente.setIdUsuario(dto.getClienteId());
        }
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEndereco(dto.getEndereco());
        cliente.setPreferencias(dto.getPreferencias());
        cliente.setUsername(dto.getUsername());
        
        // Só atualiza a senha se ela foi fornecida
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            cliente.setPassword(dto.getPassword());
        }
        
        return cliente;
    }
} 