package br.com.beautymatch.beautymatch.service;

import br.com.beautymatch.beautymatch.model.Agendamento;
import br.com.beautymatch.beautymatch.model.Cliente;
import br.com.beautymatch.beautymatch.model.Profissional;
import br.com.beautymatch.beautymatch.model.Servico;
import br.com.beautymatch.beautymatch.repository.AgendamentoRepository;
import br.com.beautymatch.beautymatch.repository.ClienteRepository;
import br.com.beautymatch.beautymatch.repository.ProfissionalRepository;
import br.com.beautymatch.beautymatch.repository.ServicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
public class CrudAgendamentoService {
    private AgendamentoRepository agendamentoRepository;
    private ClienteRepository clienteRepository;
    private ProfissionalRepository profissionalRepository;
    private ServicoRepository servicoRepository;

    public CrudAgendamentoService(AgendamentoRepository agendamentoRepository, ClienteRepository clienteRepository, ProfissionalRepository profissionalRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.servicoRepository = servicoRepository;
    }

    public void menu(Scanner scanner) {
        boolean isTrue = true;

        while (isTrue) {
            System.out.println("Qual ação você quer executar?");
            System.out.println("0 - Voltar a menu anterior");
            System.out.println("1 - Cadastrar novo Agendamento");
            System.out.println("2 - Atualizar um Agendamento");
            System.out.println("3 - Visualizar todos os Agendamentos");
            System.out.println("4 - Deletar um Agendamento");
            System.out.println("5 - Visualizar agenda de um profissional");
            System.out.println("6 - Visualizar agenda de um cliente");
            System.out.println("7 - Verificar disponibilidade de horário");
            int opcao = scanner.nextInt();

            switch (opcao) {
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
                case 5:
                    this.visualizarAgendaProfissional(scanner);
                    break;
                case 6:
                    this.visualizarAgendaCliente(scanner);
                    break;
                case 7:
                    this.verificarDisponibilidade(scanner);
                    break;
                default:
                    isTrue = false;
                    break;
            }
            System.out.println();
        }
    }

    private void cadastrar(Scanner scanner) {
        scanner.nextLine();

        // Verificar se o cliente existe
        System.out.print("Digite o ID do cliente: \n");
        Long clienteId = scanner.nextLong();
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        
        if (!clienteOpt.isPresent()) {
            System.out.println("Cliente não encontrado. Apenas clientes cadastrados podem realizar agendamentos.");
            return;
        }
        
        // Verificar se o profissional existe
        System.out.print("Digite o ID do profissional: \n");
        Long profissionalId = scanner.nextLong();
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        
        if (!profissionalOpt.isPresent()) {
            System.out.println("Profissional não encontrado.");
            return;
        }
        
        // Verificar se o serviço existe
        System.out.print("Digite o ID do serviço: \n");
        Long servicoId = scanner.nextLong();
        Optional<Servico> servicoOpt = servicoRepository.findById(servicoId);
        
        if (!servicoOpt.isPresent()) {
            System.out.println("Serviço não encontrado.");
            return;
        }
        
        // Solicitar data e hora
        scanner.nextLine(); // Limpar o buffer
        
        System.out.print("Digite a data do agendamento (formato dd/MM/yyyy): \n");
        String dataStr = scanner.nextLine();
        LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        System.out.print("Digite a hora do agendamento (formato HH:mm): \n");
        String horaStr = scanner.nextLine();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        
        // Verificar disponibilidade do horário
        if (!verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracao())) {
            System.out.println("Horário indisponível para este profissional.");
            return;
        }
        
        // Criar e salvar o agendamento
        Agendamento agendamento = new Agendamento(data, hora, clienteOpt.get(), profissionalOpt.get(), servicoOpt.get());
        this.agendamentoRepository.save(agendamento);
        System.out.println("Agendamento salvo com sucesso.");
    }

    private void atualizar(Scanner scanner) {
        System.out.print("Digite o ID do Agendamento a ser atualizado: ");
        Long id = scanner.nextLong();

        Optional<Agendamento> optional = this.agendamentoRepository.findById(id);

        if (optional.isPresent()) {
            scanner.nextLine();

            // Verificar se o cliente existe
            System.out.print("Digite o ID do cliente: \n");
            Long clienteId = scanner.nextLong();
            Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
            
            if (!clienteOpt.isPresent()) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            // Verificar se o profissional existe
            System.out.print("Digite o ID do profissional: \n");
            Long profissionalId = scanner.nextLong();
            Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
            
            if (!profissionalOpt.isPresent()) {
                System.out.println("Profissional não encontrado.");
                return;
            }
            
            // Verificar se o serviço existe
            System.out.print("Digite o ID do serviço: \n");
            Long servicoId = scanner.nextLong();
            Optional<Servico> servicoOpt = servicoRepository.findById(servicoId);
            
            if (!servicoOpt.isPresent()) {
                System.out.println("Serviço não encontrado.");
                return;
            }
            
            // Solicitar data e hora
            scanner.nextLine(); // Limpar o buffer
            
            System.out.print("Digite a data do agendamento (formato dd/MM/yyyy): \n");
            String dataStr = scanner.nextLine();
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            
            System.out.print("Digite a hora do agendamento (formato HH:mm): \n");
            String horaStr = scanner.nextLine();
            LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
            
            // Verificar disponibilidade do horário (excluindo o agendamento atual)
            if (!verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracao(), id)) {
                System.out.println("Horário indisponível para este profissional.");
                return;
            }
            
            Agendamento agendamento = optional.get();
            agendamento.setData(data);
            agendamento.setHora(hora);
            agendamento.setCliente(clienteOpt.get());
            agendamento.setProfissional(profissionalOpt.get());
            agendamento.setServico(servicoOpt.get());

            agendamentoRepository.save(agendamento);
            System.out.println("Agendamento atualizado com sucesso.");
        } else {
            System.out.println("O ID do Agendamento informado: " + id + " é inválido");
        }
    }

    private void visualizar() {
        Iterable<Agendamento> agendamentos = this.agendamentoRepository.findAll();
        for (Agendamento agendamento : agendamentos) {
            System.out.println(agendamento);
        }
        System.out.println();
    }

    private void deletar(Scanner scanner) {
        System.out.print("Digite o ID do Agendamento a ser deletado: ");
        Long id = scanner.nextLong();

        if (agendamentoRepository.existsById(id)) {
            this.agendamentoRepository.deleteById(id);
            System.out.println("Agendamento deletado com sucesso.");
        } else {
            System.out.println("Agendamento com o ID " + id + " não foi encontrado.");
        }
    }
    
    private void visualizarAgendaProfissional(Scanner scanner) {
        System.out.print("Digite o ID do profissional: ");
        Long profissionalId = scanner.nextLong();
        
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        
        if (!profissionalOpt.isPresent()) {
            System.out.println("Profissional não encontrado.");
            return;
        }
        
        System.out.print("Digite a data (formato dd/MM/yyyy) ou deixe em branco para ver todos os agendamentos: ");
        scanner.nextLine();
        String dataStr = scanner.nextLine();
        
        List<Agendamento> agendamentos;
        if (dataStr.isEmpty()) {
            agendamentos = agendamentoRepository.findByProfissional(profissionalOpt.get());
        } else {
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            agendamentos = agendamentoRepository.findByProfissionalAndData(profissionalOpt.get(), data);
        }
        
        if (agendamentos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
        } else {
            System.out.println("Agenda do profissional " + profissionalOpt.get().getNome() + ":");
            for (Agendamento agendamento : agendamentos) {
                System.out.println(agendamento);
            }
        }
    }
    
    private void visualizarAgendaCliente(Scanner scanner) {
        System.out.print("Digite o ID do cliente: ");
        Long clienteId = scanner.nextLong();
        
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        
        if (!clienteOpt.isPresent()) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        
        System.out.print("Digite a data (formato dd/MM/yyyy) ou deixe em branco para ver todos os agendamentos: ");
        scanner.nextLine(); // Limpar o buffer
        String dataStr = scanner.nextLine();
        
        List<Agendamento> agendamentos;
        if (dataStr.isEmpty()) {
            agendamentos = agendamentoRepository.findByCliente(clienteOpt.get());
        } else {
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            agendamentos = agendamentoRepository.findByClienteAndData(clienteOpt.get(), data);
        }
        
        if (agendamentos.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
        } else {
            System.out.println("Agenda do cliente " + clienteOpt.get().getNome() + ":");
            for (Agendamento agendamento : agendamentos) {
                System.out.println(agendamento);
            }
        }
    }
    
    private void verificarDisponibilidade(Scanner scanner) {
        System.out.print("Digite o ID do profissional: ");
        Long profissionalId = scanner.nextLong();
        
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        
        if (!profissionalOpt.isPresent()) {
            System.out.println("Profissional não encontrado.");
            return;
        }
        
        System.out.print("Digite o ID do serviço: ");
        Long servicoId = scanner.nextLong();
        
        Optional<Servico> servicoOpt = servicoRepository.findById(servicoId);
        
        if (!servicoOpt.isPresent()) {
            System.out.println("Serviço não encontrado.");
            return;
        }
        
        scanner.nextLine();
        
        System.out.print("Digite a data (formato dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        System.out.print("Digite a hora (formato HH:mm): ");
        String horaStr = scanner.nextLine();
        LocalTime hora = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));
        
        boolean disponivel = verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracao());
        
        if (disponivel) {
            System.out.println("Horário disponível para agendamento.");
        } else {
            System.out.println("Horário indisponível para agendamento.");
        }
    }
    
    private boolean verificarDisponibilidadeHorario(Profissional profissional, LocalDate data, LocalTime hora, java.time.Duration duracao) {
        return verificarDisponibilidadeHorario(profissional, data, hora, duracao, null);
    }
    
    private boolean verificarDisponibilidadeHorario(Profissional profissional, LocalDate data, LocalTime hora, java.time.Duration duracao, Long agendamentoIdIgnorado) {
        // Obter todos os agendamentos do profissional para a data especificada
        List<Agendamento> agendamentosDoDia = agendamentoRepository.findByProfissionalAndData(profissional, data);
        
        // Calcular o horário de término do agendamento proposto
        LocalTime horaTermino = hora.plus(duracao);
        
        // Verificar se há conflito com outros agendamentos
        for (Agendamento agendamento : agendamentosDoDia) {
            // Ignorar o agendamento que está sendo atualizado
            if (agendamentoIdIgnorado != null && agendamento.getId_agendamento().equals(agendamentoIdIgnorado)) {
                continue;
            }
            
            LocalTime horaInicioExistente = agendamento.getHora();
            LocalTime horaTerminoExistente = horaInicioExistente.plus(agendamento.getServico().getDuracao());
            
            // Verificar se há sobreposição de horários
            if (!(horaTermino.isBefore(horaInicioExistente) || hora.isAfter(horaTerminoExistente))) {
                return false; // Há conflito de horário
            }
        }
        
        return true; // Não há conflito de horário
    }
} 