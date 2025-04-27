package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public Usuario salvar(Usuario usuario) {
        // Verificar se já existe um usuário com o mesmo email
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este email");
        }
        
        // Verificar se já existe um usuário com o mesmo CPF
        Optional<Usuario> usuarioCpfExistente = usuarioRepository.findByCpf(usuario.getCpf());
        if (usuarioCpfExistente.isPresent() && !usuarioCpfExistente.get().getId().equals(usuario.getId())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este CPF");
        }
        
        // Se for uma nova senha, criptografar
        if (usuario.getId() == null || usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            // Se for atualização e não tiver senha nova, manter a senha atual
            Optional<Usuario> usuarioAtual = usuarioRepository.findById(usuario.getId());
            if (usuarioAtual.isPresent()) {
                usuario.setSenha(usuarioAtual.get().getSenha());
            }
        }
        
        return usuarioRepository.save(usuario);
    }
    
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
    
    public List<Usuario> buscarPorTipo(Usuario.TipoUsuario tipo) {
        return usuarioRepository.findByTipo(tipo);
    }
    
    public List<Usuario> buscarPorAtivo(boolean ativo) {
        return usuarioRepository.findByAtivo(ativo);
    }
    
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }
    
    @Transactional
    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
    
    @Transactional
    public Usuario atualizarStatus(Long id, boolean ativo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setAtivo(ativo);
        
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar se a senha atual está correta
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new RuntimeException("Senha atual incorreta");
        }
        
        // Atualizar a senha
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        
        return usuarioRepository.save(usuario);
    }
    
    @Transactional
    public Usuario atualizarTipo(Long id, Usuario.TipoUsuario tipo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        Usuario usuario = usuarioOpt.get();
        usuario.setTipo(tipo);
        
        return usuarioRepository.save(usuario);
    }
} 