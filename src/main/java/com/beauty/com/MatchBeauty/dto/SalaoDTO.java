package com.beauty.com.MatchBeauty.dto;

import java.util.List;

// DTO para transferência de dados de salão
// Contém classes Request e Response para salões
public class SalaoDTO {
    
    // Request - dados enviados pelo frontend para criar/atualizar salão
    public static class Request {
        private String nome;
        private String endereco;
        private String telefone;
        private String email;
        private String descricao;
        private String imagemUrl;
        private String horarioFuncionamento;
        private Long proprietarioId;

        public Request() {
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getImagemUrl() {
            return imagemUrl;
        }

        public void setImagemUrl(String imagemUrl) {
            this.imagemUrl = imagemUrl;
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
    
    // Response - dados retornados pelo backend com informações completas
    public static class Response {
        private Long id;
        private String nome;
        private String endereco;
        private String telefone;
        private String email;
        private String descricao;
        private String imagemUrl;
        private String horarioFuncionamento;
        private UsuarioDTO.Response proprietario;
        private List<ServicoDTO.Response> servicos;

        public Response() {
        }

        public Response(Long id, String nome, String endereco, String telefone, 
                       String email, String descricao, UsuarioDTO.Response proprietario, 
                       List<ServicoDTO.Response> servicos) {
            this.id = id;
            this.nome = nome;
            this.endereco = endereco;
            this.telefone = telefone;
            this.email = email;
            this.descricao = descricao;
            this.proprietario = proprietario;
            this.servicos = servicos;
        }

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getImagemUrl() {
            return imagemUrl;
        }

        public void setImagemUrl(String imagemUrl) {
            this.imagemUrl = imagemUrl;
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

        public List<ServicoDTO.Response> getServicos() {
            return servicos;
        }

        public void setServicos(List<ServicoDTO.Response> servicos) {
            this.servicos = servicos;
        }
    }
} 