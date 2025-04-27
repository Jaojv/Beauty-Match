package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    
    List<Avaliacao> findByClienteId(Long clienteId);
    
    List<Avaliacao> findByServicoId(Long servicoId);
    
    List<Avaliacao> findByProfissionalId(Long profissionalId);
    
    List<Avaliacao> findByAgendamentoId(Long agendamentoId);
    
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.servico.id = :servicoId")
    Double getMediaAvaliacaoServico(@Param("servicoId") Long servicoId);
    
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.profissional.id = :profissionalId")
    Double getMediaAvaliacaoProfissional(@Param("profissionalId") Long profissionalId);
    
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.servico.salao.id = :salaoId")
    Double getMediaAvaliacaoSalao(@Param("salaoId") Long salaoId);
} 