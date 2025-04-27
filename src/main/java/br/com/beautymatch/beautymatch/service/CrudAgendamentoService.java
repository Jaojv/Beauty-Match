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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
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
        if (!verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracaoMinutos())) {
            System.out.println("Horário indisponível para este profissional.");
            return;
        }
        
        // Criar e salvar o agendamento
        LocalDateTime dataHora = data.atTime(hora);
        LocalDateTime dataHoraFim = dataHora.plusMinutes(servicoOpt.get().getDuracaoMinutos());
        Agendamento agendamento = new Agendamento(dataHora, dataHoraFim, clienteOpt.get(), profissionalOpt.get(), servicoOpt.get());
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
            if (!verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracaoMinutos(), id)) {
                System.out.println("Horário indisponível para este profissional.");
                return;
            }
            
            Agendamento agendamento = optional.get();
            LocalDateTime dataHora = data.atTime(hora);
            LocalDateTime dataHoraFim = dataHora.plusMinutes(servicoOpt.get().getDuracaoMinutos());
            agendamento.setDataHora(dataHora);
            agendamento.setDataHoraFim(dataHoraFim);
            agendamento.setCliente(clienteOpt.get());
            agendamento.setProfissional(profissionalOpt.get());
            agendamento.setServico(servicoOpt.get());

            agendamentoRepository.save(agendamento);
            System.out.println("Agendamento atualizado com sucesso.");
        } else {
            System.out.println("O ID do Agendamento informado: " + id + " é inválido");
        }
    }

    @Transactional
    private void visualizar() {
        Iterable<Agendamento> agendamentos = this.agendamentoRepository.findAll();
        if (!((List<Agendamento>) agendamentos).isEmpty()) {
            System.out.println("\n=== Lista de Agendamentos ===");
            for (Agendamento agendamento : agendamentos) {
                exibirDetalhesAgendamento(agendamento);
            }
        } else {
            System.out.println("Não há agendamentos cadastrados.");
        }
    }

    private void exibirDetalhesAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            System.out.println("Agendamento inválido");
            return;
        }

        System.out.println("\nID: " + agendamento.getId());
        System.out.println("Data: " + (agendamento.getDataHora() != null ? 
            agendamento.getDataHora().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Data não informada"));
        System.out.println("Hora: " + (agendamento.getDataHora() != null ? 
            agendamento.getDataHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "Hora não informada"));
        
        String nomeCliente = "Cliente não informado";
        if (agendamento.getCliente() != null && agendamento.getCliente().getUsuario() != null) {
            nomeCliente = agendamento.getCliente().getUsuario().getNome();
        }
        System.out.println("Cliente: " + nomeCliente + " (ID: " + (agendamento.getCliente() != null ? agendamento.getCliente().getId() : "N/A") + ")");
        
        String nomeProfissional = "Profissional não informado";
        if (agendamento.getProfissional() != null) {
            nomeProfissional = agendamento.getProfissional().getNome();
        }
        System.out.println("Profissional: " + nomeProfissional + " (ID: " + (agendamento.getProfissional() != null ? agendamento.getProfissional().getId() : "N/A") + ")");
        
        String nomeServico = "Serviço não informado";
        if (agendamento.getServico() != null) {
            nomeServico = agendamento.getServico().getNome();
        }
        System.out.println("Serviço: " + nomeServico + " (ID: " + (agendamento.getServico() != null ? agendamento.getServico().getId() : "N/A") + ")");
        
        if (agendamento.getServico() != null) {
            System.out.println("Duração: " + agendamento.getServico().getDuracaoMinutos() + " minutos");
            System.out.println("Valor: R$ " + agendamento.getServico().getPreco());
        }
        System.out.println("----------------------------------------");
    }

    private void deletar(Scanner scanner) {
        System.out.print("Digite o ID do Agendamento a ser deletado: ");
        Long id = scanner.nextLong();

        Optional<Agendamento> agendamentoOpt = this.agendamentoRepository.findById(id);
        
        if (agendamentoOpt.isPresent()) {
            Agendamento agendamento = agendamentoOpt.get();
            
            System.out.println("\nDetalhes do agendamento a ser deletado:");
            exibirDetalhesAgendamento(agendamento);
            
            System.out.println("Tem certeza que deseja deletar este agendamento? (S/N)");
            scanner.nextLine(); // Limpa o buffer
            String resposta = scanner.nextLine().toUpperCase();
            
            if (resposta.equals("S")) {
                this.agendamentoRepository.deleteById(id);
                System.out.println("Agendamento deletado com sucesso.");
            } else {
                System.out.println("Operação cancelada pelo usuário.");
            }
        } else {
            System.out.println("Agendamento com ID " + id + " não encontrado.");
        }
    }
    
    @Transactional
    private void visualizarAgendaProfissional(Scanner scanner) {
        System.out.print("Digite o ID do Profissional: ");
        Long profissionalId = scanner.nextLong();
        
        Optional<Profissional> profissionalOpt = profissionalRepository.findById(profissionalId);
        
        if (profissionalOpt.isPresent()) {
            Profissional profissional = profissionalOpt.get();
            List<Agendamento> agendamentos = profissional.getAgendamentos();
            
            if (agendamentos != null && !agendamentos.isEmpty()) {
                String nomeProfissional = "Profissional";
                if (profissional.getUsuario() != null) {
                    nomeProfissional = profissional.getUsuario().getNome();
                }
                System.out.println("\n=== Agenda do(a) Profissional " + nomeProfissional + " ===");
                
                // Ordenar agendamentos por data e hora
                agendamentos.sort((a1, a2) -> {
                    if (a1.getDataHora() == null || a2.getDataHora() == null) {
                        return 0;
                    }
                    int dateCompare = a1.getDataHora().toLocalDate().compareTo(a2.getDataHora().toLocalDate());
                    if (dateCompare == 0) {
                        return a1.getDataHora().toLocalTime().compareTo(a2.getDataHora().toLocalTime());
                    }
                    return dateCompare;
                });
                
                LocalDate currentDate = null;
                for (Agendamento agendamento : agendamentos) {
                    if (agendamento == null || agendamento.getDataHora() == null) {
                        continue;
                    }
                    
                    if (currentDate == null || !currentDate.equals(agendamento.getDataHora().toLocalDate())) {
                        currentDate = agendamento.getDataHora().toLocalDate();
                        System.out.println("\nData: " + currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                    System.out.println("----------------------------------------");
                    System.out.println("Hora: " + agendamento.getDataHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    
                    String nomeCliente = "Cliente não informado";
                    if (agendamento.getCliente() != null && agendamento.getCliente().getUsuario() != null) {
                        nomeCliente = agendamento.getCliente().getUsuario().getNome();
                    }
                    System.out.println("Cliente: " + nomeCliente);
                    
                    String nomeServico = "Serviço não informado";
                    if (agendamento.getServico() != null) {
                        nomeServico = agendamento.getServico().getNome();
                    }
                    System.out.println("Serviço: " + nomeServico);
                    
                    if (agendamento.getServico() != null) {
                        System.out.println("Duração: " + agendamento.getServico().getDuracaoMinutos() + " minutos");
                    }
                }
            } else {
                System.out.println("Não há agendamentos para este profissional.");
            }
        } else {
            System.out.println("Profissional não encontrado.");
        }
    }
    
    @Transactional
    private void visualizarAgendaCliente(Scanner scanner) {
        System.out.print("Digite o ID do Cliente: ");
        Long clienteId = scanner.nextLong();
        
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            List<Agendamento> agendamentos = cliente.getAgendamentos();
            
            if (agendamentos != null && !agendamentos.isEmpty()) {
                String nomeCliente = "Cliente";
                if (cliente.getUsuario() != null) {
                    nomeCliente = cliente.getUsuario().getNome();
                }
                System.out.println("\n=== Agenda do(a) Cliente " + nomeCliente + " ===");
                
                // Ordenar agendamentos por data e hora
                agendamentos.sort((a1, a2) -> {
                    if (a1.getDataHora() == null || a2.getDataHora() == null) {
                        return 0;
                    }
                    int dateCompare = a1.getDataHora().toLocalDate().compareTo(a2.getDataHora().toLocalDate());
                    if (dateCompare == 0) {
                        return a1.getDataHora().toLocalTime().compareTo(a2.getDataHora().toLocalTime());
                    }
                    return dateCompare;
                });
                
                LocalDate currentDate = null;
                for (Agendamento agendamento : agendamentos) {
                    if (agendamento == null || agendamento.getDataHora() == null) {
                        continue;
                    }
                    
                    if (currentDate == null || !currentDate.equals(agendamento.getDataHora().toLocalDate())) {
                        currentDate = agendamento.getDataHora().toLocalDate();
                        System.out.println("\nData: " + currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    }
                    System.out.println("----------------------------------------");
                    System.out.println("Hora: " + agendamento.getDataHora().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                    
                    String nomeProfissional = "Profissional não informado";
                    if (agendamento.getProfissional() != null && agendamento.getProfissional().getUsuario() != null) {
                        nomeProfissional = agendamento.getProfissional().getUsuario().getNome();
                    }
                    System.out.println("Profissional: " + nomeProfissional);
                    
                    String nomeServico = "Serviço não informado";
                    if (agendamento.getServico() != null) {
                        nomeServico = agendamento.getServico().getNome();
                    }
                    System.out.println("Serviço: " + nomeServico);
                    
                    if (agendamento.getServico() != null) {
                        System.out.println("Duração: " + agendamento.getServico().getDuracaoMinutos() + " minutos");
                    }
                }
            } else {
                System.out.println("Não há agendamentos para este cliente.");
            }
        } else {
            System.out.println("Cliente não encontrado.");
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
        
        boolean disponivel = verificarDisponibilidadeHorario(profissionalOpt.get(), data, hora, servicoOpt.get().getDuracaoMinutos());
        
        if (disponivel) {
            System.out.println("Horário disponível para agendamento.");
        } else {
            System.out.println("Horário indisponível para agendamento.");
        }
    }
    
    private boolean verificarDisponibilidadeHorario(Profissional profissional, LocalDate data, LocalTime hora, Integer duracaoMinutos) {
        LocalDateTime dataHoraInicio = data.atTime(hora);
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(duracaoMinutos);
        
        List<Agendamento> agendamentosExistentes = agendamentoRepository.findConflitosAgendamentoIntervalo(
                profissional.getId(), dataHoraInicio, dataHoraFim);
        
        return agendamentosExistentes.isEmpty();
    }
    
    private boolean verificarDisponibilidadeHorario(Profissional profissional, LocalDate data, LocalTime hora, Integer duracaoMinutos, Long agendamentoId) {
        LocalDateTime dataHoraInicio = data.atTime(hora);
        LocalDateTime dataHoraFim = dataHoraInicio.plusMinutes(duracaoMinutos);
        
        List<Agendamento> agendamentosExistentes = agendamentoRepository.findConflitosAgendamentoIntervalo(
                profissional.getId(), dataHoraInicio, dataHoraFim);
        
        // Remove o agendamento atual da lista de conflitos
        agendamentosExistentes.removeIf(a -> a.getId().equals(agendamentoId));
        
        return agendamentosExistentes.isEmpty();
    }
} 