package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.HorarioBloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface HorarioBloqueadoRepository extends JpaRepository<HorarioBloqueado, Long> {
    
    List<HorarioBloqueado> findByProfissionalId(Long profissionalId);
    
    List<HorarioBloqueado> findByProfissionalIdAndDataBloqueio(Long profissionalId, LocalDate dataBloqueio);
    
    @Query("SELECT h FROM HorarioBloqueado h WHERE h.profissional.id = :profissionalId AND h.dataBloqueio = :dataBloqueio AND " +
           "((h.horaInicio <= :horaInicio AND h.horaFim > :horaInicio) OR " +
           "(h.horaInicio < :horaFim AND h.horaFim >= :horaFim) OR " +
           "(h.horaInicio >= :horaInicio AND h.horaFim <= :horaFim))")
    List<HorarioBloqueado> findConflitosHorario(
            @Param("profissionalId") Long profissionalId,
            @Param("dataBloqueio") LocalDate dataBloqueio,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFim") LocalTime horaFim);
} 