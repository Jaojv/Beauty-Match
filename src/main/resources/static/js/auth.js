// Configuração da API
const API_BASE_URL = 'http://localhost:8080/api';

// Classe para gerenciar autenticação
class AuthService {
    constructor() {
        this.token = localStorage.getItem('authToken');
        this.user = JSON.parse(localStorage.getItem('user'));
    }

    // Salvar dados de autenticação
    setAuth(token, user) {
        this.token = token;
        this.user = user;
        localStorage.setItem('authToken', token);
        localStorage.setItem('user', JSON.stringify(user));
    }

    // Limpar dados de autenticação
    clearAuth() {
        this.token = null;
        this.user = null;
        localStorage.removeItem('authToken');
        localStorage.removeItem('user');
    }

    // Verificar se está autenticado
    isAuthenticated() {
        return this.token !== null;
    }

    // Obter token
    getToken() {
        return this.token;
    }

    // Obter usuário atual
    getCurrentUser() {
        return this.user;
    }

    // Fazer login
    async login(username, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                throw new Error('Credenciais inválidas');
            }

            const data = await response.json();
            this.setAuth(data.token, {
                id: data.id,
                username: data.username,
                tipoUsuario: data.tipoUsuario
            });

            return data;
        } catch (error) {
            console.error('Erro no login:', error);
            throw error;
        }
    }

    // Fazer logout
    logout() {
        this.clearAuth();
        window.location.href = '/index.html';
    }

    // Registrar novo usuário
    async register(registroData) {
        try {
            const response = await fetch(`${API_BASE_URL}/auth/registro`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(registroData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Erro no registro');
            }

            const data = await response.json();
            this.setAuth(data.token, {
                id: data.id,
                username: data.username,
                tipoUsuario: data.tipoUsuario
            });

            return data;
        } catch (error) {
            console.error('Erro no registro:', error);
            throw error;
        }
    }
}

// Classe para requisições HTTP autenticadas
class ApiService {
    constructor(authService) {
        this.authService = authService;
    }

    // Fazer requisição autenticada
    async request(url, options = {}) {
        const token = this.authService.getToken();
        
        const defaultHeaders = {
            'Content-Type': 'application/json',
        };

        if (token) {
            defaultHeaders['Authorization'] = `Bearer ${token}`;
        }

        const config = {
            ...options,
            headers: {
                ...defaultHeaders,
                ...options.headers
            }
        };

        try {
            const response = await fetch(`${API_BASE_URL}${url}`, config);
            
            if (response.status === 401) {
                // Token expirado ou inválido
                this.authService.clearAuth();
                window.location.href = '/pages/login.html';
                return;
            }

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return await response.json();
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    }

    // GET
    async get(url) {
        return this.request(url, { method: 'GET' });
    }

    // POST
    async post(url, data) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    // PUT
    async put(url, data) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    // DELETE
    async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
}

// Instâncias globais
const authService = new AuthService();
const apiService = new ApiService(authService);

// Função para verificar autenticação em páginas protegidas
function checkAuth() {
    if (!authService.isAuthenticated()) {
        window.location.href = '/pages/login.html';
        return false;
    }
    return true;
}

// Função para redirecionar baseado no tipo de usuário
function redirectBasedOnUserType() {
    const user = authService.getCurrentUser();
    if (!user) return;

    switch (user.tipoUsuario.toLowerCase()) {
        case 'cliente':
            window.location.href = '/index_logado.html';
            break;
        case 'profissional':
            window.location.href = '/pages/profissional-dashboard.html';
            break;
        case 'proprietario':
            window.location.href = '/pages/proprietario-dashboard.html';
            break;
        case 'admin':
            window.location.href = '/pages/admin-dashboard.html';
            break;
        default:
            window.location.href = '/index_logado.html';
    }
}

// Exportar para uso global
window.AuthService = AuthService;
window.ApiService = ApiService;
window.authService = authService;
window.apiService = apiService;
window.checkAuth = checkAuth;
window.redirectBasedOnUserType = redirectBasedOnUserType; 