// API Client para requisições HTTP
class ApiClient {
    constructor() {
        this.baseURL = 'http://localhost:8080/api';
    }

    // Função genérica para requisições HTTP
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        
        // Configurações padrão
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        // Adicionar token de autenticação se existir
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(url, config);
            
            // Verificar se a resposta é ok
            if (!response.ok) {
                const errorData = await response.text();
                throw new Error(errorData || `Erro ${response.status}: ${response.statusText}`);
            }

            // Tentar parsear JSON, se não conseguir retornar texto
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            } else {
                return await response.text();
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    }

    // Métodos específicos para autenticação
    async login(username, password) {
        return this.request('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        });
    }

    async register(username, password, nome, email) {
        return this.request('/auth/registro', {
            method: 'POST',
            body: JSON.stringify({
                username,
                password,
                nome,
                email,
                tipoUsuario: 'CLIENTE' // Fixo para cliente
            })
        });
    }

    // Método para buscar dados do usuário logado
    async getUserProfile() {
        return this.request('/auth/profile', {
            method: 'GET'
        });
    }

    // Método para logout (limpar localStorage)
    logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userData');
        
        // Redirecionar para a página inicial sem causar erro 404
        const currentPath = window.location.pathname;
        if (currentPath.includes('/pages/')) {
            // Se estamos em uma página dentro de /pages/, voltar para index
            window.location.href = '../index.html';
        } else {
            // Se estamos na raiz, recarregar a página atual
            window.location.reload();
        }
    }

    // ============================================================================
    // MÉTODOS ESPECÍFICOS DO QUIZ
    // ============================================================================

    /**
     * Busca todas as perguntas ativas do quiz
     */
    async getPerguntasQuiz() {
        return this.request('/quiz/perguntas', {
            method: 'GET'
        });
    }

    /**
     * Verifica o status do quiz para um cliente específico
     */
    async getStatusQuiz(clienteId) {
        return this.request(`/quiz/cliente/${clienteId}/status`, {
            method: 'GET'
        });
    }

    /**
     * Busca a resposta do quiz de um cliente específico
     */
    async getRespostaQuiz(clienteId) {
        return this.request(`/quiz/cliente/${clienteId}/resposta`, {
            method: 'GET'
        });
    }

    /**
     * Envia as respostas do quiz e recebe a recomendação
     */
    async enviarRespostasQuiz(respostaQuizDTO) {
        return this.request('/quiz/responder', {
            method: 'POST',
            body: JSON.stringify(respostaQuizDTO)
        });
    }

    /**
     * Busca uma recomendação específica por critério
     */
    async getRecomendacaoPorCriterio(criterio) {
        return this.request(`/quiz/recomendacoes/${criterio}`, {
            method: 'GET'
        });
    }

    /**
     * Limpa as respostas do quiz de um cliente (permite refazer)
     */
    async limparRespostasQuiz(clienteId) {
        return this.request(`/quiz/cliente/${clienteId}/respostas`, {
            method: 'DELETE'
        });
    }

    // =========================================================================
    // MÉTODOS DE SALÃO
    // =========================================================================

    /**
     * Busca todos os salões cadastrados
     */
    async getSaloes() {
        return this.request('/saloes', {
            method: 'GET'
        });
    }

    /**
     * Busca um salão específico por ID
     */
    async getSalao(id) {
        return this.request(`/saloes/${id}`, {
            method: 'GET'
        });
    }

    /**
     * Busca serviços de um salão específico
     */
    async getServicosSalao(salaoId) {
        return this.request(`/servicos/salao/${salaoId}`, {
            method: 'GET'
        });
    }

    /**
     * Busca um serviço específico por ID
     */
    async getServico(id) {
        return this.request(`/servicos/${id}`, {
            method: 'GET'
        });
    }

    /**
     * Busca profissionais de um salão específico
     */
    async getProfissionaisSalao(salaoId) {
        return this.request(`/profissionais/salao/${salaoId}`, {
            method: 'GET'
        });
    }

    /**
     * Busca horários disponíveis para agendamento
     */
    async getHorariosDisponiveis(salaoId, profissionalId, data) {
        const params = new URLSearchParams({
            salaoId: salaoId,
            profissionalId: profissionalId,
            data: data
        });
        
        return this.request(`/agendamentos/horarios-disponiveis?${params}`, {
            method: 'GET'
        });
    }

    /**
     * Cria um novo agendamento
     */
    async criarAgendamento(dadosAgendamento) {
        return this.request('/agendamentos', {
            method: 'POST',
            body: JSON.stringify(dadosAgendamento)
        });
    }

    /**
     * Busca agendamentos do cliente logado
     */
    async getAgendamentosCliente() {
        return this.request('/agendamentos/cliente', {
            method: 'GET'
        });
    }

    /**
     * Busca agendamentos ativos do cliente logado
     */
    async getAgendamentosAtivosCliente() {
        return this.request('/agendamentos/cliente/ativos', {
            method: 'GET'
        });
    }

    /**
     * Busca histórico de agendamentos do cliente logado
     */
    async getHistoricoAgendamentosCliente(dataInicio = null, dataFim = null) {
        const params = new URLSearchParams();
        if (dataInicio) params.append('dataInicio', dataInicio);
        if (dataFim) params.append('dataFim', dataFim);
        
        return this.request(`/agendamentos/cliente/historico?${params}`, {
            method: 'GET'
        });
    }

    /**
     * Cancela um agendamento específico
     */
    async cancelarAgendamento(agendamentoId) {
        return this.request(`/agendamentos/${agendamentoId}/cancelar`, {
            method: 'PUT'
        });
    }

    /**
     * Busca um agendamento específico por ID
     */
    async getAgendamento(id) {
        return this.request(`/agendamentos/${id}`, {
            method: 'GET'
        });
    }

    // =========================================================================
    // MÉTODOS DE FAVORITOS
    // =========================================================================

    /**
     * Lista todos os favoritos do usuário logado
     */
    async getFavoritos() {
        return this.request('/favoritos', {
            method: 'GET'
        });
    }

    /**
     * Adiciona um salão aos favoritos
     */
    async adicionarFavorito(salaoId) {
        return this.request('/favoritos', {
            method: 'POST',
            body: JSON.stringify({ salaoId: salaoId })
        });
    }

    /**
     * Remove um salão dos favoritos
     */
    async removerFavorito(salaoId) {
        return this.request(`/favoritos/${salaoId}`, {
            method: 'DELETE'
        });
    }

    /**
     * Verifica se um salão está favoritado
     */
    async verificarFavorito(salaoId) {
        return this.request(`/favoritos/verificar/${salaoId}`, {
            method: 'GET'
        });
    }

    /**
     * Conta quantos favoritos o usuário tem
     */
    async contarFavoritos() {
        return this.request('/favoritos/contar', {
            method: 'GET'
        });
    }
}

// Instância global do cliente API
const apiClient = new ApiClient(); 