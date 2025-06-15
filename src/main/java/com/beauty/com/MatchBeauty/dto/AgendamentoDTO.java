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
        private UsuarioDTO.Response cliente;
        private UsuarioDTO.Response profissional;
        private ServicoDTO.Response servico;
        private SalaoDTO.Response salao;
        private String observacoes;
        private Double valorServico;

        public Response() {
        }

        public Response(Long id, LocalDateTime dataHora, Agendamento.StatusAgendamento status,
                       UsuarioDTO.Response cliente, UsuarioDTO.Response profissional,
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
        
        public static Response fromEntity(Agendamento agendamento) {
            if (agendamento == null) return null;
            
            UsuarioDTO.Response clienteDTO = null;
            if (agendamento.getCliente() != null) {
                clienteDTO = new UsuarioDTO.Response(
                    agendamento.getCliente().getIdUsuario(),
                    agendamento.getCliente().getNome(),
                    agendamento.getCliente().getEmail(),
                    agendamento.getCliente().getTelefone(),
                    agendamento.getCliente().getTipoUsuario()
                );
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
        
        public static Agendamento toEntity(Request request) {
            if (request == null) return null;
            
            Agendamento agendamento = new Agendamento();
            agendamento.setDataHora(request.getDataHora());
            
            Usuario cliente = new Usuario();
            cliente.setIdUsuario(request.getClienteId());
            agendamento.setCliente(cliente);
            
            Usuario profissional = new Usuario();
            profissional.setIdUsuario(request.getProfissionalId());
            agendamento.setProfissional(profissional);
            
            Servico servico = new Servico();
            servico.setId(request.getServicoId());
            agendamento.setServico(servico);
            
            Salao salao = new Salao();
            salao.setId(request.getSalaoId());
            agendamento.setSalao(salao);
            
            agendamento.setObservacoes(request.getObservacoes());
            agendamento.setStatus(Agendamento.StatusAgendamento.AGENDADO);
            
            return agendamento;
        }
    }
}