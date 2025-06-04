package com.beauty.com.MatchBeauty.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public class SalaoDTO {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String nome;
        private String endereco;
        private String telefone;
        private String descricao;
        private String horarioFuncionamento;
        private Long proprietarioId;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getHorarioFuncionamento() {
            return horarioFuncionamento;
        }

        public void setHorarioFuncionamento(String horarioFuncionamento) {
            this.horarioFuncionamento = horarioFuncionamento;
        }

        public Long getProprietarioId() {
            return proprietarioId;
        }

        public void setProprietarioId(Long proprietarioId) {
            this.proprietarioId = proprietarioId;
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String nome;
        private String endereco;
        private String telefone;
        private String descricao;
        private String horarioFuncionamento;
        private UsuarioDTO.Response proprietario;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEndereco() {
            return endereco;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getHorarioFuncionamento() {
            return horarioFuncionamento;
        }

        public void setHorarioFuncionamento(String horarioFuncionamento) {
            this.horarioFuncionamento = horarioFuncionamento;
        }

        public UsuarioDTO.Response getProprietario() {
            return proprietario;
        }

        public void setProprietario(UsuarioDTO.Response proprietario) {
            this.proprietario = proprietario;
        }
    }
} 