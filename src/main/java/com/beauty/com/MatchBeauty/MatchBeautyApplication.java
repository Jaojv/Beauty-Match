package com.beauty.com.MatchBeauty;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.beauty.com.MatchBeauty.service.AutenticacaoService;
import com.beauty.com.MatchBeauty.service.MenuService;

@SpringBootApplication
public class MatchBeautyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchBeautyApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(AutenticacaoService autenticacaoService, MenuService menuService) {
		return args -> {
			while (true) {
				if (autenticacaoService.realizarLogin()) {
					menuService.exibirMenu();
				}
			}
		};
	}

}
