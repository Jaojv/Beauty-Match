package com.beauty.com.MatchBeauty.dto;

/**
 * DTO para estatísticas do dashboard administrativo
 * Contém contadores de usuários e salões para exibição no painel
 */
public class DashboardStatsDTO {
    
    private Long totalUsuarios;
    private Long totalSaloes;
    private Long totalClientes;
    private Long totalProfissionais;
    private Long totalProprietarios;
    private Long totalAdmins;
    private Long saloesPendentes;
    private Long saloesAprovados;
    private Long saloesRejeitados;

    // Construtor padrão
    public DashboardStatsDTO() {}

    // Construtor com todos os campos
    public DashboardStatsDTO(Long totalUsuarios, Long totalSaloes, Long totalClientes, 
                           Long totalProfissionais, Long totalProprietarios, Long totalAdmins,
                           Long saloesPendentes, Long saloesAprovados, Long saloesRejeitados) {
        this.totalUsuarios = totalUsuarios;
        this.totalSaloes = totalSaloes;
        this.totalClientes = totalClientes;
        this.totalProfissionais = totalProfissionais;
        this.totalProprietarios = totalProprietarios;
        this.totalAdmins = totalAdmins;
        this.saloesPendentes = saloesPendentes;
        this.saloesAprovados = saloesAprovados;
        this.saloesRejeitados = saloesRejeitados;
    }

    // Getters e Setters
    public Long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Long getTotalSaloes() {
        return totalSaloes;
    }

    public void setTotalSaloes(Long totalSaloes) {
        this.totalSaloes = totalSaloes;
    }

    public Long getTotalClientes() {
        return totalClientes;
    }

    public void setTotalClientes(Long totalClientes) {
        this.totalClientes = totalClientes;
    }

    public Long getTotalProfissionais() {
        return totalProfissionais;
    }

    public void setTotalProfissionais(Long totalProfissionais) {
        this.totalProfissionais = totalProfissionais;
    }

    public Long getTotalProprietarios() {
        return totalProprietarios;
    }

    public void setTotalProprietarios(Long totalProprietarios) {
        this.totalProprietarios = totalProprietarios;
    }

    public Long getTotalAdmins() {
        return totalAdmins;
    }

    public void setTotalAdmins(Long totalAdmins) {
        this.totalAdmins = totalAdmins;
    }

    public Long getSaloesPendentes() {
        return saloesPendentes;
    }

    public void setSaloesPendentes(Long saloesPendentes) {
        this.saloesPendentes = saloesPendentes;
    }

    public Long getSaloesAprovados() {
        return saloesAprovados;
    }

    public void setSaloesAprovados(Long saloesAprovados) {
        this.saloesAprovados = saloesAprovados;
    }

    public Long getSaloesRejeitados() {
        return saloesRejeitados;
    }

    public void setSaloesRejeitados(Long saloesRejeitados) {
        this.saloesRejeitados = saloesRejeitados;
    }
} 