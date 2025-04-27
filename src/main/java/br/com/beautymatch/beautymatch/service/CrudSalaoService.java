package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class CrudSalaoService {

    @Autowired
    private SalaoRepository salaoRepository;

    public void menu(Scanner scanner) {
        boolean isTrue = true;

        while (isTrue) {
            System.out.println("\n=== Menu de Salões ===");
            System.out.println("1 - Listar todos os salões");
            System.out.println("2 - Buscar salão por ID");
            System.out.println("3 - Buscar salões por nome");
            System.out.println("4 - Buscar salões por endereço");
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
                    buscarPorNome(scanner);
                    break;
                case 4:
                    buscarPorEndereco(scanner);
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
        System.out.println("\n=== Lista de Salões ===");
        salaoRepository.findAll()
                .forEach(salao -> System.out.println(salao));
    }

    private void buscarPorId(Scanner scanner) {
        System.out.print("Digite o ID do salão: ");
        Long id = scanner.nextLong();
        salaoRepository.findById(id)
                .ifPresentOrElse(
                        salao -> System.out.println(salao),
                        () -> System.out.println("Salão não encontrado!")
                );
    }

    private void buscarPorNome(Scanner scanner) {
        System.out.print("Digite o nome do salão: ");
        String nome = scanner.nextLine();
        System.out.println("\n=== Salões por Nome ===");
        salaoRepository.findByNomeContainingIgnoreCase(nome)
                .forEach(salao -> System.out.println(salao));
    }

    private void buscarPorEndereco(Scanner scanner) {
        System.out.print("Digite o endereço do salão: ");
        String endereco = scanner.nextLine();
        System.out.println("\n=== Salões por Endereço ===");
        salaoRepository.buscarPorEndereco(endereco)
                .forEach(salao -> System.out.println(salao));
    }
} 