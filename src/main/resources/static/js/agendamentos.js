// Classe para gerenciar operações de agendamentos
class AgendamentoService {
    constructor(apiService) {
        this.apiService = apiService;
    }

    // Buscar todos os agendamentos do usuário logado
    async buscarMeusAgendamentos() {
        try {
            const user = authService.getCurrentUser();
            if (!user) throw new Error('Usuário não autenticado');

            switch (user.tipoUsuario.toLowerCase()) {
                case 'cliente':
                    return await this.apiService.get('/agendamentos/cliente');
                case 'profissional':
                    return await this.apiService.get('/agendamentos/profissional');
                case 'proprietario':
                    return await this.apiService.get('/agendamentos/proprietario');
                default:
                    return await this.apiService.get('/agendamentos');
            }
        } catch (error) {
            console.error('Erro ao buscar agendamentos:', error);
            throw error;
        }
    }

    // Buscar agendamento por ID
    async buscarAgendamentoPorId(id) {
        try {
            return await this.apiService.get(`/agendamentos/${id}`);
        } catch (error) {
            console.error('Erro ao buscar agendamento:', error);
            throw error;
        }
    }

    // Criar novo agendamento
    async criarAgendamento(agendamentoData) {
        try {
            return await this.apiService.post('/agendamentos', agendamentoData);
        } catch (error) {
            console.error('Erro ao criar agendamento:', error);
            throw error;
        }
    }

    // Cancelar agendamento
    async cancelarAgendamento(id) {
        try {
            return await this.apiService.put(`/agendamentos/${id}/cancelar`);
        } catch (error) {
            console.error('Erro ao cancelar agendamento:', error);
            throw error;
        }
    }

    // Concluir agendamento
    async concluirAgendamento(id) {
        try {
            return await this.apiService.put(`/agendamentos/${id}/concluir`);
        } catch (error) {
            console.error('Erro ao concluir agendamento:', error);
            throw error;
        }
    }

    // Buscar horários disponíveis
    async buscarHorariosDisponiveis(salaoId, servicoId, profissionalId, data) {
        try {
            const params = new URLSearchParams({
                salaoId: salaoId,
                servicoId: servicoId,
                profissionalId: profissionalId,
                data: data
            });
            return await this.apiService.get(`/agendamentos/disponibilidade?${params}`);
        } catch (error) {
            console.error('Erro ao buscar horários disponíveis:', error);
            throw error;
        }
    }
}

// Classe para UI de agendamentos
class AgendamentoUI {
    constructor(agendamentoService) {
        this.agendamentoService = agendamentoService;
        this.agendamentoAtual = null;
    }

    // Renderizar lista de agendamentos
    async renderizarAgendamentos(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        try {
            container.innerHTML = '<div class="loading">Carregando agendamentos...</div>';
            
            const agendamentos = await this.agendamentoService.buscarMeusAgendamentos();
            
            if (agendamentos.length === 0) {
                container.innerHTML = '<div class="no-data">Nenhum agendamento encontrado.</div>';
                return;
            }

            const agendamentosHTML = agendamentos.map(agendamento => this.criarCardAgendamento(agendamento)).join('');
            container.innerHTML = agendamentosHTML;

        } catch (error) {
            container.innerHTML = '<div class="error">Erro ao carregar agendamentos. Tente novamente.</div>';
            console.error('Erro ao renderizar agendamentos:', error);
        }
    }

    // Criar card de agendamento
    criarCardAgendamento(agendamento) {
        const dataFormatada = new Date(agendamento.dataHora).toLocaleDateString('pt-BR');
        const horaFormatada = new Date(agendamento.dataHora).toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit'
        });
        
        const statusClass = this.getStatusClass(agendamento.status);
        const statusText = this.getStatusText(agendamento.status);

        return `
            <div class="card agendamento-card ${statusClass}" data-agendamento-id="${agendamento.id}">
                <div class="card-header">
                    <h3>${agendamento.servico.nome}</h3>
                    <span class="status-badge ${statusClass}">${statusText}</span>
                </div>
                <div class="card-body">
                    <div class="agendamento-info">
                        <div class="info-item">
                            <img src="../images/calendar.png" alt="Data" class="info-icon">
                            <span><strong>Data:</strong> ${dataFormatada} às ${horaFormatada}</span>
                        </div>
                        <div class="info-item">
                            <img src="../images/salao.png" alt="Salão" class="info-icon">
                            <span><strong>Salão:</strong> ${agendamento.salao.nome}</span>
                        </div>
                        <div class="info-item">
                            <img src="../images/user.png" alt="Profissional" class="info-icon">
                            <span><strong>Profissional:</strong> ${agendamento.profissional.nome}</span>
                        </div>
                        <div class="info-item">
                            <img src="../images/price.png" alt="Preço" class="info-icon">
                            <span><strong>Preço:</strong> R$ ${agendamento.servico.preco}</span>
                        </div>
                    </div>
                    ${agendamento.observacoes ? `
                        <div class="observacoes">
                            <strong>Observações:</strong> ${agendamento.observacoes}
                        </div>
                    ` : ''}
                </div>
                <div class="card-footer">
                    ${this.getBotoesAgendamento(agendamento)}
                </div>
            </div>
        `;
    }

    // Obter classe CSS baseada no status
    getStatusClass(status) {
        switch (status.toLowerCase()) {
            case 'agendado': return 'status-agendado';
            case 'concluido': return 'status-concluido';
            case 'cancelado': return 'status-cancelado';
            case 'faltante': return 'status-faltante';
            default: return 'status-default';
        }
    }

    // Obter texto do status
    getStatusText(status) {
        switch (status.toLowerCase()) {
            case 'agendado': return 'Agendado';
            case 'concluido': return 'Concluído';
            case 'cancelado': return 'Cancelado';
            case 'faltante': return 'Faltante';
            default: return status;
        }
    }

    // Obter botões baseados no status e tipo de usuário
    getBotoesAgendamento(agendamento) {
        const user = authService.getCurrentUser();
        const isCliente = user.tipoUsuario.toLowerCase() === 'cliente';
        const isProfissional = user.tipoUsuario.toLowerCase() === 'profissional';
        const isProprietario = user.tipoUsuario.toLowerCase() === 'proprietario';
        
        let botoes = '';

        if (agendamento.status.toLowerCase() === 'agendado') {
            if (isCliente || isProfissional || isProprietario) {
                botoes += `<button class="botao btn-danger" onclick="agendamentoUI.cancelarAgendamento(${agendamento.id})">
                    Cancelar
                </button>`;
            }
            
            if (isProfissional || isProprietario) {
                botoes += `<button class="botao btn-success" onclick="agendamentoUI.concluirAgendamento(${agendamento.id})">
                    Concluir
                </button>`;
            }
        }

        botoes += `<button class="botao btn-secondary" onclick="agendamentoUI.verDetalhesAgendamento(${agendamento.id})">
            Ver Detalhes
        </button>`;

        return botoes;
    }

    // Cancelar agendamento
    async cancelarAgendamento(id) {
        if (!confirm('Tem certeza que deseja cancelar este agendamento?')) {
            return;
        }

        try {
            await this.agendamentoService.cancelarAgendamento(id);
            alert('Agendamento cancelado com sucesso!');
            this.renderizarAgendamentos('agendamentos-container');
        } catch (error) {
            alert('Erro ao cancelar agendamento. Tente novamente.');
            console.error('Erro ao cancelar agendamento:', error);
        }
    }

    // Concluir agendamento
    async concluirAgendamento(id) {
        if (!confirm('Confirmar conclusão do agendamento?')) {
            return;
        }

        try {
            await this.agendamentoService.concluirAgendamento(id);
            alert('Agendamento concluído com sucesso!');
            this.renderizarAgendamentos('agendamentos-container');
        } catch (error) {
            alert('Erro ao concluir agendamento. Tente novamente.');
            console.error('Erro ao concluir agendamento:', error);
        }
    }

    // Ver detalhes do agendamento
    async verDetalhesAgendamento(id) {
        try {
            const agendamento = await this.agendamentoService.buscarAgendamentoPorId(id);
            this.mostrarModalDetalhes(agendamento);
        } catch (error) {
            console.error('Erro ao buscar detalhes do agendamento:', error);
            alert('Erro ao carregar detalhes do agendamento. Tente novamente.');
        }
    }

    // Mostrar modal com detalhes
    mostrarModalDetalhes(agendamento) {
        const dataFormatada = new Date(agendamento.dataHora).toLocaleDateString('pt-BR');
        const horaFormatada = new Date(agendamento.dataHora).toLocaleTimeString('pt-BR', {
            hour: '2-digit',
            minute: '2-digit'
        });

        const modalHTML = `
            <div class="modal-overlay" id="modalOverlay">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>Detalhes do Agendamento</h2>
                        <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">×</button>
                    </div>
                    <div class="modal-body">
                        <div class="agendamento-details">
                            <h3>Informações do Agendamento</h3>
                            <p><strong>ID:</strong> ${agendamento.id}</p>
                            <p><strong>Data e Hora:</strong> ${dataFormatada} às ${horaFormatada}</p>
                            <p><strong>Status:</strong> <span class="status-badge ${this.getStatusClass(agendamento.status)}">${this.getStatusText(agendamento.status)}</span></p>
                            
                            <h4>Serviço</h4>
                            <p><strong>Nome:</strong> ${agendamento.servico.nome}</p>
                            <p><strong>Descrição:</strong> ${agendamento.servico.descricao}</p>
                            <p><strong>Preço:</strong> R$ ${agendamento.servico.preco}</p>
                            <p><strong>Duração:</strong> ${agendamento.servico.duracaoMinutos} minutos</p>
                            
                            <h4>Salão</h4>
                            <p><strong>Nome:</strong> ${agendamento.salao.nome}</p>
                            <p><strong>Endereço:</strong> ${agendamento.salao.endereco}</p>
                            <p><strong>Telefone:</strong> ${agendamento.salao.telefone}</p>
                            
                            <h4>Profissional</h4>
                            <p><strong>Nome:</strong> ${agendamento.profissional.nome}</p>
                            <p><strong>Email:</strong> ${agendamento.profissional.email}</p>
                            
                            <h4>Cliente</h4>
                            <p><strong>Nome:</strong> ${agendamento.cliente.nome}</p>
                            <p><strong>Email:</strong> ${agendamento.cliente.email}</p>
                            <p><strong>Telefone:</strong> ${agendamento.cliente.telefone}</p>
                            
                            ${agendamento.observacoes ? `
                                <h4>Observações</h4>
                                <p>${agendamento.observacoes}</p>
                            ` : ''}
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="botao btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                            Fechar
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', modalHTML);
    }

    // Iniciar processo de agendamento
    async iniciarAgendamento(salaoId) {
        this.agendamentoAtual = { salaoId: salaoId };
        this.mostrarModalAgendamento();
    }

    // Mostrar modal de agendamento
    mostrarModalAgendamento() {
        const modalHTML = `
            <div class="modal-overlay" id="modalOverlay">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>Novo Agendamento</h2>
                        <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">×</button>
                    </div>
                    <div class="modal-body">
                        <form id="agendamentoForm">
                            <div class="form-group">
                                <label for="servicoSelect">Serviço:</label>
                                <select id="servicoSelect" required>
                                    <option value="">Selecione um serviço</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="profissionalSelect">Profissional:</label>
                                <select id="profissionalSelect" required>
                                    <option value="">Selecione um profissional</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="dataInput">Data:</label>
                                <input type="date" id="dataInput" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="horaSelect">Horário:</label>
                                <select id="horaSelect" required>
                                    <option value="">Selecione um horário</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="observacoesInput">Observações:</label>
                                <textarea id="observacoesInput" rows="3" placeholder="Observações opcionais"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button class="botao btn-primary" onclick="agendamentoUI.confirmarAgendamento()">
                            Confirmar Agendamento
                        </button>
                        <button class="botao btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                            Cancelar
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', modalHTML);
        
        // Carregar dados iniciais
        this.carregarDadosAgendamento();
    }

    // Carregar dados para o formulário de agendamento
    async carregarDadosAgendamento() {
        try {
            const salaoId = this.agendamentoAtual.salaoId;
            
            // Carregar serviços
            const servicos = await salaoService.buscarServicosDoSalao(salaoId);
            const servicoSelect = document.getElementById('servicoSelect');
            servicoSelect.innerHTML = '<option value="">Selecione um serviço</option>' +
                servicos.map(servico => `<option value="${servico.id}">${servico.nome} - R$ ${servico.preco}</option>`).join('');
            
            // Carregar profissionais
            const profissionais = await salaoService.buscarProfissionaisDoSalao(salaoId);
            const profissionalSelect = document.getElementById('profissionalSelect');
            profissionalSelect.innerHTML = '<option value="">Selecione um profissional</option>' +
                profissionais.map(prof => `<option value="${prof.id}">${prof.nome}</option>`).join('');
            
            // Configurar data mínima (hoje)
            const dataInput = document.getElementById('dataInput');
            const hoje = new Date().toISOString().split('T')[0];
            dataInput.min = hoje;
            
            // Event listeners para atualizar horários
            servicoSelect.addEventListener('change', () => this.atualizarHorarios());
            profissionalSelect.addEventListener('change', () => this.atualizarHorarios());
            dataInput.addEventListener('change', () => this.atualizarHorarios());
            
        } catch (error) {
            console.error('Erro ao carregar dados do agendamento:', error);
            alert('Erro ao carregar dados. Tente novamente.');
        }
    }

    // Atualizar horários disponíveis
    async atualizarHorarios() {
        const servicoId = document.getElementById('servicoSelect').value;
        const profissionalId = document.getElementById('profissionalSelect').value;
        const data = document.getElementById('dataInput').value;
        
        if (!servicoId || !profissionalId || !data) {
            return;
        }
        
        try {
            const horarios = await this.agendamentoService.buscarHorariosDisponiveis(
                this.agendamentoAtual.salaoId,
                servicoId,
                profissionalId,
                data
            );
            
            const horaSelect = document.getElementById('horaSelect');
            horaSelect.innerHTML = '<option value="">Selecione um horário</option>' +
                horarios.map(hora => `<option value="${hora}">${hora}</option>`).join('');
                
        } catch (error) {
            console.error('Erro ao buscar horários:', error);
            document.getElementById('horaSelect').innerHTML = '<option value="">Erro ao carregar horários</option>';
        }
    }

    // Confirmar agendamento
    async confirmarAgendamento() {
        const form = document.getElementById('agendamentoForm');
        if (!form.checkValidity()) {
            form.reportValidity();
            return;
        }
        
        const servicoId = document.getElementById('servicoSelect').value;
        const profissionalId = document.getElementById('profissionalSelect').value;
        const data = document.getElementById('dataInput').value;
        const hora = document.getElementById('horaSelect').value;
        const observacoes = document.getElementById('observacoesInput').value;
        
        const dataHora = new Date(`${data}T${hora}`);
        
        const agendamentoData = {
            dataHora: dataHora.toISOString(),
            servicoId: parseInt(servicoId),
            profissionalId: parseInt(profissionalId),
            salaoId: this.agendamentoAtual.salaoId,
            observacoes: observacoes
        };
        
        try {
            await this.agendamentoService.criarAgendamento(agendamentoData);
            alert('Agendamento realizado com sucesso!');
            document.querySelector('.modal-overlay').remove();
            
            // Atualizar lista de agendamentos se estiver em uma página de agendamentos
            const container = document.getElementById('agendamentos-container');
            if (container) {
                this.renderizarAgendamentos('agendamentos-container');
            }
            
        } catch (error) {
            alert('Erro ao realizar agendamento. Tente novamente.');
            console.error('Erro ao criar agendamento:', error);
        }
    }
}

// Instâncias globais
const agendamentoService = new AgendamentoService(apiService);
const agendamentoUI = new AgendamentoUI(agendamentoService);

// Exportar para uso global
window.AgendamentoService = AgendamentoService;
window.AgendamentoUI = AgendamentoUI;
window.agendamentoService = agendamentoService;
window.agendamentoUI = agendamentoUI; 