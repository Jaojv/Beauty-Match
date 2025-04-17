package br.com.beautymatch.beautymatch.repository;

import br.com.beautymatch.beautymatch.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository: Indica que esta interface é um componente de repositório do Spring
// JpaRepository: Interface que fornece métodos CRUD básicos e paginação
// Servico: A entidade que este repositório gerencia
// Long: O tipo do ID da entidade Servico
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    // Esta interface herda automaticamente vários métodos úteis como:
    // save(), findById(), findAll(), delete(), etc.
}
