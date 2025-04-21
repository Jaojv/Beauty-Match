package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Cliente;
import br.com.beautymatch.beautymatch.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CrudClienteService {
    private ClienteRepository clienteRepository;//Depêndencia da classe CrudClienteService

    //O Spling autômaticamente cria um objeto com a interface 'ClienteRepository',
    //e o injeta para o construtor da classe atual ==> Injeção de Dependência
    public CrudClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void menu(Scanner scanner){
        boolean isTrue = true;

        while (isTrue){
            System.out.println("Qual ação você quer executar?");
            System.out.println("0 - Voltar a menu anterior");
            System.out.println("1 - Cadastrar novo Cliente");
            System.out.println("2 - Atualizar um Cliente");
            System.out.println("3 - Visualizar todos Clientes");
            System.out.println("4 - Deletar um Cliente");
            int opcao = scanner.nextInt();

            switch (opcao){
                case 1:
                    this.cadastrar(scanner);
                    break;
                case 2:
                    this.atualizar(scanner);
                    break;
                case 3:
                    this.visualizar();
                    break;
                case 4:
                    this.deletar(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }
            System.out.println();
        }
    }

    private void cadastrar(Scanner scanner){
        scanner.nextLine();

        System.out.print("Digite o nome do cliente: \n");
        String nome = scanner.nextLine();

        System.out.print("Digite o CPF do cliente: \n");
        String cpf = scanner.nextLine();

        System.out.print("Digite o telefone do cliente: \n");
        String telefone = scanner.nextLine();

        System.out.print("Digite o email do cliente: \n");
        String email = scanner.nextLine();

        Cliente cliente = new Cliente(nome, cpf, telefone, email);
        this.clienteRepository.save(cliente);
        System.out.println("Cliente salvo no Banco.");

    }

    private void atualizar(Scanner scanner) {
        System.out.print("Digite o Id do Cliente a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Cliente> optional = this.clienteRepository.findById(id);

        //Se o hibernate conseguiu achar um registro na tabela de clientes com id igual ao passado pelo usuario
        if (optional.isPresent()) {

            scanner.nextLine();

            System.out.print("Digite o nome do cliente: \n");
            String nome = scanner.nextLine();

            System.out.print("Digite o CPF do cliente: \n");
            String cpf = scanner.nextLine();

            System.out.print("Digite o telefone do cliente: \n");
            String telefone = scanner.nextLine();

            System.out.print("Digite o email do cliente: \n");
            String email = scanner.nextLine();

            Cliente cliente = optional.get();
            cliente.setNome(nome);
            cliente.setCpf(cpf);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);

            clienteRepository.save(cliente);

            System.out.println("Cliente atualizado com sucesso.");

        } else {
            System.out.println("O Id do Cliente informado: " + id + " é inválido");
        }
    }

    private void visualizar() {
        Iterable<Cliente> clientes = this.clienteRepository.findAll();
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
        System.out.println();
    }

    private void deletar(Scanner scanner) {
        System.out.print("Digite o Id do Cliente a ser deletado: ");
        Long id = scanner.nextLong();

        if (clienteRepository.existsById(id)) {
            this.clienteRepository.deleteById(id);
            System.out.println("Cliente deletado com sucesso.");
        } else {
            System.out.println("Cliente com o ID " + id + " não foi encontrado.");
        }

    }

}
