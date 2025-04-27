package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.CategoriaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaServicoRepository extends JpaRepository<CategoriaServico, Long> {
    Optional<CategoriaServico> findByNome(String nome);
    List<CategoriaServico> findByAtivo(boolean ativo);
    List<CategoriaServico> findByNomeContainingIgnoreCase(String nome);
    boolean existsByNome(String nome);
} 