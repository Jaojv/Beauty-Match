package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Scanner;

@Service
public class CrudProfissionalService {
    private ProfissionalRepository profissionalRepository;

    public CrudProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public void menu(Scanner scanner){
        boolean isTrue = true;

        while (isTrue){
            System.out.println("Qual ação você quer executar?");
            System.out.println("0 - Voltar a menu anterior");
            System.out.println("1 - Cadastrar novo Profissional");
            System.out.println("2 - Atualizar um Profissional");
            System.out.println("3 - Visualizar todos os Profissionais");
            System.out.println("4 - Deletar um Profissional");
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

        System.out.print("Digite o nome do profissional: \n");
        String nome = scanner.nextLine();

        System.out.print("Digite a especialidade do profissional: \n");
        String especialidade = scanner.nextLine();

        System.out.print("Digite o telefone do profissional: \n");
        String telefone = scanner.nextLine();

        System.out.print("Digite o email do profissional: \n");
        String email = scanner.nextLine();

        Profissional profissional = new Profissional(nome, especialidade, telefone, email);
        this.profissionalRepository.save(profissional);
        System.out.println("Profissional salvo no Banco.");

    }

    private void atualizar(Scanner scanner) {
        System.out.print("Digite o Id do Profissional a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Profissional> optional = this.profissionalRepository.findById(id);

        //Se o hibernate conseguiu achar um registro na tabela do profissional com id igual ao passado pelo usuario
        if (optional.isPresent()) {

            scanner.nextLine();

            System.out.print("Digite o nome do profissional: \n");
            String nome = scanner.nextLine();

            System.out.print("Digite a especialidade do profissional: \n");
            String especialidade = scanner.nextLine();

            System.out.print("Digite o telefone do profissional: \n");
            String telefone = scanner.nextLine();

            System.out.print("Digite o email do profissional: \n");
            String email = scanner.nextLine();

            Profissional profissional = optional.get();
            profissional.setNome(nome);
            profissional.setEspecialidade(especialidade);
            profissional.setTelefone(telefone);
            profissional.setEmail(email);

            profissionalRepository.save(profissional);

            System.out.println("Profissional atualizado com sucesso.");

        } else {
            System.out.println("O Id do Profissional informado: " + id + " é inválido");
        }
    }

    private void visualizar() {
        Iterable<Profissional> profissionais = this.profissionalRepository.findAll();
        for (Profissional profissional : profissionais) {
            System.out.println(profissional);
        }
        System.out.println();
    }

    private void deletar(Scanner scanner) {
        System.out.print("Digite o Id do Profissional a ser deletado: ");
        Long id = scanner.nextLong();

        if (profissionalRepository.existsById(id)) {
            this.profissionalRepository.deleteById(id);
            System.out.println("Profissional deletado com sucesso.");
        } else {
            System.out.println("Profissional com o ID " + id + " não foi encontrado.");
        }

    }
}
