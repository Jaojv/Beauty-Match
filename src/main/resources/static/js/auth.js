// Gerenciamento de Autenticação
class AuthManager {
    constructor() {
        console.log('AuthManager: Inicializando...');
        // Não executar automaticamente, será chamado após DOM estar pronto
    }

    // Inicializar após DOM estar pronto
    init() {
        console.log('AuthManager: Iniciando com delay...');
        // Adicionar delay para garantir que outros scripts não interfiram
        setTimeout(() => {
            console.log('AuthManager: Executando checkAuthStatus...');
        this.checkAuthStatus();
        }, 100);
    }

    // Verificar se o usuário está autenticado
    checkAuthStatus() {
        console.log('AuthManager: Verificando status de autenticação...');
        try {
        const token = localStorage.getItem('authToken');
        const userData = localStorage.getItem('userData');
            
            console.log('AuthManager: Token encontrado:', !!token);
            console.log('AuthManager: UserData encontrado:', !!userData);
        
        if (token && userData) {
            // Verificar se o token não expirou (implementação básica)
            try {
                const user = JSON.parse(userData);
                    console.log('AuthManager: Usuário logado:', user.username);
                this.updateUIForLoggedUser(user);
            } catch (error) {
                    console.error('AuthManager: Erro ao parsear dados do usuário:', error);
                    // Não forçar logout automaticamente, apenas limpar dados corrompidos
                    localStorage.removeItem('userData');
                    this.updateUIForGuest();
            }
        } else {
                console.log('🔧 AuthManager: Usuário não logado');
            this.updateUIForGuest();
            }
        } catch (error) {
            console.error('AuthManager: Erro ao verificar status de autenticação:', error);
            // Em caso de erro, não fazer nada para evitar loops
        }
    }

    // Salvar dados de autenticação
    saveAuthData(token, userData) {
        console.log('AuthManager: Salvando dados de autenticação...');
        try {
        localStorage.setItem('authToken', token);
        localStorage.setItem('userData', JSON.stringify(userData));
            console.log('AuthManager: Dados salvos com sucesso');
        } catch (error) {
            console.error('AuthManager: Erro ao salvar dados de autenticação:', error);
        }
    }

    // Atualizar UI para usuário logado (se existirem)
    updateUIForLoggedUser(user) {
        console.log(`AuthManager: Atualizando UI para usuário logado...`);
        try {
            // Esconder botões de login/cadastro (se existirem)
        const loginLink = document.getElementById('login-link');
        const cadastrarLink = document.getElementById('cadastrar-link');
            if (loginLink) {
                loginLink.style.display = 'none';
                console.log('AuthManager: Botão login escondido');
            }
            if (cadastrarLink) {
                cadastrarLink.style.display = 'none';
                console.log('AuthManager: Botão cadastrar escondido');
            }

            // Mostrar perfil do usuário (se existir)
        const perfilUsuario = document.getElementById('perfil-usuario');
        if (perfilUsuario) {
            perfilUsuario.style.display = 'flex';
                console.log('AuthManager: Perfil do usuário mostrado');
            
            // Preencher dados do usuário
            const usernameDisplay = document.getElementById('username-display');
            const nomePerfil = document.getElementById('nome-perfil');
            const emailPerfil = document.getElementById('email-perfil');
            const senhaPerfil = document.getElementById('senha-perfil');
            
            if (usernameDisplay) {
                usernameDisplay.value = user.username || '';
                    console.log('AuthManager: Username preenchido');
            }
            if (nomePerfil) {
                nomePerfil.value = user.nome || '';
                    console.log('AuthManager: Nome preenchido');
            }
            if (emailPerfil) {
                emailPerfil.value = user.email || '';
                    console.log('AuthManager: Email preenchido');
            }
            if (senhaPerfil) {
                senhaPerfil.value = '********';
                    console.log('AuthManager: Senha preenchida');
                }
            } else {
                console.log('AuthManager: Elemento perfil-usuario não encontrado');
        }

            // Configurar logout (se existir)
            const logoutLink = document.getElementById('logout-link');
            const sairConta = document.querySelector('.sair_conta');
            
            if (logoutLink) {
                // Remover event listener anterior para evitar duplicação
                logoutLink.removeEventListener('click', this.handleLogoutClick);
                
                // Criar função para lidar com o logout
                this.handleLogoutClick = (e) => {
                    e.preventDefault();
                    console.log('AuthManager: Logout solicitado via link');
                    this.logout();
                };
                
                logoutLink.addEventListener('click', this.handleLogoutClick);
                console.log('AuthManager: Logout configurado via link');
            }
            
            // Configurar logout para toda a div sair_conta
            if (sairConta) {
                // Remover event listener anterior para evitar duplicação
                sairConta.removeEventListener('click', this.handleLogoutClick);
                
                // Adicionar event listener para toda a div
                sairConta.addEventListener('click', (e) => {
                    e.preventDefault();
                    console.log('AuthManager: Logout solicitado via div sair_conta');
                    this.logout();
                });
                
                // Adicionar cursor pointer para indicar que é clicável
                sairConta.style.cursor = 'pointer';
                console.log('AuthManager: Logout configurado para toda a div sair_conta');
            }
            
            if (!logoutLink && !sairConta) {
                console.log('AuthManager: Elementos de logout não encontrados');
            }

            // Configurar modal de perfil (se existir)
            this.setupPerfilModal();
        } catch (error) {
            console.error('AuthManager: Erro ao atualizar UI para usuário logado:', error);
        }
    }

    // Atualizar UI para visitante (se existirem)
    updateUIForGuest() {
        console.log('AuthManager: Atualizando UI para visitante...');
        try {
            // Mostrar botões de login/cadastro (se existirem)
        const loginLink = document.getElementById('login-link');
        const cadastrarLink = document.getElementById('cadastrar-link');
            if (loginLink) {
                loginLink.style.display = 'inline-block';
                console.log('AuthManager: Botão login mostrado');
            }
            if (cadastrarLink) {
                cadastrarLink.style.display = 'inline-block';
                console.log('AuthManager: Botão cadastrar mostrado');
            }

            // Esconder perfil do usuário (se existir)
        const perfilUsuario = document.getElementById('perfil-usuario');
        if (perfilUsuario) {
            perfilUsuario.style.display = 'none';
                console.log('AuthManager: Perfil do usuário escondido');
            } else {
                console.log('AuthManager: Elemento perfil-usuario não encontrado');
            }
        } catch (error) {
            console.error('AuthManager: Erro ao atualizar UI para visitante:', error);
        }
    }

    // Configurar modal de perfil
    setupPerfilModal() {
        console.log('AuthManager: Configurando modal de perfil...');
        try {
        const botaoPerfil = document.querySelector('.botao_logado');
        const modalPerfil = document.getElementById('modal-perfil');
        const fecharModal = document.getElementById('fechar-modal');

        if (botaoPerfil && modalPerfil && fecharModal) {
            // Remover event listeners anteriores para evitar duplicação
            botaoPerfil.removeEventListener('click', this.handlePerfilClick);
            
            // Criar função para lidar com o clique no perfil
            this.handlePerfilClick = async (e) => {
                e.preventDefault();
                    console.log('AuthManager: Modal de perfil aberto');
                
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
                
                console.log('AuthManager: Modal de perfil configurado');
            } else {
                console.log('AuthManager: Elementos do modal não encontrados');
            }
        } catch (error) {
            console.error('AuthManager: Erro ao configurar modal de perfil:', error);
        }
    }

    // Logout
    logout() {
        console.log('AuthManager: Executando logout...');
        try {
            localStorage.removeItem('authToken');
            localStorage.removeItem('userData');
            console.log('AuthManager: Dados removidos do localStorage');
            
            // Redirecionar para a página inicial sem causar erro 404
            const currentPath = window.location.pathname;
            if (currentPath.includes('/pages/')) {
                // Se estamos em uma página dentro de /pages/, voltar para index
                window.location.href = '../index.html';
            } else {
                // Se estamos na raiz, recarregar a página atual
                window.location.reload();
            }
        } catch (error) {
            console.error('AuthManager: Erro ao fazer logout:', error);
            // Fallback: recarregar a página
            window.location.reload();
        }
    }

    // Verificar se está autenticado
    isAuthenticated() {
        try {
            const isAuth = localStorage.getItem('authToken') !== null;
            console.log('AuthManager: Verificando autenticação:', isAuth);
            return isAuth;
        } catch (error) {
            console.error('AuthManager: Erro ao verificar autenticação:', error);
            return false;
        }
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
        try {
        const userData = localStorage.getItem('userData');
            const parsed = userData ? JSON.parse(userData) : null;
            console.log('AuthManager: Obtendo dados do usuário:', !!parsed);
            return parsed;
        } catch (error) {
            console.error('AuthManager: Erro ao obter dados do usuário:', error);
            return null;
        }
    }
}

// Instância global do gerenciador de autenticação
document.addEventListener('DOMContentLoaded', function() {
    console.log('🔧 AuthManager: DOM carregado, inicializando...');
    try {
    const authManager = new AuthManager();
    authManager.init();
    window.authManager = authManager; // Tornar global para acesso em outros scripts
        console.log('AuthManager: Inicializado com sucesso');
    } catch (error) {
        console.error('AuthManager: Erro ao inicializar:', error);
    }
}); 