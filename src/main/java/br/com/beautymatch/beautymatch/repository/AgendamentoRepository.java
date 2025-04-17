package br.com.beautymatch.beautymatch.repository;

// Importações necessárias
import br.com.beautymatch.beautymatch.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository: Indica que esta interface é um componente de repositório do Spring
// JpaRepository: Interface que fornece métodos CRUD básicos e paginação
// Agendamento: A entidade que este repositório gerencia
// Long: O tipo do ID da entidade Agendamento
@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Esta interface herda automaticamente vários métodos úteis como:
    // save(), findById(), findAll(), delete(), etc.
}
