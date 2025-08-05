package com.beauty.com.MatchBeauty.dto;

/**
 * DTO para edição de usuários pelo administrador
 * Contém os dados necessários para atualizar um usuário existente
 */
public class EditarUsuarioDTO {
    private Long id;
    private String username;
    private String nome;
    private String email;
    private String telefone;
    private String tipoUsuario;
    private String password; // Opcional, só atualiza se fornecido

    // Construtores
    public EditarUsuarioDTO() {}

    public EditarUsuarioDTO(Long id, String username, String nome, String email, String telefone, String tipoUsuario) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "EditarUsuarioDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                ", password='" + (password != null ? "[PROTECTED]" : "null") + '\'' +
                '}';
    }
} 