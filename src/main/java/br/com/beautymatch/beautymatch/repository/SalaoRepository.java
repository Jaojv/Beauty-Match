package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Salao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalaoRepository extends JpaRepository<Salao, Long> {
    Optional<Salao> findByCnpj(String cnpj);
    List<Salao> findByAtivo(boolean ativo);
    List<Salao> findByNomeContainingIgnoreCase(String nome);
    List<Salao> findByCidadeContainingIgnoreCase(String cidade);
    List<Salao> findByEstadoContainingIgnoreCase(String estado);
    boolean existsByCnpj(String cnpj);
    
    @Query("SELECT s FROM Salao s WHERE " +
           "LOWER(s.logradouro) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(s.bairro) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(s.cidade) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(s.estado) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(s.cep) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Salao> buscarPorEndereco(@Param("termo") String termo);
} 