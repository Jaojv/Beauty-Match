// Sistema de gerenciamento de agendamentos
// Controla exibição, filtros e ações dos agendamentos do cliente

// Gerenciador da página de agendamentos
class AgendamentosManager {
    constructor() {
        this.agendamentos = [];
        this.filtros = {
            estabelecimento: '',
            status: ''
        };
        this.init();
    }

    // Inicializa o sistema de agendamentos
    async init() {
        console.log('AgendamentosJS: Inicializando...');
        
        // Verificar se o usuário está logado
        if (!this.isUserLoggedIn()) {
            console.log('AgendamentosJS: Usuário não logado, redirecionando...');
            window.location.href = 'login.html';
            return;
        }

        // Configurar filtros
        this.configurarFiltros();
        
        // Carregar agendamentos
        await this.carregarAgendamentos();
        
        console.log('AgendamentosJS: Inicializado com sucesso');
    }

    // Verifica se o usuário está logado
    isUserLoggedIn() {
        return localStorage.getItem('authToken') !== null;
    }

    // Configura filtros de estabelecimento e status
    configurarFiltros() {
        console.log('AgendamentosJS: Configurando filtros...');
        
        const filtroEstabelecimento = document.getElementById('filtro-estabelecimento');
        const filtroStatus = document.getElementById('filtro-status');

        if (filtroEstabelecimento) {
            filtroEstabelecimento.addEventListener('change', (e) => {
                this.filtros.estabelecimento = e.target.value;
                this.aplicarFiltros();
            });
        }

        if (filtroStatus) {
            filtroStatus.addEventListener('change', (e) => {
                this.filtros.status = e.target.value;
                this.aplicarFiltros();
            });
        }
    }

    // Carrega agendamentos da API
    async carregarAgendamentos() {
        console.log('AgendamentosJS: Carregando agendamentos...');
        
        try {
            this.mostrarLoading();
            
            // Buscar agendamentos do cliente
            this.agendamentos = await apiClient.getAgendamentosCliente();
            
            console.log('AgendamentosJS: Agendamentos carregados:', this.agendamentos.length);
            
            // Popular filtro de estabelecimentos
            this.popularFiltroEstabelecimentos();
            
            // Renderizar agendamentos
            this.renderizarAgendamentos();
            
        } catch (error) {
            console.error('AgendamentosJS: Erro ao carregar agendamentos:', error);
            this.mostrarErro('Erro ao carregar agendamentos. Tente novamente.');
        }
    }

    // Popula o filtro de estabelecimentos com dados únicos
    popularFiltroEstabelecimentos() {
        const filtroEstabelecimento = document.getElementById('filtro-estabelecimento');
        if (!filtroEstabelecimento) return;

        // Obter estabelecimentos únicos
        const estabelecimentos = [...new Set(this.agendamentos.map(a => a.salao?.nome).filter(Boolean))];
        
        // Limpar opções existentes (exceto a primeira)
        filtroEstabelecimento.innerHTML = '<option value="">Todos os estabelecimentos</option>';
        
        // Adicionar opções
        estabelecimentos.forEach(estabelecimento => {
            const option = document.createElement('option');
            option.value = estabelecimento;
            option.textContent = estabelecimento;
            filtroEstabelecimento.appendChild(option);
        });
    }

    // Aplica filtros selecionados aos agendamentos
    aplicarFiltros() {
        console.log('AgendamentosJS: Aplicando filtros:', this.filtros);
        
        let agendamentosFiltrados = [...this.agendamentos];

        // Filtrar por estabelecimento
        if (this.filtros.estabelecimento) {
            agendamentosFiltrados = agendamentosFiltrados.filter(
                a => a.salao?.nome === this.filtros.estabelecimento
            );
        }

        // Filtrar por status
        if (this.filtros.status) {
            agendamentosFiltrados = agendamentosFiltrados.filter(
                a => a.status === this.filtros.status
            );
        }

        this.renderizarAgendamentos(agendamentosFiltrados);
    }

    renderizarAgendamentos(agendamentos = this.agendamentos) {
        console.log('AgendamentosJS: Renderizando agendamentos:', agendamentos.length);
        
        const container = document.getElementById('lista-agendamentos');
        const semAgendamentos = document.getElementById('sem-agendamentos');
        
        if (!container) return;

        if (agendamentos.length === 0) {
            container.style.display = 'none';
            if (semAgendamentos) {
                semAgendamentos.style.display = 'block';
            }
            return;
        }

        container.style.display = 'grid';
        if (semAgendamentos) {
            semAgendamentos.style.display = 'none';
        }

        container.innerHTML = '';

        agendamentos.forEach(agendamento => {
            const card = this.criarCardAgendamento(agendamento);
            container.appendChild(card);
        });
    }

    criarCardAgendamento(agendamento) {
        const card = document.createElement('div');
        card.className = 'agendamento-card';
        
        const dataHora = new Date(agendamento.dataHora);
        const dataFormatada = dataHora.toLocaleDateString('pt-BR');
        const horaFormatada = dataHora.toLocaleTimeString('pt-BR', { 
            hour: '2-digit', 
            minute: '2-digit' 
        });

        const statusClass = this.getStatusClass(agendamento.status);
        const statusText = this.getStatusText(agendamento.status);

        card.innerHTML = `
            <div class="agendamento-header">
                <div class="agendamento-info">
                    <h3>${agendamento.servico?.nome || 'Serviço'}</h3>
                    <p>${agendamento.salao?.nome || 'Salão'}</p>
                </div>
                <span class="status-badge ${statusClass}">${statusText}</span>
            </div>
            
            <div class="agendamento-detalhes">
                <div class="detalhe-item">
                    <i class="fas fa-calendar"></i>
                    <strong>Data:</strong> ${dataFormatada}
                </div>
                <div class="detalhe-item">
                    <i class="fas fa-clock"></i>
                    <strong>Horário:</strong> ${horaFormatada}
                </div>
                <div class="detalhe-item">
                    <i class="fas fa-user"></i>
                    <strong>Profissional:</strong> ${agendamento.profissional?.nome || 'Não informado'}
                </div>
                <div class="detalhe-item">
                    <i class="fas fa-dollar-sign"></i>
                    <strong>Valor:</strong> R$ ${agendamento.valorServico?.toFixed(2).replace('.', ',') || '0,00'}
                </div>
                ${agendamento.observacoes ? `
                <div class="detalhe-item">
                    <i class="fas fa-comment"></i>
                    <strong>Observações:</strong> ${agendamento.observacoes}
                </div>
                ` : ''}
            </div>
            
            <div class="agendamento-acoes">
                ${agendamento.status === 'AGENDADO' ? `
                    <button class="btn-acao btn-cancelar" data-agendamento-id="${agendamento.id}">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                ` : ''}
                <button class="btn-acao btn-ver-detalhes" data-agendamento-id="${agendamento.id}">
                    <i class="fas fa-eye"></i> Ver Detalhes
                </button>
                ${agendamento.status === 'CONCLUIDO' ? `
                    <button class="btn-acao btn-reagendar" data-agendamento-id="${agendamento.id}">
                        <i class="fas fa-redo"></i> Reagendar
                    </button>
                ` : ''}
            </div>
        `;

        // Adicionar event listeners após criar o HTML
        this.adicionarEventListeners(card, agendamento);

        return card;
    }

    adicionarEventListeners(card, agendamento) {
        // Botão cancelar
        const btnCancelar = card.querySelector('.btn-cancelar');
        if (btnCancelar) {
            btnCancelar.addEventListener('click', () => {
                console.log('AgendamentosJS: Botão cancelar clicado para agendamento:', agendamento.id);
                this.cancelarAgendamento(agendamento.id);
            });
        }

        // Botão ver detalhes
        const btnVerDetalhes = card.querySelector('.btn-ver-detalhes');
        if (btnVerDetalhes) {
            btnVerDetalhes.addEventListener('click', () => {
                console.log('AgendamentosJS: Botão ver detalhes clicado para agendamento:', agendamento.id);
                this.verDetalhes(agendamento.id);
            });
        }

        // Botão reagendar
        const btnReagendar = card.querySelector('.btn-reagendar');
        if (btnReagendar) {
            btnReagendar.addEventListener('click', () => {
                console.log('AgendamentosJS: Botão reagendar clicado para agendamento:', agendamento.id);
                this.reagendar(agendamento.id);
            });
        }
    }

    getStatusClass(status) {
        switch (status) {
            case 'AGENDADO': return 'status-agendado';
            case 'CONCLUIDO': return 'status-concluido';
            case 'CANCELADO': return 'status-cancelado';
            default: return 'status-agendado';
        }
    }

    getStatusText(status) {
        switch (status) {
            case 'AGENDADO': return 'Agendado';
            case 'CONCLUIDO': return 'Concluído';
            case 'CANCELADO': return 'Cancelado';
            default: return 'Agendado';
        }
    }

    async cancelarAgendamento(agendamentoId) {
        console.log('AgendamentosJS: Função cancelarAgendamento chamada com ID:', agendamentoId);
        console.log('AgendamentosJS: this.agendamentos:', this.agendamentos);
        
        // Mostrar modal de confirmação
        const confirmado = await this.mostrarModalConfirmacao(
            'Cancelar Agendamento',
            'Tem certeza que deseja cancelar este agendamento? Esta ação não pode ser desfeita.'
        );

        console.log('AgendamentosJS: Usuário confirmou cancelamento:', confirmado);

        if (!confirmado) {
            console.log('AgendamentosJS: Cancelamento cancelado pelo usuário');
            return;
        }

        try {
            console.log('AgendamentosJS: Chamando API para cancelar agendamento...');
            await apiClient.cancelarAgendamento(agendamentoId);
            
            console.log('AgendamentosJS: Agendamento cancelado com sucesso');
            
            // Recarregar agendamentos
            await this.carregarAgendamentos();
            
            // Mostrar mensagem de sucesso
            this.mostrarMensagem('Agendamento cancelado com sucesso!', 'success');
            
        } catch (error) {
            console.error('AgendamentosJS: Erro ao cancelar agendamento:', error);
            this.mostrarMensagem('Erro ao cancelar agendamento. Tente novamente.', 'error');
        }
    }

    verDetalhes(agendamentoId) {
        console.log('🔧 AgendamentosJS: Ver detalhes do agendamento:', agendamentoId);
        
        const agendamento = this.agendamentos.find(a => a.id === agendamentoId);
        if (!agendamento) {
            this.mostrarMensagem('Agendamento não encontrado.', 'error');
            return;
        }

        // Aqui você pode implementar um modal com detalhes completos
        // Por enquanto, vamos mostrar um alert com as informações
        const dataHora = new Date(agendamento.dataHora);
        const detalhes = `
Detalhes do Agendamento:

Serviço: ${agendamento.servico?.nome || 'Não informado'}
Salão: ${agendamento.salao?.nome || 'Não informado'}
Profissional: ${agendamento.profissional?.nome || 'Não informado'}
Data: ${dataHora.toLocaleDateString('pt-BR')}
Horário: ${dataHora.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}
Valor: R$ ${agendamento.valorServico?.toFixed(2).replace('.', ',') || '0,00'}
Status: ${this.getStatusText(agendamento.status)}
${agendamento.observacoes ? `Observações: ${agendamento.observacoes}` : ''}
        `;

        alert(detalhes);
    }

    reagendar(agendamentoId) {
        console.log('AgendamentosJS: Reagendar agendamento:', agendamentoId);
        
        // Redirecionar para a página de serviços do salão
        const agendamento = this.agendamentos.find(a => a.id === agendamentoId);
        if (agendamento && agendamento.salao?.id) {
            window.location.href = `servicos.html?salaoId=${agendamento.salao.id}`;
        } else {
            this.mostrarMensagem('Erro ao redirecionar para reagendamento.', 'error');
        }
    }

    mostrarModalConfirmacao(titulo, mensagem) {
        return new Promise((resolve) => {
            const modal = document.createElement('div');
            modal.className = 'modal-confirmacao';
            modal.innerHTML = `
                <div class="modal-conteudo">
                    <h3>${titulo}</h3>
                    <p>${mensagem}</p>
                    <div class="botoes">
                        <button class="btn-confirmar manter">
                            Manter
                        </button>
                        <button class="btn-confirmar cancelar">
                            Confirmar
                        </button>
                    </div>
                </div>
            `;

            document.body.appendChild(modal);

            // Adicionar event listeners para os botões
            const btnManter = modal.querySelector('.btn-confirmar.manter');
            const btnConfirmar = modal.querySelector('.btn-confirmar.cancelar');

            btnManter.addEventListener('click', () => {
                modal.remove();
                resolve(false);
            });

            btnConfirmar.addEventListener('click', () => {
                modal.remove();
                resolve(true);
            });

            // Fechar modal ao clicar fora
            modal.addEventListener('click', (e) => {
                if (e.target === modal) {
                    modal.remove();
                    resolve(false);
                }
            });
        });
    }

    mostrarLoading() {
        const container = document.getElementById('lista-agendamentos');
        if (container) {
            container.innerHTML = '<div class="loading"><p>Carregando agendamentos...</p></div>';
        }
    }

    mostrarErro(mensagem) {
        const container = document.getElementById('lista-agendamentos');
        if (container) {
            container.innerHTML = `
                <div class="loading">
                    <p style="color: #ff6b6b;">${mensagem}</p>
                    <button onclick="agendamentosManager.carregarAgendamentos()" style="margin-top: 10px; padding: 8px 16px; background: #d46b6b; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        Tentar Novamente
                    </button>
                </div>
            `;
        }
    }

    mostrarMensagem(mensagem, tipo = 'info') {
        // Criar uma notificação temporária
        const notificacao = document.createElement('div');
        notificacao.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 8px;
            color: white;
            font-weight: 500;
            z-index: 1000;
            animation: slideIn 0.3s ease;
        `;

        if (tipo === 'success') {
            notificacao.style.background = '#4caf50';
        } else if (tipo === 'error') {
            notificacao.style.background = '#f44336';
        } else {
            notificacao.style.background = '#2196f3';
        }

        notificacao.textContent = mensagem;
        document.body.appendChild(notificacao);

        // Remover após 3 segundos
        setTimeout(() => {
            notificacao.style.animation = 'slideOut 0.3s ease';
            setTimeout(() => notificacao.remove(), 300);
        }, 3000);
    }
}

// Inicializar quando a página carregar
let agendamentosManager;

document.addEventListener('DOMContentLoaded', () => {
    console.log('AgendamentosJS: DOM carregado, iniciando...');
    try {
        agendamentosManager = new AgendamentosManager();
        console.log('AgendamentosJS: AgendamentosManager inicializado com sucesso');
        
        // Verificar se a variável global está disponível
        window.agendamentosManager = agendamentosManager;
        console.log('AgendamentosJS: Variável global agendamentosManager definida');
        
    } catch (error) {
        console.error('AgendamentosJS: Erro ao inicializar AgendamentosManager:', error);
    }
});

// Adicionar estilos CSS para animações
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOut {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
`;
document.head.appendChild(style); 