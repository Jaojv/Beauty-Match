package com.beauty.com.MatchBeauty.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import com.beauty.com.MatchBeauty.entity.Salao;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "profissional")
@DiscriminatorValue("PROFISSIONAL")
public class Profissional extends Usuario {
    
    @Column(name = "cpf")
    private String cpf;
    
    @Column(name = "especialidade")
    private String especialidade;
    
    @Column(name = "biografia")
    private String biografia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salao_id")
    private Salao salao;
    
    public Profissional() {
        super();
    }

    public Profissional(Long idUsuario, String username, String password, String email, String telefone, 
                       LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        super(idUsuario, username, password, email, telefone, criadoEm, atualizadoEm);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public Salao getSalao() {
        return salao;
    }

    public void setSalao(Salao salao) {
        this.salao = salao;
    }
    // Pode adicionar métodos específicos de Profissional se necessário
} 