package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Agendamento.StatusAgendamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AgendamentoDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private LocalDateTime dataHora;
        private Long clienteId;
        private Long profissionalId;
        private Long servicoId;
        private Long salaoId;
        private String observacoes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private LocalDateTime dataHora;
        private StatusAgendamento status;
        private UsuarioDTO.Response cliente;
        private UsuarioDTO.Response profissional;
        private ServicoDTO.Response servico;
        private SalaoDTO.Response salao;
        private String observacoes;
    }
} 