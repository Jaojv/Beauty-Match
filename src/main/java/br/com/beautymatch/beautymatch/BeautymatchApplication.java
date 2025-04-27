package br.com.beautymatch.beautymatch;

// Importações necessárias do Spring Boot
import br.com.beautymatch.beautymatch.service.CrudAgendamentoService;
import br.com.beautymatch.beautymatch.service.CrudClienteService;
import br.com.beautymatch.beautymatch.service.CrudProfissionalService;
import br.com.beautymatch.beautymatch.service.CrudServicoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.Scanner;

// @SpringBootApplication: Anotação que combina @Configuration, @EnableAutoConfiguration e @ComponentScan
// Esta é a classe principal que inicia a aplicação Spring Boot
@SpringBootApplication
@EnableCaching
public class BeautyMatchApplication implements CommandLineRunner {
	private final CrudClienteService clienteService;
	private CrudProfissionalService profissionalService;
	private CrudServicoService servicoService;
	private CrudAgendamentoService agendamentoService;

	// Os objetos passado por parâmetro são injetados automaticamente pelo String
	// pq sua classe possue a anotação @Service

	public BeautyMatchApplication(CrudClienteService clienteService, 
                                 CrudProfissionalService profissionalService, 
                                 CrudServicoService servicoService,
                                 CrudAgendamentoService agendamentoService) {
		this.clienteService = clienteService;
		this.profissionalService = profissionalService;
		this.servicoService = servicoService;
		this.agendamentoService = agendamentoService;
	}

	public static void main(String[] args) {
		// SpringApplication.run: Inicia a aplicação Spring Boot
		// BeautyMatchApplication.class: Classe principal da aplicação
		// args: Argumentos de linha de comando passados para a aplicação
		SpringApplication.run(BeautyMatchApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		boolean isTrue = true;
		Scanner scanner = new Scanner(System.in);

		while (isTrue) {
			System.out.println("Qual entidade você deseja interagir? ");
			System.out.println("0 - Sair");
			System.out.println("1 - Cliente");
			System.out.println("2 - Profissional");
			System.out.println("3 - Serviços");
			System.out.println("4 - Agendamentos");
			int opcao = scanner.nextInt();

			switch (opcao) {
				case 1:
					this.clienteService.menu(scanner);
					break;
				case 2:
					this.profissionalService.menu(scanner);
					break;
				case 3:
					this.servicoService.menu(scanner);
					break;
				case 4:
					this.agendamentoService.menu(scanner);
					break;
				default:
					isTrue = false;
					break;
			}
		}

	}
}
