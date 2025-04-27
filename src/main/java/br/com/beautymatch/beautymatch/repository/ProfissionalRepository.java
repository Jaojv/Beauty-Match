package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// @Repository: Indica que esta interface é um componente de repositório do Spring
// JpaRepository: Interface que fornece métodos CRUD básicos e paginação
// Profissional: A entidade que este repositório gerencia
// Long: O tipo do ID da entidade Profissional
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    // Esta interface herda automaticamente vários métodos úteis como:
    // save(), findById(), findAll(), delete(), etc.
    
    List<Profissional> findBySalaoId(Long salaoId);
    
    List<Profissional> findByAtivo(boolean ativo);
    
    List<Profissional> findBySalaoIdAndAtivo(Long salaoId, boolean ativo);
    
    List<Profissional> findByNomeContainingIgnoreCase(String nome);
    
    List<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade);
    
    Optional<Profissional> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT p FROM Profissional p WHERE p.salao.id = :salaoId AND p.especialidade LIKE %:especialidade% AND p.ativo = true")
    List<Profissional> findBySalaoIdAndEspecialidade(@Param("salaoId") Long salaoId, @Param("especialidade") String especialidade);
}