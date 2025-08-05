// ===== JAVASCRIPT DO PAINEL ADMINISTRATIVO =====

// Verificar se o ApiClient está disponível
if (typeof apiClient === 'undefined') {
    console.error('AdminPanel: ApiClient não encontrado!');
}

class AdminPanel {
    constructor() {
        this.currentPage = 'dashboard';
        this.userData = null;
        this.init();
    }

    // Inicializar o painel administrativo
    init() {
        console.log('AdminPanel: Inicializando...');
        this.checkAdminAuth();
        this.setupSidebar();
        this.setupEventListeners();
        this.loadUserData();
    }

    // Verificar se o usuário é admin
    checkAdminAuth() {
        const userData = this.getUserData();
        if (!userData || userData.tipoUsuario !== 'ADMIN') {
            console.log('AdminPanel: Usuário não é admin, redirecionando...');
            window.location.href = 'login.html';
            return;
        }
        this.userData = userData;
        console.log('AdminPanel: Usuário admin autenticado:', userData.username);
    }

    // Obter dados do usuário do localStorage
    getUserData() {
        try {
            const userData = localStorage.getItem('userData');
            return userData ? JSON.parse(userData) : null;
        } catch (error) {
            console.error('AdminPanel: Erro ao obter dados do usuário:', error);
            return null;
        }
    }

    // Obter token de autenticação
    getToken() {
        return localStorage.getItem('authToken');
    }

    // Configurar sidebar responsiva
    setupSidebar() {
        const sidebarToggle = document.querySelector('.sidebar-toggle');
        const sidebar = document.querySelector('.admin-sidebar');
        
        if (sidebarToggle && sidebar) {
            sidebarToggle.addEventListener('click', () => {
                sidebar.classList.toggle('active');
            });

            // Fechar sidebar ao clicar fora (mobile)
            document.addEventListener('click', (e) => {
                if (window.innerWidth <= 768) {
                    if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                        sidebar.classList.remove('active');
                    }
                }
            });
        }
    }

    // Configurar event listeners
    setupEventListeners() {
        // Navegação da sidebar
        const navItems = document.querySelectorAll('.admin-nav-item');
        navItems.forEach(item => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                const page = item.getAttribute('data-page');
                if (page) {
                    this.navigateToPage(page);
                }
            });
        });

        // Logout
        const logoutBtn = document.querySelector('.admin-logout');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.logout();
            });
        }

        // Fechar modais
        const modalCloses = document.querySelectorAll('.admin-modal-close');
        modalCloses.forEach(close => {
            close.addEventListener('click', () => {
                this.closeAllModals();
            });
        });

        // Fechar modal ao clicar fora
        window.addEventListener('click', (e) => {
            const modals = document.querySelectorAll('.admin-modal');
            modals.forEach(modal => {
                if (e.target === modal) {
                    modal.style.display = 'none';
                }
            });
        });
    }

    // Navegar para uma página específica
    navigateToPage(page) {
        console.log('AdminPanel: Navegando para:', page);
        
        // Atualizar página atual
        this.currentPage = page;
        
        // Atualizar navegação ativa
        this.updateActiveNavigation(page);
        
        // Carregar conteúdo da página
        this.loadPageContent(page);
    }

    // Atualizar navegação ativa
    updateActiveNavigation(page) {
        const navItems = document.querySelectorAll('.admin-nav-item');
        navItems.forEach(item => {
            item.classList.remove('active');
            if (item.getAttribute('data-page') === page) {
                item.classList.add('active');
            }
        });
    }

    // Carregar conteúdo da página
    loadPageContent(page) {
        const contentArea = document.querySelector('.admin-content');
        if (!contentArea) return;

        // Mostrar loading
        contentArea.innerHTML = '<div class="admin-loading">Carregando...</div>';

        switch (page) {
            case 'dashboard':
                this.loadDashboard();
                break;
            case 'usuarios':
                this.loadUsuarios();
                break;
            case 'saloes':
                this.loadSaloes();
                break;
            default:
                this.loadDashboard();
        }
    }

    // Carregar dashboard
    async loadDashboard() {
        try {
            const contentArea = document.querySelector('.admin-content');
            
            // Buscar estatísticas
            const stats = await this.getDashboardStats();
            
            contentArea.innerHTML = `
                <div class="dashboard-grid">
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Total de Usuários</div>
                            <div class="dashboard-card-icon" style="background-color: #CB5A6E;">
                                👥
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalUsuarios}</div>
                        <div class="dashboard-card-description">Usuários cadastrados no sistema</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Total de Salões</div>
                            <div class="dashboard-card-icon" style="background-color: #28a745;">
                                🏪
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalSaloes}</div>
                        <div class="dashboard-card-description">Salões cadastrados no sistema</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Clientes</div>
                            <div class="dashboard-card-icon" style="background-color: #17a2b8;">
                                👤
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalClientes}</div>
                        <div class="dashboard-card-description">Clientes cadastrados</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Profissionais</div>
                            <div class="dashboard-card-icon" style="background-color: #ffc107;">
                                👩‍💼
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalProfissionais}</div>
                        <div class="dashboard-card-description">Profissionais cadastrados</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Proprietários</div>
                            <div class="dashboard-card-icon" style="background-color: #6f42c1;">
                                👑
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalProprietarios}</div>
                        <div class="dashboard-card-description">Proprietários cadastrados</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Administradores</div>
                            <div class="dashboard-card-icon" style="background-color: #dc3545;">
                                👨‍💼
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.totalAdmins}</div>
                        <div class="dashboard-card-description">Administradores do sistema</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Salões Pendentes</div>
                            <div class="dashboard-card-icon" style="background-color: #ffc107;">
                                ⏳
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.saloesPendentes}</div>
                        <div class="dashboard-card-description">Aguardando aprovação</div>
                    </div>
                    
                    <div class="dashboard-card">
                        <div class="dashboard-card-header">
                            <div class="dashboard-card-title">Salões Aprovados</div>
                            <div class="dashboard-card-icon" style="background-color: #28a745;">
                                ✅
                            </div>
                        </div>
                        <div class="dashboard-card-number">${stats.saloesAprovados}</div>
                        <div class="dashboard-card-description">Salões ativos</div>
                    </div>
                </div>
            `;
        } catch (error) {
            console.error('AdminPanel: Erro ao carregar dashboard:', error);
            this.showError('Erro ao carregar estatísticas do dashboard');
        }
    }

    // Buscar estatísticas do dashboard
    async getDashboardStats() {
        try {
            const response = await apiClient.request('/admin/dashboard/stats', {
                method: 'GET'
            });
            return response;
        } catch (error) {
            console.error('AdminPanel: Erro ao buscar estatísticas:', error);
            // Fallback para dados mockados em caso de erro
            return {
                totalUsuarios: 0,
                totalSaloes: 0,
                totalClientes: 0,
                totalProfissionais: 0,
                totalProprietarios: 0,
                totalAdmins: 0,
                saloesPendentes: 0,
                saloesAprovados: 0,
                saloesRejeitados: 0
            };
        }
    }

    // Carregar página de usuários
    async loadUsuarios() {
        try {
            const contentArea = document.querySelector('.admin-content');
            
            contentArea.innerHTML = `
                <div class="admin-table-container">
                    <div class="admin-table-header">
                        <h2 class="admin-table-title">Gestão de Usuários</h2>
                        <button class="admin-btn admin-btn-primary" onclick="adminPanel.showCreateUserModal()">
                            Novo Usuário
                        </button>
                    </div>
                    
                    <div class="admin-loading">Carregando usuários...</div>
                </div>
            `;
            
            // TODO: Implementar carregamento real de usuários
            this.loadUsersList();
        } catch (error) {
            console.error('AdminPanel: Erro ao carregar usuários:', error);
            this.showError('Erro ao carregar lista de usuários');
        }
    }

    // Carregar página de salões
    async loadSaloes() {
        try {
            const contentArea = document.querySelector('.admin-content');
            
            contentArea.innerHTML = `
                <div class="admin-table-container">
                    <div class="admin-table-header">
                        <h2 class="admin-table-title">Gestão de Salões</h2>
                        <div class="d-flex gap-10">
                            <select class="admin-form-select" id="statusFilter">
                                <option value="">Todos os Status</option>
                                <option value="PENDENTE">Pendentes</option>
                                <option value="APROVADO">Aprovados</option>
                                <option value="REJEITADO">Rejeitados</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="admin-loading">Carregando salões...</div>
                </div>
            `;
            
            // TODO: Implementar carregamento real de salões
            this.loadSaloesList();
        } catch (error) {
            console.error('AdminPanel: Erro ao carregar salões:', error);
            this.showError('Erro ao carregar lista de salões');
        }
    }

    // Carregar lista de usuários (real)
    async loadUsersList() {
        try {
            const response = await apiClient.request('/admin/usuarios', {
                method: 'GET'
            });
            
            const contentArea = document.querySelector('.admin-content .admin-loading');
            if (!contentArea) return;

            contentArea.parentElement.innerHTML = `
                <div class="admin-table-container">
                    <div class="admin-table-header">
                        <h2 class="admin-table-title">Gestão de Usuários</h2>
                        <button class="admin-btn admin-btn-primary" onclick="adminPanel.showCreateUserModal()">
                            Novo Usuário
                        </button>
                    </div>
                    
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nome</th>
                                <th>Username</th>
                                <th>Email</th>
                                <th>Tipo</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${response.map(user => `
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.nome}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>${user.tipoUsuario}</td>
                                    <td><span class="status-badge status-approved">${user.status}</span></td>
                                    <td>
                                        <button class="admin-btn admin-btn-secondary" onclick="adminPanel.editUser(${user.id})">
                                            Editar
                                        </button>
                                        <button class="admin-btn admin-btn-danger" onclick="adminPanel.deleteUser(${user.id}, '${user.tipoUsuario}')">
                                            Deletar
                                        </button>
                                    </td>
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            `;
        } catch (error) {
            console.error('AdminPanel: Erro ao carregar usuários:', error);
            this.showError('Erro ao carregar lista de usuários');
        }
    }

    // Carregar lista de salões (mock)
    async loadSaloesList() {
        try {
            const contentArea = document.querySelector('.admin-content');
            contentArea.innerHTML = '<div class="admin-loading">Carregando salões...</div>';
            
            // Buscar salões da API usando ApiClient
            const saloes = await apiClient.request('/admin/saloes', {
                method: 'GET'
            });
            
            console.log('AdminPanel: Salões carregados:', saloes);
            
            // Renderizar tabela de salões
            contentArea.innerHTML = `
                <div class="admin-page-header">
                    <h2>Gestão de Salões</h2>
                    <p>Gerencie todos os salões cadastrados no sistema</p>
                </div>
                
                <div class="admin-filters">
                    <div class="filter-group">
                        <label for="statusFilter">Status:</label>
                        <select id="statusFilter" onchange="adminPanel.filterSaloes()">
                            <option value="">Todos</option>
                            <option value="PENDENTE">Pendentes</option>
                            <option value="APROVADO">Aprovados</option>
                            <option value="REJEITADO">Rejeitados</option>
                        </select>
                    </div>
                    <div class="filter-group">
                        <label for="searchSalao">Buscar:</label>
                        <input type="text" id="searchSalao" placeholder="Nome do salão..." onkeyup="adminPanel.filterSaloes()">
                    </div>
                </div>
                
                <div class="admin-table-container">
                    <table class="admin-table" id="saloesTable">
                        <thead>
                            <tr>
                                <th>Nome</th>
                                <th>Proprietário</th>
                                <th>Email</th>
                                <th>Telefone</th>
                                <th>Endereço</th>
                                <th>Status</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${this.renderSaloesTable(saloes)}
                        </tbody>
                    </table>
                </div>
            `;
            
        } catch (error) {
            console.error('AdminPanel: Erro ao carregar salões:', error);
            const contentArea = document.querySelector('.admin-content');
            contentArea.innerHTML = `
                <div class="admin-error">
                    <h3>Erro ao carregar salões</h3>
                    <p>${error.message}</p>
                    <button class="admin-btn admin-btn-primary" onclick="adminPanel.loadSaloesList()">Tentar novamente</button>
                </div>
            `;
        }
    }

    // Carregar dados do usuário
    loadUserData() {
        const userInfo = document.querySelector('.admin-user-info');
        if (userInfo && this.userData) {
            userInfo.innerHTML = `
                <div>
                    <strong>${this.userData.nome}</strong><br>
                    <small>${this.userData.email}</small>
                </div>
                <img src="../images/user.png" alt="Admin">
            `;
        }
    }

    // Mostrar modal de criar usuário
    showCreateUserModal() {
        const modal = document.createElement('div');
        modal.className = 'admin-modal';
        modal.style.display = 'flex';
        modal.innerHTML = `
            <div class="admin-modal-content">
                <div class="admin-modal-header">
                    <h3 class="admin-modal-title">Criar Novo Usuário</h3>
                    <button class="admin-modal-close">&times;</button>
                </div>
                <form id="createUserForm">
                    <div class="admin-form-group">
                        <label class="admin-form-label">Nome</label>
                        <input type="text" class="admin-form-input" name="nome" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Email</label>
                        <input type="email" class="admin-form-input" name="email" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Username</label>
                        <input type="text" class="admin-form-input" name="username" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Senha</label>
                        <input type="password" class="admin-form-input" name="password" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Tipo de Usuário</label>
                        <select class="admin-form-select" name="tipoUsuario" required>
                            <option value="">Selecione...</option>
                            <option value="CLIENTE">Cliente</option>
                            <option value="PROFISSIONAL">Profissional</option>
                            <option value="PROPRIETARIO">Proprietário</option>
                            <option value="ADMIN">Administrador</option>
                        </select>
                    </div>
                    <div class="d-flex gap-10">
                        <button type="submit" class="admin-btn admin-btn-primary">Criar Usuário</button>
                        <button type="button" class="admin-btn admin-btn-secondary" onclick="this.closest('.admin-modal').remove()">Cancelar</button>
                    </div>
                </form>
            </div>
        `;
        
        document.body.appendChild(modal);
        
        // Configurar formulário
        const form = modal.querySelector('#createUserForm');
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            this.createUser(new FormData(form));
        });
    }

    // Criar usuário
    async createUser(formData) {
        try {
            const userData = {
                nome: formData.get('nome'),
                email: formData.get('email'),
                username: formData.get('username'),
                password: formData.get('password'),
                tipoUsuario: formData.get('tipoUsuario')
            };
            
            const response = await apiClient.request('/admin/usuarios', {
                method: 'POST',
                body: JSON.stringify(userData)
            });
            
            this.showSuccess('Usuário criado com sucesso!');
            this.closeAllModals();
            this.loadUsuarios(); // Recarregar lista
        } catch (error) {
            console.error('AdminPanel: Erro ao criar usuário:', error);
            this.showError('Erro ao criar usuário: ' + error.message);
        }
    }

    // Editar usuário
    // Editar usuário
    async editUser(userId) {
        try {
            console.log('AdminPanel: Editando usuário ID:', userId);
            
            // Buscar dados do usuário
            const usuario = await apiClient.request(`/admin/usuarios/${userId}`, {
                method: 'GET'
            });
            
            console.log('AdminPanel: Dados do usuário recebidos:', usuario);
            this.showEditUserModal(usuario);
            
        } catch (error) {
            console.error('AdminPanel: Erro ao buscar dados do usuário:', error);
            this.showError('Erro ao carregar dados do usuário: ' + error.message);
        }
    }

    // Deletar usuário
    async deleteUser(userId, tipoUsuario) {
        if (confirm('Tem certeza que deseja deletar este usuário?')) {
            try {
                await apiClient.request(`/admin/usuarios/${userId}?tipoUsuario=${tipoUsuario}`, {
                    method: 'DELETE'
                });
                
                this.showSuccess('Usuário deletado com sucesso!');
                this.loadUsuarios(); // Recarregar lista
            } catch (error) {
                console.error('AdminPanel: Erro ao deletar usuário:', error);
                this.showError('Erro ao deletar usuário: ' + error.message);
            }
        }
    }

    // Mostrar modal de edição de usuário
    showEditUserModal(usuario) {
        const modal = document.createElement('div');
        modal.className = 'admin-modal';
        modal.style.display = 'flex';
        modal.innerHTML = `
            <div class="admin-modal-content">
                <div class="admin-modal-header">
                    <h3 class="admin-modal-title">Editar Usuário</h3>
                    <button class="admin-modal-close">&times;</button>
                </div>
                <form id="editUserForm">
                    <div class="admin-form-group">
                        <label class="admin-form-label">Nome</label>
                        <input type="text" class="admin-form-input" name="nome" value="${usuario.nome || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Username</label>
                        <input type="text" class="admin-form-input" name="username" value="${usuario.username || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Email</label>
                        <input type="email" class="admin-form-input" name="email" value="${usuario.email || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Telefone</label>
                        <input type="text" class="admin-form-input" name="telefone" value="${usuario.telefone || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Tipo de Usuário</label>
                        <select class="admin-form-input" name="tipoUsuario" required>
                            <option value="CLIENTE" ${usuario.tipoUsuario === 'CLIENTE' ? 'selected' : ''}>Cliente</option>
                            <option value="PROFISSIONAL" ${usuario.tipoUsuario === 'PROFISSIONAL' ? 'selected' : ''}>Profissional</option>
                            <option value="PROPRIETARIO" ${usuario.tipoUsuario === 'PROPRIETARIO' ? 'selected' : ''}>Proprietário</option>
                            <option value="ADMIN" ${usuario.tipoUsuario === 'ADMIN' ? 'selected' : ''}>Administrador</option>
                        </select>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Nova Senha (deixe em branco para manter a atual)</label>
                        <input type="password" class="admin-form-input" name="password" placeholder="Digite a nova senha...">
                    </div>
                    <div class="admin-form-actions">
                        <button type="submit" class="admin-btn admin-btn-primary">Salvar Alterações</button>
                        <button type="button" class="admin-btn admin-btn-secondary" onclick="adminPanel.closeAllModals()">Cancelar</button>
                    </div>
                </form>
            </div>
        `;
        
        document.body.appendChild(modal);
        
        // Event listener para o formulário
        const form = modal.querySelector('#editUserForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await this.updateUser(usuario.id, new FormData(form));
        });
    }

    // Atualizar usuário
    async updateUser(userId, formData) {
        try {
            console.log('AdminPanel: Atualizando usuário ID:', userId);
            
            const userData = {
                id: userId,
                nome: formData.get('nome'),
                username: formData.get('username'),
                email: formData.get('email'),
                telefone: formData.get('telefone'),
                tipoUsuario: formData.get('tipoUsuario'),
                password: formData.get('password') || null
            };
            
            console.log('AdminPanel: Dados do usuário para atualizar:', userData);
            
            // Usar apiClient em vez de fetch
            const usuarioAtualizado = await apiClient.request(`/admin/usuarios/${userId}`, {
                method: 'PUT',
                body: JSON.stringify(userData)
            });
            
            console.log('AdminPanel: Usuário atualizado com sucesso:', usuarioAtualizado);
            
            this.showSuccess('Usuário atualizado com sucesso!');
            this.closeAllModals();
            this.loadUsuarios(); // Recarregar lista
            
        } catch (error) {
            console.error('AdminPanel: Erro ao atualizar usuário:', error);
            this.showError('Erro ao atualizar usuário: ' + error.message);
        }
    }

    // Renderizar tabela de salões
    renderSaloesTable(saloes) {
        if (!saloes || saloes.length === 0) {
            return '<tr><td colspan="7" class="text-center">Nenhum salão encontrado</td></tr>';
        }
        
        return saloes.map(salao => `
            <tr data-salao-id="${salao.id}" data-status="${salao.status}">
                <td>${salao.nome || 'N/A'}</td>
                <td>${salao.proprietarioNome || 'N/A'}</td>
                <td>${salao.email || 'N/A'}</td>
                <td>${salao.telefone || 'N/A'}</td>
                <td>${salao.endereco || 'N/A'}</td>
                <td>
                    <span class="status-badge status-${salao.status.toLowerCase()}">${salao.status}</span>
                </td>
                <td>
                    ${salao.status === 'PENDENTE' ? `
                        <button class="admin-btn admin-btn-small admin-btn-success" onclick="adminPanel.approveSalao(${salao.id})">
                            Aprovar
                        </button>
                        <button class="admin-btn admin-btn-small admin-btn-danger" onclick="adminPanel.rejectSalao(${salao.id})">
                            Rejeitar
                        </button>
                    ` : `
                        <button class="admin-btn admin-btn-small admin-btn-primary" onclick="adminPanel.editSalao(${salao.id})">
                            Editar
                        </button>
                    `}
                </td>
            </tr>
        `).join('');
    }

    // Filtrar salões
    filterSaloes() {
        const statusFilter = document.getElementById('statusFilter').value;
        const searchTerm = document.getElementById('searchSalao').value.toLowerCase();
        const rows = document.querySelectorAll('#saloesTable tbody tr');
        
        rows.forEach(row => {
            const status = row.getAttribute('data-status');
            const nome = row.cells[0].textContent.toLowerCase();
            const proprietario = row.cells[1].textContent.toLowerCase();
            
            const statusMatch = !statusFilter || status === statusFilter;
            const searchMatch = !searchTerm || 
                nome.includes(searchTerm) || 
                proprietario.includes(searchTerm);
            
            row.style.display = statusMatch && searchMatch ? '' : 'none';
        });
    }

    // Aprovar salão
    async approveSalao(salaoId) {
        try {
            console.log('AdminPanel: Aprovando salão ID:', salaoId);
            
            const resultado = await apiClient.request('/admin/saloes/aprovar', {
                method: 'POST',
                body: JSON.stringify({
                    salaoId: salaoId,
                    status: 'APROVADO',
                    observacao: 'Aprovado pelo administrador'
                })
            });
            
            if (resultado) {
                this.showSuccess('Salão aprovado com sucesso!');
                this.loadSaloesList(); // Recarregar lista
            } else {
                this.showError('Erro ao aprovar salão');
            }
            
        } catch (error) {
            console.error('AdminPanel: Erro ao aprovar salão:', error);
            this.showError('Erro ao aprovar salão: ' + error.message);
        }
    }

    // Rejeitar salão
    async rejectSalao(salaoId) {
        try {
            console.log('AdminPanel: Rejeitando salão ID:', salaoId);
            
            const resultado = await apiClient.request('/admin/saloes/aprovar', {
                method: 'POST',
                body: JSON.stringify({
                    salaoId: salaoId,
                    status: 'REJEITADO',
                    observacao: 'Rejeitado pelo administrador'
                })
            });
            
            if (resultado) {
                this.showSuccess('Salão rejeitado com sucesso!');
                this.loadSaloesList(); // Recarregar lista
            } else {
                this.showError('Erro ao rejeitar salão');
            }
            
        } catch (error) {
            console.error('AdminPanel: Erro ao rejeitar salão:', error);
            this.showError('Erro ao rejeitar salão: ' + error.message);
        }
    }

    // Editar salão
    async editSalao(salaoId) {
        try {
            console.log('AdminPanel: Editando salão ID:', salaoId);
            console.log('AdminPanel: Token disponível:', !!this.getToken());
            
            // Verificar se apiClient está disponível
            if (typeof apiClient === 'undefined') {
                throw new Error('ApiClient não está disponível');
            }
            
            // Buscar dados do salão usando apiClient
            console.log('AdminPanel: Fazendo requisição para:', `/admin/saloes/${salaoId}`);
            const salao = await apiClient.request(`/admin/saloes/${salaoId}`, {
                method: 'GET'
            });
            
            console.log('AdminPanel: Dados do salão recebidos:', salao);
            this.showEditSalaoModal(salao);
            
        } catch (error) {
            console.error('AdminPanel: Erro ao buscar dados do salão:', error);
            this.showError('Erro ao carregar dados do salão: ' + error.message);
        }
    }

    // Mostrar modal de edição de salão
    showEditSalaoModal(salao) {
        const modal = document.createElement('div');
        modal.className = 'admin-modal';
        modal.style.display = 'flex';
        modal.innerHTML = `
            <div class="admin-modal-content">
                <div class="admin-modal-header">
                    <h3 class="admin-modal-title">Editar Salão</h3>
                    <button class="admin-modal-close">&times;</button>
                </div>
                <form id="editSalaoForm">
                    <div class="admin-form-group">
                        <label class="admin-form-label">Nome do Salão</label>
                        <input type="text" class="admin-form-input" name="nome" value="${salao.nome || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Descrição</label>
                        <textarea class="admin-form-input" name="descricao" rows="3">${salao.descricao || ''}</textarea>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Email</label>
                        <input type="email" class="admin-form-input" name="email" value="${salao.email || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Telefone</label>
                        <input type="text" class="admin-form-input" name="telefone" value="${salao.telefone || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Endereço</label>
                        <input type="text" class="admin-form-input" name="endereco" value="${salao.endereco || ''}" required>
                    </div>
                    <div class="admin-form-group">
                        <label class="admin-form-label">Status</label>
                        <select class="admin-form-input" name="status" required>
                            <option value="PENDENTE" ${salao.status === 'PENDENTE' ? 'selected' : ''}>Pendente</option>
                            <option value="APROVADO" ${salao.status === 'APROVADO' ? 'selected' : ''}>Aprovado</option>
                            <option value="REJEITADO" ${salao.status === 'REJEITADO' ? 'selected' : ''}>Rejeitado</option>
                        </select>
                    </div>
                    <div class="admin-form-actions">
                        <button type="submit" class="admin-btn admin-btn-primary">Salvar Alterações</button>
                        <button type="button" class="admin-btn admin-btn-secondary" onclick="adminPanel.closeAllModals()">Cancelar</button>
                    </div>
                </form>
            </div>
        `;
        
        document.body.appendChild(modal);
        
        // Event listener para o formulário
        const form = modal.querySelector('#editSalaoForm');
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await this.updateSalao(salao.id, new FormData(form));
        });
    }

    // Atualizar salão
    async updateSalao(salaoId, formData) {
        try {
            console.log('AdminPanel: Atualizando salão ID:', salaoId);
            
            const salaoData = {
                id: salaoId,
                nome: formData.get('nome'),
                descricao: formData.get('descricao'),
                email: formData.get('email'),
                telefone: formData.get('telefone'),
                endereco: formData.get('endereco'),
                status: formData.get('status')
            };
            
            console.log('AdminPanel: Dados do salão para atualizar:', salaoData);
            
            // Usar apiClient em vez de fetch
            const salaoAtualizado = await apiClient.request(`/admin/saloes/${salaoId}`, {
                method: 'PUT',
                body: JSON.stringify(salaoData)
            });
            
            console.log('AdminPanel: Salão atualizado com sucesso:', salaoAtualizado);
            
            this.showSuccess('Salão atualizado com sucesso!');
            this.closeAllModals();
            this.loadSaloesList(); // Recarregar lista
            
        } catch (error) {
            console.error('AdminPanel: Erro ao atualizar salão:', error);
            this.showError('Erro ao atualizar salão: ' + error.message);
        }
    }

    // Fechar todos os modais
    closeAllModals() {
        const modals = document.querySelectorAll('.admin-modal');
        modals.forEach(modal => modal.remove());
    }

    // Mostrar mensagem de sucesso
    showSuccess(message) {
        this.showMessage(message, 'success');
    }

    // Mostrar mensagem de erro
    showError(message) {
        this.showMessage(message, 'error');
    }

    // Mostrar mensagem de informação
    showInfo(message) {
        this.showMessage(message, 'info');
    }

    // Mostrar mensagem
    showMessage(message, type) {
        const messageDiv = document.createElement('div');
        messageDiv.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            padding: 15px 20px;
            border-radius: 8px;
            color: white;
            font-weight: 500;
            z-index: 3000;
            animation: slideIn 0.3s ease;
        `;
        
        switch (type) {
            case 'success':
                messageDiv.style.backgroundColor = '#28a745';
                break;
            case 'error':
                messageDiv.style.backgroundColor = '#dc3545';
                break;
            case 'info':
                messageDiv.style.backgroundColor = '#17a2b8';
                break;
        }
        
        messageDiv.textContent = message;
        document.body.appendChild(messageDiv);
        
        setTimeout(() => {
            messageDiv.remove();
        }, 3000);
    }

    // Logout
    logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userData');
        window.location.href = 'login.html';
    }
}

// Inicializar painel administrativo quando DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    console.log('AdminPanel: DOM carregado, inicializando...');
    window.adminPanel = new AdminPanel();
});

// Adicionar CSS para animação de mensagens
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
`;
document.head.appendChild(style); 