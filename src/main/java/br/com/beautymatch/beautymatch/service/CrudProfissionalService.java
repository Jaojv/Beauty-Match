package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;

@Service
public class CrudProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void menu(Scanner scanner) {
        boolean isTrue = true;

        while (isTrue) {
            System.out.println("\n=== Menu de Profissionais ===");
            System.out.println("1 - Listar todos os profissionais");
            System.out.println("2 - Buscar profissional por ID");
            System.out.println("3 - Buscar profissionais por salão");
            System.out.println("4 - Buscar profissionais por especialidade");
            System.out.println("5 - Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            switch (opcao) {
                case 1:
                    listarTodos();
                    break;
                case 2:
                    buscarPorId(scanner);
                    break;
                case 3:
                    buscarPorSalao(scanner);
                    break;
                case 4:
                    buscarPorEspecialidade(scanner);
                    break;
                case 5:
                    isTrue = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private void listarTodos() {
        System.out.println("\n=== Lista de Profissionais ===");
        profissionalRepository.findAll()
                .forEach(profissional -> System.out.println(profissional));
    }

    private void buscarPorId(Scanner scanner) {
        System.out.print("Digite o ID do profissional: ");
        Long id = scanner.nextLong();
        profissionalRepository.findById(id)
                .ifPresentOrElse(
                        profissional -> System.out.println(profissional),
                        () -> System.out.println("Profissional não encontrado!")
                );
    }

    private void buscarPorSalao(Scanner scanner) {
        System.out.print("Digite o ID do salão: ");
        Long salaoId = scanner.nextLong();
        System.out.println("\n=== Profissionais do Salão ===");
        profissionalRepository.findBySalaoId(salaoId)
                .forEach(profissional -> System.out.println(profissional));
    }

    private void buscarPorEspecialidade(Scanner scanner) {
        System.out.print("Digite a especialidade: ");
        String especialidade = scanner.nextLine();
        System.out.println("\n=== Profissionais por Especialidade ===");
        profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade)
                .forEach(profissional -> System.out.println(profissional));
    }

    private void cadastrar(Scanner scanner){
        scanner.nextLine();

        System.out.print("Digite o nome do profissional: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a especialidade do profissional: ");
        String especialidade = scanner.nextLine();

        System.out.print("Digite o telefone do profissional: ");
        String telefone = scanner.nextLine();

        System.out.print("Digite o email do profissional: ");
        String email = scanner.nextLine();

        // Criar usuário primeiro
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setTipo(Usuario.TipoUsuario.PROFISSIONAL);
        usuarioRepository.save(usuario);

        // Criar profissional associado ao usuário
        Profissional profissional = new Profissional(especialidade, "", true, null, usuario);
        profissional.setNome(nome);
        profissionalRepository.save(profissional);

        System.out.println("Profissional cadastrado com sucesso!");
    }

    private void atualizar(Scanner scanner) {
        System.out.print("Digite o ID do Profissional a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Profissional> optional = this.profissionalRepository.findById(id);

        if (optional.isPresent()) {
            scanner.nextLine();

            System.out.print("Digite o novo nome do profissional: ");
            String nome = scanner.nextLine();

            System.out.print("Digite a nova especialidade do profissional: ");
            String especialidade = scanner.nextLine();

            System.out.print("Digite o novo telefone do profissional: ");
            String telefone = scanner.nextLine();

            System.out.print("Digite o novo email do profissional: ");
            String email = scanner.nextLine();

            Profissional profissional = optional.get();
            Usuario usuario = profissional.getUsuario();

            // Atualizar dados do usuário
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setTelefone(telefone);
            usuarioRepository.save(usuario);

            // Atualizar dados do profissional
            profissional.setNome(nome);
            profissional.setEspecialidade(especialidade);
            profissionalRepository.save(profissional);

            System.out.println("Profissional atualizado com sucesso!");
        } else {
            System.out.println("O ID do Profissional informado: " + id + " é inválido");
        }
    }

    @Transactional
    private void visualizar() {
        Iterable<Profissional> profissionais = this.profissionalRepository.findAll();
        for (Profissional profissional : profissionais) {
            System.out.println(profissional);
            List<Agendamento> agendamentos = profissional.getAgendamentos();
            if (!agendamentos.isEmpty()) {
                System.out.println("Agendamentos do profissional:");
                for (Agendamento agendamento : agendamentos) {
                    System.out.println("  - Data: " + agendamento.getDataHora().toLocalDate() +
                                     ", Hora: " + agendamento.getDataHora().toLocalTime() +
                                     ", Serviço: " + agendamento.getServico().getNome() +
                                     ", Cliente: " + agendamento.getCliente().getUsuario().getNome());
                }
            }
            System.out.println();
        }
    }

    @Transactional
    private void deletar(Scanner scanner) {
        System.out.print("Digite o Id do Profissional a ser deletado: ");
        Long id = scanner.nextLong();

        Optional<Profissional> profissionalOpt = profissionalRepository.findById(id);
        
        if (profissionalOpt.isPresent()) {
            Profissional profissional = profissionalOpt.get();
            List<Agendamento> agendamentos = profissional.getAgendamentos();
            
            if (!agendamentos.isEmpty()) {
                System.out.println("ATENÇÃO: Este profissional possui " + agendamentos.size() + " agendamento(s) vinculado(s).");
                System.out.println("Deseja realmente deletar o profissional e todos os seus agendamentos? (S/N)");
                scanner.nextLine(); // Limpa o buffer
                String resposta = scanner.nextLine().toUpperCase();
                
                if (!resposta.equals("S")) {
                    System.out.println("Operação cancelada pelo usuário.");
                    return;
                }
            }
            
            this.profissionalRepository.deleteById(id);
            System.out.println("Profissional deletado com sucesso.");
        } else {
            System.out.println("Profissional com o ID " + id + " não foi encontrado.");
        }
    }
}

