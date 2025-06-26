package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Agendamento;
import com.beauty.com.MatchBeauty.entity.Salao;
import com.beauty.com.MatchBeauty.entity.Servico;
import com.beauty.com.MatchBeauty.entity.StatusAgendamento;
import com.beauty.com.MatchBeauty.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AgendamentoDTO {
    
   
    public static class Request {
        private LocalDateTime dataHora;
        private Long clienteId;
        private Long profissionalId;
        private Long servicoId;
        private Long salaoId;
        private String observacoes;

        public Request() {
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public void setDataHora(LocalDateTime dataHora) {
            this.dataHora = dataHora;
        }

        public Long getClienteId() {
            return clienteId;
        }

        public void setClienteId(Long clienteId) {
            this.clienteId = clienteId;
        }

        public Long getProfissionalId() {
            return profissionalId;
        }

        public void setProfissionalId(Long profissionalId) {
            this.profissionalId = profissionalId;
        }

        public Long getServicoId() {
            return servicoId;
        }

        public void setServicoId(Long servicoId) {
            this.servicoId = servicoId;
        }

        public Long getSalaoId() {
            return salaoId;
        }

        public void setSalaoId(Long salaoId) {
            this.salaoId = salaoId;
        }

        public String getObservacoes() {
            return observacoes;
        }

        public void setObservacoes(String observacoes) {
            this.observacoes = observacoes;
        }
    }
    
  
    public static class Response {
        private Long id;
        private LocalDateTime dataHora;
        private Agendamento.StatusAgendamento status;
        private ClienteDTO cliente;
        private UsuarioDTO.Response profissional;
        private ServicoDTO.Response servico;
        private SalaoDTO.Response salao;
        private String observacoes;
        private Double valorServico;

        public Response() {
        }

        public Response(Long id, LocalDateTime dataHora, Agendamento.StatusAgendamento status,
                       ClienteDTO cliente, UsuarioDTO.Response profissional,
                       ServicoDTO.Response servico, SalaoDTO.Response salao,
                       String observacoes, Double valorServico) {
            this.id = id;
            this.dataHora = dataHora;
            this.status = status;
            this.cliente = cliente;
            this.profissional = profissional;
            this.servico = servico;
            this.salao = salao;
            this.observacoes = observacoes;
            this.valorServico = valorServico;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public LocalDateTime getDataHora() { return dataHora; }
        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

        public Agendamento.StatusAgendamento getStatus() { return status; }
        public void setStatus(Agendamento.StatusAgendamento status) { this.status = status; }

        public ClienteDTO getCliente() { return cliente; }
        public void setCliente(ClienteDTO cliente) { this.cliente = cliente; }

        public UsuarioDTO.Response getProfissional() { return profissional; }
        public void setProfissional(UsuarioDTO.Response profissional) { this.profissional = profissional; }

        public ServicoDTO.Response getServico() { return servico; }
        public void setServico(ServicoDTO.Response servico) { this.servico = servico; }

        public SalaoDTO.Response getSalao() { return salao; }
        public void setSalao(SalaoDTO.Response salao) { this.salao = salao; }

        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

        public Double getValorServico() { return valorServico; }
        public void setValorServico(Double valorServico) { this.valorServico = valorServico; }
        
        public static Response fromEntity(Agendamento agendamento) {
            if (agendamento == null) return null;
            
            ClienteDTO clienteDTO = null;
            if (agendamento.getCliente() != null) {
                clienteDTO = new ClienteDTO();
                clienteDTO.setClienteId(agendamento.getCliente().getIdUsuario());
                clienteDTO.setNome(agendamento.getCliente().getNome());
                clienteDTO.setEmail(agendamento.getCliente().getEmail());
                clienteDTO.setTelefone(agendamento.getCliente().getTelefone());
                // Adicione outros campos conforme necessário
            }
            
            UsuarioDTO.Response profissionalDTO = null;
            if (agendamento.getProfissional() != null) {
                profissionalDTO = new UsuarioDTO.Response(
                    agendamento.getProfissional().getIdUsuario(),
                    agendamento.getProfissional().getNome(),
                    agendamento.getProfissional().getEmail(),
                    agendamento.getProfissional().getTelefone(),
                    agendamento.getProfissional().getTipoUsuario()
                );
            }
            
            ServicoDTO.Response servicoDTO = null;
            if (agendamento.getServico() != null) {
                servicoDTO = new ServicoDTO.Response(
                    agendamento.getServico().getId(),
                    agendamento.getServico().getNome(),
                    agendamento.getServico().getDescricao(),
                    agendamento.getServico().getPreco(),
                    agendamento.getServico().getDuracaoMinutos(),
                    agendamento.getServico().getSalao() != null ? agendamento.getServico().getSalao().getId() : null
                );
            }
            
            SalaoDTO.Response salaoDTO = null;
            if (agendamento.getSalao() != null) {
                salaoDTO = new SalaoDTO.Response(
                    agendamento.getSalao().getId(),
                    agendamento.getSalao().getNome(),
                    agendamento.getSalao().getEndereco(),
                    agendamento.getSalao().getTelefone(),
                    agendamento.getSalao().getEmail(),
                    agendamento.getSalao().getDescricao(),
                    null, // Não incluir proprietário para evitar recursão
                    null  // Não incluir serviços para evitar recursão
                );
            }
            
            Double valorServico = agendamento.getServico() != null ? 
                agendamento.getServico().getPreco().doubleValue() : 0.0;
            
            return new Response(
                agendamento.getId(),
                agendamento.getDataHora(),
                agendamento.getStatus(),
                clienteDTO,
                profissionalDTO,
                servicoDTO,
                salaoDTO,
                agendamento.getObservacoes(),
                valorServico
            );
        }
    }
}