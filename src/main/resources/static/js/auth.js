// Gerenciamento de Autenticação
class AuthManager {
    constructor() {
        // Não executar automaticamente, será chamado após DOM estar pronto
    }

    // Inicializar após DOM estar pronto
    init() {
        this.checkAuthStatus();
    }

    // Verificar se o usuário está autenticado
    checkAuthStatus() {
        const token = localStorage.getItem('authToken');
        const userData = localStorage.getItem('userData');
        
        if (token && userData) {
            // Verificar se o token não expirou (implementação básica)
            try {
                const user = JSON.parse(userData);
                this.updateUIForLoggedUser(user);
            } catch (error) {
                console.error('Erro ao parsear dados do usuário:', error);
                this.logout();
            }
        } else {
            this.updateUIForGuest();
        }
    }

    // Salvar dados de autenticação
    saveAuthData(token, userData) {
        localStorage.setItem('authToken', token);
        localStorage.setItem('userData', JSON.stringify(userData));
    }

    // Atualizar UI para usuário logado
    updateUIForLoggedUser(user) {
        // Esconder botões de login/cadastro
        const loginLink = document.getElementById('login-link');
        const cadastrarLink = document.getElementById('cadastrar-link');
        if (loginLink) loginLink.style.display = 'none';
        if (cadastrarLink) cadastrarLink.style.display = 'none';

        // Mostrar perfil do usuário
        const perfilUsuario = document.getElementById('perfil-usuario');
        if (perfilUsuario) {
            perfilUsuario.style.display = 'flex';
            
            // Preencher dados do usuário
            const usernameDisplay = document.getElementById('username-display');
            const nomePerfil = document.getElementById('nome-perfil');
            const emailPerfil = document.getElementById('email-perfil');
            const senhaPerfil = document.getElementById('senha-perfil');
            
            if (usernameDisplay) {
                usernameDisplay.value = user.username || '';
            }
            if (nomePerfil) {
                nomePerfil.value = user.nome || '';
            }
            if (emailPerfil) {
                emailPerfil.value = user.email || '';
            }
            if (senhaPerfil) {
                senhaPerfil.value = '********';
            }
        }

        // Configurar logout
        const logoutLink = document.getElementById('logout-link');
        if (logoutLink) {
            logoutLink.addEventListener('click', (e) => {
                e.preventDefault();
                this.logout();
            });
        }

        // Configurar modal de perfil
        this.setupPerfilModal();
    }

    // Atualizar UI para visitante
    updateUIForGuest() {
        // Mostrar botões de login/cadastro
        const loginLink = document.getElementById('login-link');
        const cadastrarLink = document.getElementById('cadastrar-link');
        if (loginLink) loginLink.style.display = 'inline-block';
        if (cadastrarLink) cadastrarLink.style.display = 'inline-block';

        // Esconder perfil do usuário
        const perfilUsuario = document.getElementById('perfil-usuario');
        if (perfilUsuario) {
            perfilUsuario.style.display = 'none';
        }
    }

    // Configurar modal de perfil
    setupPerfilModal() {
        const botaoPerfil = document.querySelector('.botao_logado');
        const modalPerfil = document.getElementById('modal-perfil');
        const fecharModal = document.getElementById('fechar-modal');

        if (botaoPerfil && modalPerfil && fecharModal) {
            // Remover event listeners anteriores para evitar duplicação
            botaoPerfil.removeEventListener('click', this.handlePerfilClick);
            
            // Criar função para lidar com o clique no perfil
            this.handlePerfilClick = async (e) => {
                e.preventDefault();
                
                // Buscar dados do usuário do localStorage
                const userData = this.getUserData();
                
                if (userData) {
                    // Preencher todos os campos do modal com dados do localStorage
                    const nomePerfil = document.getElementById('nome-perfil');
                    const emailPerfil = document.getElementById('email-perfil');
                    const senhaPerfil = document.getElementById('senha-perfil');
                    
                    if (nomePerfil) nomePerfil.value = userData.nome || '';
                    if (emailPerfil) emailPerfil.value = userData.email || '';
                    if (senhaPerfil) senhaPerfil.value = '********';
                }
                
                modalPerfil.style.display = 'flex';
            };

            // Adicionar event listener para abrir modal ao clicar no perfil
            botaoPerfil.addEventListener('click', this.handlePerfilClick);

            // Fechar modal ao clicar no X
            fecharModal.addEventListener('click', function() {
                modalPerfil.style.display = 'none';
            });

            // Fechar modal ao clicar fora do conteúdo
            window.addEventListener('click', function(e) {
                if (e.target === modalPerfil) {
                    modalPerfil.style.display = 'none';
                }
            });
        }
    }

    // Logout
    logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userData');
        window.location.href = '/index.html';
    }

    // Verificar se está autenticado
    isAuthenticated() {
        return localStorage.getItem('authToken') !== null;
    }

    // Verificar se está autenticado e redirecionar se não estiver
    requireAuth(redirectTo = '/pages/login.html') {
        if (!this.isAuthenticated()) {
            window.location.href = redirectTo;
            return false;
        }
        return true;
    }

    // Verificar se NÃO está autenticado e redirecionar se estiver
    requireGuest(redirectTo = '/index.html') {
        if (this.isAuthenticated()) {
            window.location.href = redirectTo;
            return false;
        }
        return true;
    }

    // Obter dados do usuário
    getUserData() {
        const userData = localStorage.getItem('userData');
        return userData ? JSON.parse(userData) : null;
    }
}

// Instância global do gerenciador de autenticação
document.addEventListener('DOMContentLoaded', function() {
    const authManager = new AuthManager();
    authManager.init();
    window.authManager = authManager; // Tornar global para acesso em outros scripts
}); 