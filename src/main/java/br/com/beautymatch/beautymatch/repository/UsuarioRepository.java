package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByCpf(String cpf);
    
    List<Usuario> findByTipo(Usuario.TipoUsuario tipo);
    
    List<Usuario> findByAtivo(boolean ativo);
    
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
} 