package com.beauty.com.MatchBeauty.dto;

import com.beauty.com.MatchBeauty.entity.Profissional;
import com.beauty.com.MatchBeauty.entity.Salao;

// DTO para dados de profissionais
public class ProfissionalDTO {
    
    // Classe para requisições de profissional
    public static class Request {
    private String username;
    private String password;
    private String email;
    private String telefone;
    private String nome;
    private String cpf;
    private String especialidade;
    private String biografia;
    private Long salaoId;

        // Getters e Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getEspecialidade() { return especialidade; }
        public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
        
        public String getBiografia() { return biografia; }
        public void setBiografia(String biografia) { this.biografia = biografia; }
        
        public Long getSalaoId() { return salaoId; }
        public void setSalaoId(Long salaoId) { this.salaoId = salaoId; }
    }
    
    // Classe para respostas de profissional
    public static class Response {
        private Long idUsuario;
        private String username;
        private String email;
        private String telefone;
        private String nome;
        private String cpf;
        private String especialidade;
        private String biografia;
        private SalaoResumoDTO salao;

        public Response() {}

        public Response(Profissional profissional) {
            this.idUsuario = profissional.getIdUsuario();
            this.username = profissional.getUsername();
            this.email = profissional.getEmail();
            this.telefone = profissional.getTelefone();
            this.nome = profissional.getNome();
            this.cpf = profissional.getCpf();
            this.especialidade = profissional.getEspecialidade();
            this.biografia = profissional.getBiografia();
            if (profissional.getSalao() != null) {
                this.salao = new SalaoResumoDTO(profissional.getSalao());
            }
        }

        // Getters e Setters
        public Long getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        
        public String getEspecialidade() { return especialidade; }
        public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
        
        public String getBiografia() { return biografia; }
        public void setBiografia(String biografia) { this.biografia = biografia; }
        
        public SalaoResumoDTO getSalao() { return salao; }
        public void setSalao(SalaoResumoDTO salao) { this.salao = salao; }
    }
    
    // DTO resumido para salão
    public static class SalaoResumoDTO {
        private Long id;
        private String nome;
        private String endereco;
        private String telefone;
        private String email;

        public SalaoResumoDTO() {}

        public SalaoResumoDTO(Salao salao) {
            this.id = salao.getId();
            this.nome = salao.getNome();
            this.endereco = salao.getEndereco();
            this.telefone = salao.getTelefone();
            this.email = salao.getEmail();
        }

        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
} 