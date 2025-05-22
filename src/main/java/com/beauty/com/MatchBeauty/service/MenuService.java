package com.beauty.com.MatchBeauty.service;

import com.beauty.com.MatchBeauty.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class MenuService {
    
    private final Scanner scanner;
    private final AutenticacaoService autenticacaoService;

    @Autowired
    public MenuService(AutenticacaoService autenticacaoService) {
        this.scanner = new Scanner(System.in);
        this.autenticacaoService = autenticacaoService;
    }

    public void exibirMenu() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("\nSessão expirada. Por favor, faça login novamente.");
            return;
        }

        Usuario usuario = autenticacaoService.getUsuarioLogado();
        if (usuario == null) {
            System.out.println("\nErro ao carregar informações do usuário. Por favor, faça login novamente.");
            return;
        }
        
        if (usuario instanceof Administrador) {
            exibirMenuAdministrador();
        } else if (usuario instanceof Proprietario) {
            exibirMenuProprietario();
        } else if (usuario instanceof Profissional) {
            exibirMenuProfissional();
        } else if (usuario instanceof Cliente) {
            exibirMenuCliente();
        }
    }

    private void exibirMenuAdministrador() {
        while (true) {
            System.out.println("\n=== Menu Administrador ===");
            System.out.println("0 - Voltar ao login");
            System.out.println("1 - Cadastrar salão");
            System.out.println("2 - Listar salões");
            System.out.println("3 - Editar informações de salão");
            System.out.println("4 - Deletar salão");
            System.out.println("5 - Cadastrar dono de salão");
            System.out.println("6 - Listar donos de salão");
            System.out.println("7 - Editar dados de dono de salão");
            System.out.println("8 - Deletar dono de salão");
            
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());
            
            if (opcao == 0) {
                autenticacaoService.logout();
                break;
            }
            
            // Implementar as ações do menu
            switch (opcao) {
                case 1 -> System.out.println("Cadastrar salão - Em desenvolvimento");
                case 2 -> System.out.println("Listar salões - Em desenvolvimento");
                case 3 -> System.out.println("Editar salão - Em desenvolvimento");
                case 4 -> System.out.println("Deletar salão - Em desenvolvimento");
                case 5 -> System.out.println("Cadastrar dono - Em desenvolvimento");
                case 6 -> System.out.println("Listar donos - Em desenvolvimento");
                case 7 -> System.out.println("Editar dono - Em desenvolvimento");
                case 8 -> System.out.println("Deletar dono - Em desenvolvimento");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenuProprietario() {
        while (true) {
            System.out.println("\n=== Menu Proprietario do Salão ===");
            System.out.println("0 - Voltar ao login");
            System.out.println("1 - Cadastrar Profissional");
            System.out.println("2 - Cadastrar Serviço");
            System.out.println("3 - Bloquear horário do profissional");
            
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());
            
            if (opcao == 0) {
                autenticacaoService.logout();
                break;
            }
            
            // Implementar as ações do menu
            switch (opcao) {
                case 1 -> System.out.println("Cadastrar profissional - Em desenvolvimento");
                case 2 -> System.out.println("Cadastrar serviço - Em desenvolvimento");
                case 3 -> System.out.println("Bloquear horário - Em desenvolvimento");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenuProfissional() {
        while (true) {
            System.out.println("\n=== Menu Profissional ===");
            System.out.println("0 - Voltar ao login");
            System.out.println("1 - Visualizar agendamentos do dia");
            System.out.println("2 - Visualizar agendamentos futuros");
            System.out.println("3 - Visualizar histórico de agendamentos");
            
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());
            
            if (opcao == 0) {
                autenticacaoService.logout();
                break;
            }
            
            // Implementar as ações do menu
            switch (opcao) {
                case 1 -> System.out.println("Agendamentos do dia - Em desenvolvimento");
                case 2 -> System.out.println("Agendamentos futuros - Em desenvolvimento");
                case 3 -> System.out.println("Histórico - Em desenvolvimento");
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private void exibirMenuCliente() {
        while (true) {
            System.out.println("\n=== Menu Cliente ===");
            System.out.println("0 - Voltar ao login");
            System.out.println("1 - Agendar serviço");
            System.out.println("2 - Cancelar agendamento");
            System.out.println("3 - Ver meus agendamentos futuros");
            System.out.println("4 - Ver histórico de agendamentos");
            
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());
            
            if (opcao == 0) {
                autenticacaoService.logout();
                break;
            }
            
            // Implementar as ações do menu
            switch (opcao) {
                case 1 -> System.out.println("Agendar serviço - Em desenvolvimento");
                case 2 -> System.out.println("Cancelar agendamento - Em desenvolvimento");
                case 3 -> System.out.println("Agendamentos futuros - Em desenvolvimento");
                case 4 -> System.out.println("Histórico - Em desenvolvimento");
                default -> System.out.println("Opção inválida!");
            }
        }
    }
} 