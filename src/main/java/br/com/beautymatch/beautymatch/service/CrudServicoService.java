package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Salao;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import br.com.beautymatch.beautymatch.repository.SalaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CrudServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private SalaoRepository salaoRepository;

    public void menu(Scanner scanner) {
        while (true) {
            System.out.println("\n=== Menu de Serviços ===");
            System.out.println("1. Cadastrar Serviço");
            System.out.println("2. Listar Serviços");
            System.out.println("3. Atualizar Serviço");
            System.out.println("4. Deletar Serviço");
            System.out.println("5. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (opcao) {
                case 1:
                    cadastrar(scanner);
                    break;
                case 2:
                    listar(scanner);
                    break;
                case 3:
                    atualizar(scanner);
                    break;
                case 4:
                    deletar(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    public void cadastrar(Scanner scanner) {
        System.out.println("\n=== Cadastro de Serviço ===");
        
        System.out.print("Nome do serviço: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição do serviço: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Preço do serviço: ");
        BigDecimal preco = scanner.nextBigDecimal();
        scanner.nextLine(); // Limpar o buffer
        
        System.out.print("Duração do serviço (em minutos): ");
        int duracaoMinutos = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer
        
        System.out.println("\nSalões disponíveis:");
        List<Salao> saloes = salaoRepository.findAll();
        for (Salao salao : saloes) {
            System.out.println(salao.getId() + ". " + salao.getNome());
        }
        
        System.out.print("Escolha o ID do salão: ");
        Long salaoId = scanner.nextLong();
        scanner.nextLine(); // Limpar o buffer
        
        Salao salao = salaoRepository.findById(salaoId)
                .orElseThrow(() -> new RuntimeException("Salão não encontrado"));
        
        Servico servico = new Servico();
        servico.setNome(nome);
        servico.setDescricao(descricao);
        servico.setPreco(preco);
        servico.setDuracaoMinutos(duracaoMinutos);
        servico.setSalao(salao);
        
        servicoRepository.save(servico);
        System.out.println("Serviço cadastrado com sucesso!");
    }

    private void listar(Scanner scanner) {
        System.out.println("\n=== Lista de Serviços ===");
        List<Servico> servicos = servicoRepository.findAll();
        if (servicos.isEmpty()) {
            System.out.println("Nenhum serviço cadastrado.");
            return;
        }
        for (Servico servico : servicos) {
            System.out.println("ID: " + servico.getId());
            System.out.println("Nome: " + servico.getNome());
            System.out.println("Descrição: " + servico.getDescricao());
            System.out.println("Preço: R$" + servico.getPreco());
            System.out.println("Duração: " + servico.getDuracaoMinutos() + " minutos");
            System.out.println("Salão: " + servico.getSalao().getNome());
            System.out.println("-------------------");
        }
    }

    public void atualizar(Scanner scanner) {
        System.out.println("\n=== Atualização de Serviço ===");
        System.out.print("Digite o ID do serviço a ser atualizado: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Limpar o buffer
        
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
        
        System.out.print("Novo nome do serviço (atual: " + servico.getNome() + "): ");
        String nome = scanner.nextLine();
        
        System.out.print("Nova descrição do serviço (atual: " + servico.getDescricao() + "): ");
        String descricao = scanner.nextLine();
        
        System.out.print("Novo preço do serviço (atual: " + servico.getPreco() + "): ");
        BigDecimal preco = scanner.nextBigDecimal();
        scanner.nextLine(); // Limpar o buffer
        
        System.out.print("Nova duração do serviço em minutos (atual: " + servico.getDuracaoMinutos() + "): ");
        int duracaoMinutos = scanner.nextInt();
        scanner.nextLine(); // Limpar o buffer
        
        servico.setNome(nome);
        servico.setDescricao(descricao);
        servico.setPreco(preco);
        servico.setDuracaoMinutos(duracaoMinutos);
        
        servicoRepository.save(servico);
        System.out.println("Serviço atualizado com sucesso!");
    }

    @Transactional
    public void deletar(Scanner scanner) {
        System.out.println("\n=== Deletar Serviço ===");
        System.out.print("Digite o ID do serviço a ser deletado: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Limpar o buffer
        
        Optional<Servico> servicoOpt = servicoRepository.findById(id);
        
        if (servicoOpt.isPresent()) {
            Servico servico = servicoOpt.get();
            List<Agendamento> agendamentos = servico.getAgendamentos();
            
            if (!agendamentos.isEmpty()) {
                System.out.println("ATENÇÃO: Este serviço possui " + agendamentos.size() + " agendamento(s) vinculado(s).");
                System.out.println("Deseja realmente deletar o serviço e todos os seus agendamentos? (S/N)");
                String resposta = scanner.nextLine().toUpperCase();
                
                if (!resposta.equals("S")) {
                    System.out.println("Operação cancelada pelo usuário.");
                    return;
                }
            }
            
            servicoRepository.deleteById(id);
            System.out.println("Serviço deletado com sucesso!");
        } else {
            System.out.println("Serviço não encontrado!");
        }
    }

    @Transactional
    private void visualizar(Scanner scanner) {
        Iterable<Servico> servicos = this.servicoRepository.findAll();
        for (Servico servico : servicos) {
            System.out.println(servico);
            List<Agendamento> agendamentos = servico.getAgendamentos();
            if (!agendamentos.isEmpty()) {
                System.out.println("Agendamentos do serviço:");
                for (Agendamento agendamento : agendamentos) {
                    System.out.println("  - Data: " + agendamento.getDataHora().toLocalDate() + 
                                     ", Hora: " + agendamento.getDataHora().toLocalTime() + 
                                     ", Cliente: " + agendamento.getCliente().getUsuario().getNome() +
                                     ", Profissional: " + agendamento.getProfissional().getNome());
                }
            }
            System.out.println();
        }
    }
}
