package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Usuario;
import br.com.beautymatch.beautymatch.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service
public class CrudClienteService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void menu(Scanner scanner) {
        boolean isTrue = true;

        while (isTrue) {
            System.out.println("\n=== Menu de Clientes ===");
            System.out.println("1 - Listar todos os clientes");
            System.out.println("2 - Buscar cliente por ID");
            System.out.println("3 - Buscar cliente por email");
            System.out.println("4 - Buscar cliente por CPF");
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
                    buscarPorEmail(scanner);
                    break;
                case 4:
                    buscarPorCpf(scanner);
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
        System.out.println("\n=== Lista de Clientes ===");
        usuarioRepository.findByTipo(Usuario.TipoUsuario.CLIENTE)
                .forEach(cliente -> System.out.println(cliente));
    }

    private void buscarPorId(Scanner scanner) {
        System.out.print("Digite o ID do cliente: ");
        Long id = scanner.nextLong();
        usuarioRepository.findById(id)
                .ifPresentOrElse(
                        cliente -> System.out.println(cliente),
                        () -> System.out.println("Cliente não encontrado!")
                );
    }

    private void buscarPorEmail(Scanner scanner) {
        System.out.print("Digite o email do cliente: ");
        String email = scanner.nextLine();
        usuarioRepository.findByEmail(email)
                .ifPresentOrElse(
                        cliente -> System.out.println(cliente),
                        () -> System.out.println("Cliente não encontrado!")
                );
    }

    private void buscarPorCpf(Scanner scanner) {
        System.out.print("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();
        usuarioRepository.findByCpf(cpf)
                .ifPresentOrElse(
                        cliente -> System.out.println(cliente),
                        () -> System.out.println("Cliente não encontrado!")
                );
    }
}

