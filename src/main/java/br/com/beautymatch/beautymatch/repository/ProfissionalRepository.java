package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository: Indica que esta interface é um componente de repositório do Spring
// JpaRepository: Interface que fornece métodos CRUD básicos e paginação
// Profissional: A entidade que este repositório gerencia
// Long: O tipo do ID da entidade Profissional
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    // Esta interface herda automaticamente vários métodos úteis como:
    // save(), findById(), findAll(), delete(), etc.
}