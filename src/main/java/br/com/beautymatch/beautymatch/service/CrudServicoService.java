package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CrudServicoService {
    private ServicoRepository servicoRepository;

    public CrudServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public void menu(Scanner scanner){
        boolean isTrue = true;

        while (isTrue){
            System.out.println("Qual ação você quer executar?");
            System.out.println("0 - Voltar a menu anterior");
            System.out.println("1 - Cadastrar novo Serviço");
            System.out.println("2 - Atualizar um Serviço");
            System.out.println("3 - Visualizar todos os Serviços");
            System.out.println("4 - Deletar um Serviço");
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

        System.out.print("Digite o nome do serviço: \n");
        String nome = scanner.nextLine();

        System.out.print("Digite a descrição do serviço: \n");
        String descricao = scanner.nextLine();

        System.out.print("Digite o preço do serviço: \n");
        BigDecimal preco = scanner.nextBigDecimal();

        System.out.print("Digite a duração do serviço em minutos: \n");
        int duracaoEmMinutos = scanner.nextInt();
        Duration duracao = Duration.ofMinutes(duracaoEmMinutos);

        Servico servico = new Servico(nome, descricao, preco, duracao);
        this.servicoRepository.save(servico);
        System.out.println("Serviço salvo no Banco.");

    }

    private void atualizar(Scanner scanner) {
        System.out.print("Digite o Id do Serviço a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Servico> optional = this.servicoRepository.findById(id);

        //Se o hibernate conseguiu achar um registro na tabela de serviço com id igual ao passado pelo usuario
        if (optional.isPresent()) {

            scanner.nextLine();

            System.out.print("Digite o nome do serviço: \n");
            String nome = scanner.nextLine();

            System.out.print("Digite a descrição do serviço: \n");
            String descricao = scanner.nextLine();

            System.out.print("Digite o preço do serviço: \n");
            BigDecimal preco = scanner.nextBigDecimal();

            System.out.print("Digite a duração do serviço em minutos: \n");
            int duracaoEmMinutos = scanner.nextInt();
            Duration duracao = Duration.ofMinutes(duracaoEmMinutos);

            Servico servico = optional.get();
            servico.setNome(nome);
            servico.setDescricao(descricao);
            servico.setPreco(preco);
            servico.setDuracao(duracao);

            servicoRepository.save(servico);

            System.out.println("Serviço atualizado com sucesso.");

        } else {
            System.out.println("O Id do Serviço informado: " + id + " é inválido");
        }
    }

    private void visualizar() {
        Iterable<Servico> servicos = this.servicoRepository.findAll();
        for (Servico servico : servicos) {
            System.out.println(servico);
        }
        System.out.println();
    }

    private void deletar(Scanner scanner) {
        System.out.print("Digite o Id do Serviço a ser deletado: ");
        Long id = scanner.nextLong();

        if (servicoRepository.existsById(id)) {
            this.servicoRepository.deleteById(id);
            System.out.println("Serviço deletado com sucesso.");
        } else {
            System.out.println("Serviço com o ID " + id + " não foi encontrado.");
        }

    }
}
