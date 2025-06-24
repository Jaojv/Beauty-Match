// Classe para gerenciar operações de salões
class SalaoService {
    constructor(apiService) {
        this.apiService = apiService;
    }

    // Buscar todos os salões
    async buscarTodosSaloes() {
        try {
            return await this.apiService.get('/saloes');
        } catch (error) {
            console.error('Erro ao buscar salões:', error);
            throw error;
        }
    }

    // Buscar salão por ID
    async buscarSalaoPorId(id) {
        try {
            return await this.apiService.get(`/saloes/${id}`);
        } catch (error) {
            console.error('Erro ao buscar salão:', error);
            throw error;
        }
    }

    // Buscar salões por proprietário
    async buscarSaloesPorProprietario(proprietarioId) {
        try {
            return await this.apiService.get(`/saloes/proprietario/${proprietarioId}`);
        } catch (error) {
            console.error('Erro ao buscar salões do proprietário:', error);
            throw error;
        }
    }

    // Criar novo salão
    async criarSalao(salaoData) {
        try {
            return await this.apiService.post('/saloes', salaoData);
        } catch (error) {
            console.error('Erro ao criar salão:', error);
            throw error;
        }
    }

    // Atualizar salão
    async atualizarSalao(id, salaoData) {
        try {
            return await this.apiService.put(`/saloes/${id}`, salaoData);
        } catch (error) {
            console.error('Erro ao atualizar salão:', error);
            throw error;
        }
    }

    // Deletar salão
    async deletarSalao(id) {
        try {
            return await this.apiService.delete(`/saloes/${id}`);
        } catch (error) {
            console.error('Erro ao deletar salão:', error);
            throw error;
        }
    }

    // Buscar serviços de um salão
    async buscarServicosDoSalao(salaoId) {
        try {
            return await this.apiService.get(`/saloes/${salaoId}/servicos`);
        } catch (error) {
            console.error('Erro ao buscar serviços do salão:', error);
            throw error;
        }
    }

    // Buscar profissionais de um salão
    async buscarProfissionaisDoSalao(salaoId) {
        try {
            return await this.apiService.get(`/saloes/${salaoId}/profissionais`);
        } catch (error) {
            console.error('Erro ao buscar profissionais do salão:', error);
            throw error;
        }
    }
}

// Funções utilitárias para UI
class SalaoUI {
    constructor(salaoService) {
        this.salaoService = salaoService;
    }

    // Renderizar lista de salões
    async renderizarSaloes(containerId) {
        const container = document.getElementById(containerId);
        if (!container) return;

        try {
            container.innerHTML = '<div class="loading">Carregando salões...</div>';
            
            const saloes = await this.salaoService.buscarTodosSaloes();
            
            if (saloes.length === 0) {
                container.innerHTML = '<div class="no-data">Nenhum salão encontrado.</div>';
                return;
            }

            const saloesHTML = saloes.map(salao => this.criarCardSalao(salao)).join('');
            container.innerHTML = saloesHTML;

        } catch (error) {
            container.innerHTML = '<div class="error">Erro ao carregar salões. Tente novamente.</div>';
            console.error('Erro ao renderizar salões:', error);
        }
    }

    // Criar card de salão
    criarCardSalao(salao) {
        return `
            <div class="card salao-card" data-salao-id="${salao.id}">
                <div class="card-header">
                    <h3>${salao.nome}</h3>
                    <div class="salao-rating">
                        <span class="stars">★★★★☆</span>
                        <span class="rating-text">4.0</span>
                    </div>
                </div>
                <div class="card-body">
                    <p class="salao-description">${salao.descricao || 'Descrição não disponível'}</p>
                    <div class="salao-info">
                        <div class="info-item">
                            <img src="../images/localizacao.png" alt="Localização" class="info-icon">
                            <span>${salao.endereco}</span>
                        </div>
                        <div class="info-item">
                            <img src="../images/telefone.png" alt="Telefone" class="info-icon">
                            <span>${salao.telefone}</span>
                        </div>
                        <div class="info-item">
                            <img src="../images/e-mail.png" alt="Email" class="info-icon">
                            <span>${salao.email}</span>
                        </div>
                    </div>
                    <div class="salao-horario">
                        <strong>Horário:</strong> ${salao.horarioFuncionamento}
                    </div>
                </div>
                <div class="card-footer">
                    <button class="botao btn-primary" onclick="salaoUI.verDetalhesSalao(${salao.id})">
                        Ver Detalhes
                    </button>
                    <button class="botao btn-secondary" onclick="salaoUI.favoritarSalao(${salao.id})">
                        ❤️ Favoritar
                    </button>
                </div>
            </div>
        `;
    }

    // Ver detalhes do salão
    async verDetalhesSalao(salaoId) {
        try {
            const salao = await this.salaoService.buscarSalaoPorId(salaoId);
            const servicos = await this.salaoService.buscarServicosDoSalao(salaoId);
            const profissionais = await this.salaoService.buscarProfissionaisDoSalao(salaoId);
            
            this.mostrarModalDetalhes(salao, servicos, profissionais);
        } catch (error) {
            console.error('Erro ao buscar detalhes do salão:', error);
            alert('Erro ao carregar detalhes do salão. Tente novamente.');
        }
    }

    // Mostrar modal com detalhes
    mostrarModalDetalhes(salao, servicos, profissionais) {
        const modalHTML = `
            <div class="modal-overlay" id="modalOverlay">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>${salao.nome}</h2>
                        <button class="modal-close" onclick="this.closest('.modal-overlay').remove()">×</button>
                    </div>
                    <div class="modal-body">
                        <div class="salao-details">
                            <h3>Informações</h3>
                            <p><strong>Endereço:</strong> ${salao.endereco}</p>
                            <p><strong>Telefone:</strong> ${salao.telefone}</p>
                            <p><strong>Email:</strong> ${salao.email}</p>
                            <p><strong>Horário:</strong> ${salao.horarioFuncionamento}</p>
                            <p><strong>Descrição:</strong> ${salao.descricao}</p>
                        </div>
                        
                        <div class="servicos-section">
                            <h3>Serviços Disponíveis</h3>
                            <div class="servicos-grid">
                                ${servicos.map(servico => `
                                    <div class="servico-item">
                                        <h4>${servico.nome}</h4>
                                        <p>${servico.descricao}</p>
                                        <p class="servico-preco">R$ ${servico.preco}</p>
                                        <p class="servico-duracao">${servico.duracaoMinutos} min</p>
                                    </div>
                                `).join('')}
                            </div>
                        </div>
                        
                        <div class="profissionais-section">
                            <h3>Profissionais</h3>
                            <div class="profissionais-grid">
                                ${profissionais.map(prof => `
                                    <div class="profissional-item">
                                        <img src="${prof.fotoPerfil || '../images/user.png'}" alt="Foto" class="profissional-foto">
                                        <h4>${prof.nome}</h4>
                                        <p>${prof.especialidades || 'Especialidades não informadas'}</p>
                                    </div>
                                `).join('')}
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="botao btn-primary" onclick="agendamentoUI.iniciarAgendamento(${salao.id})">
                            Agendar Serviço
                        </button>
                        <button class="botao btn-secondary" onclick="this.closest('.modal-overlay').remove()">
                            Fechar
                        </button>
                    </div>
                </div>
            </div>
        `;
        
        document.body.insertAdjacentHTML('beforeend', modalHTML);
    }

    // Favoritar salão
    async favoritarSalao(salaoId) {
        try {
            // Implementar lógica de favoritar
            console.log('Favoritando salão:', salaoId);
            alert('Salão adicionado aos favoritos!');
        } catch (error) {
            console.error('Erro ao favoritar salão:', error);
            alert('Erro ao favoritar salão. Tente novamente.');
        }
    }

    // Buscar salões (funcionalidade de busca)
    async buscarSaloes(termo) {
        try {
            const saloes = await this.salaoService.buscarTodosSaloes();
            const saloesFiltrados = saloes.filter(salao => 
                salao.nome.toLowerCase().includes(termo.toLowerCase()) ||
                salao.endereco.toLowerCase().includes(termo.toLowerCase())
            );
            return saloesFiltrados;
        } catch (error) {
            console.error('Erro na busca de salões:', error);
            throw error;
        }
    }
}

// Instâncias globais
const salaoService = new SalaoService(apiService);
const salaoUI = new SalaoUI(salaoService);

// Exportar para uso global
window.SalaoService = SalaoService;
window.SalaoUI = SalaoUI;
window.salaoService = salaoService;
window.salaoUI = salaoUI; 