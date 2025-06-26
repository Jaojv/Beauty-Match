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
        window.location.href = '/index.html';
    }
}

// Instância global do cliente API
const apiClient = new ApiClient(); 