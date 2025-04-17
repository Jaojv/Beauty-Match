package br.com.beautymatch.beautymatch;

// Importações necessárias do Spring Boot
import br.com.beautymatch.beautymatch.service.CrudClienteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

// @SpringBootApplication: Anotação que combina @Configuration, @EnableAutoConfiguration e @ComponentScan
// Esta é a classe principal que inicia a aplicação Spring Boot
@SpringBootApplication
public class BeautymatchApplication implements CommandLineRunner {
	private CrudClienteService clienteService;

	// Os objetos passado por parâmetro são injetados automaticamente pelo String
	// pq sua classe possue a anotação @Service
	public BeautymatchApplication(CrudClienteService clienteService) {
		this.clienteService = clienteService;

	}

	public static void main(String[] args) {
		// SpringApplication.run: Inicia a aplicação Spring Boot
		// BeautyMatchApplication.class: Classe principal da aplicação
		// args: Argumentos de linha de comando passados para a aplicação
		SpringApplication.run(BeautymatchApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		boolean isTrue = true;
		Scanner scanner = new Scanner(System.in);

		while (isTrue) {
			System.out.println("Qual entidade você deseja interagir? ");
			System.out.println("0 - Sair");
			System.out.println("1 - Cliente");
			int opcao = scanner.nextInt();

			switch (opcao) {
				case 1:
					this.clienteService.menu(scanner);
					break;
				default:
					isTrue = false;
					break;
			}
		}

	}
}
