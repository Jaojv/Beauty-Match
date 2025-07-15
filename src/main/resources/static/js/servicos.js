// Função para extrair ID do salão da URL
function getSalaoIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    console.log('🔧 ServicosJS: ID do salão extraído da URL:', id);
    return id;
}

// Função para carregar dados do salão
async function carregarDadosSalao(salaoId) {
    console.log('🔧 ServicosJS: Carregando dados do salão ID:', salaoId);
    try {
        const salao = await apiClient.getSalao(salaoId);
        console.log('✅ ServicosJS: Dados do salão carregados:', salao.nome);
        
        // Atualizar informações do salão
        const nomeElement = document.getElementById('nome-salao');
        if (nomeElement) {
            nomeElement.textContent = salao.nome ?? '';
            console.log('🔧 ServicosJS: Nome do salão atualizado');
        }

        const enderecoElement = document.getElementById('endereco-salao');
        if (enderecoElement) {
            enderecoElement.innerHTML = `<i class="fas fa-map-marker-alt"></i> ${salao.endereco ?? ''}`;
            console.log('🔧 ServicosJS: Endereço atualizado');
        }

        const telefoneElement = document.getElementById('telefone-salao');
        if (telefoneElement) {
            telefoneElement.innerHTML = `<i class="fas fa-phone-alt"></i> <i> ${salao.telefone ?? ''} </i>`;
            console.log('🔧 ServicosJS: Telefone atualizado');
        }

        const emailElement = document.getElementById('email-salao');
        if (emailElement) {
            emailElement.innerHTML = `<i class="fas fa-envelope"></i> <b> ${salao.email ?? ''} </b>`;
            console.log('🔧 ServicosJS: Email atualizado');
        }

        // Atualizar imagem do salão se existir
        const logoElement = document.querySelector('.logoservico');
        if (logoElement && salao.imagemUrl) {
            logoElement.src = salao.imagemUrl;
            logoElement.alt = `Logo do ${salao.nome}`;
            console.log('🔧 ServicosJS: Logo do salão atualizado');
        }

        return salao;
    } catch (error) {
        console.error('❌ ServicosJS: Erro ao carregar dados do salão:', error);
        mostrarErro('Erro ao carregar dados do salão');
        return null;
    }
}

// Função para carregar e renderizar serviços
async function carregarServicos(salaoId) {
    console.log('🔧 ServicosJS: Carregando serviços do salão ID:', salaoId);
    try {
        const servicos = await apiClient.getServicosSalao(salaoId);
        console.log('✅ ServicosJS: Serviços carregados:', servicos.length);
        
        const tbody = document.querySelector('table tbody');
        if (!tbody) {
            console.error('❌ ServicosJS: Tabela de serviços não encontrada');
            return;
        }

        // Limpar tabela existente (exceto o cabeçalho)
        const trTitle = tbody.querySelector('.trtitle');
        tbody.innerHTML = '';
        if (trTitle) {
            tbody.appendChild(trTitle);
        }

        if (servicos.length === 0) {
            const tr = document.createElement('tr');
            tr.className = 'itemtabela';
            tr.innerHTML = '<td colspan="3" style="text-align: center;">Nenhum serviço disponível</td>';
            tbody.appendChild(tr);
            console.log('⚠️ ServicosJS: Nenhum serviço encontrado');
            return;
        }

        // Renderizar cada serviço
        servicos.forEach(servico => {
            const tr = document.createElement('tr');
            tr.className = 'itemtabela';
            
            tr.innerHTML = `
                <td><input type="text" value="${servico.nome}" readonly></td>
                <td><input type="text" value="R$ ${servico.preco.toFixed(2).replace('.', ',')}" class="preco" readonly></td>
                <td><button class="btn-agendar" data-servico-id="${servico.id}" data-salao-id="${salaoId}">Agendar</button></td>
            `;
            
            tbody.appendChild(tr);
        });

        console.log('🔧 ServicosJS: Serviços renderizados na tabela');

        // Configurar botões de agendamento
        configurarBotoesAgendamento();
        
    } catch (error) {
        console.error('❌ ServicosJS: Erro ao carregar serviços:', error);
        mostrarErro('Erro ao carregar serviços');
    }
}

// Função para configurar botões de agendamento
function configurarBotoesAgendamento() {
    console.log('🔧 ServicosJS: Configurando botões de agendamento...');
    const botoes = document.querySelectorAll('.btn-agendar');
    console.log('🔧 ServicosJS: Encontrados', botoes.length, 'botões de agendamento');
    
    botoes.forEach(botao => {
        botao.addEventListener('click', function() {
            const servicoId = this.getAttribute('data-servico-id');
            const salaoId = this.getAttribute('data-salao-id');
            
            console.log('🔧 ServicosJS: Botão agendar clicado - Serviço:', servicoId, 'Salão:', salaoId);
            
            // Redirecionar para página de agendamento
            window.location.href = `horario.html?servicoId=${servicoId}&salaoId=${salaoId}`;
        });
    });
}

// Função para mostrar erro
function mostrarErro(mensagem) {
    console.error('❌ ServicosJS: Mostrando erro:', mensagem);
    const container = document.querySelector('.container');
    if (container) {
        const erroDiv = document.createElement('div');
        erroDiv.style.cssText = 'text-align: center; color: red; padding: 20px;';
        erroDiv.innerHTML = `
            <h3>Erro</h3>
            <p>${mensagem}</p>
            <button onclick="window.location.href='../index.html'">Voltar para Salões</button>
        `;
        container.appendChild(erroDiv);
    }
}

// Função para verificar se o ID do salão é válido
function validarSalaoId(salaoId) {
    console.log('🔧 ServicosJS: Validando ID do salão:', salaoId);
    if (!salaoId || isNaN(salaoId) || salaoId <= 0) {
        console.error('❌ ServicosJS: ID do salão inválido:', salaoId);
        mostrarErro('ID do salão inválido');
        return false;
    }
    console.log('✅ ServicosJS: ID do salão válido');
    return true;
}

// Função principal para inicializar a página
async function inicializarPaginaServicos() {
    console.log('🔧 ServicosJS: Inicializando página de serviços...');
    const salaoId = getSalaoIdFromUrl();
    
    if (!validarSalaoId(salaoId)) {
        return;
    }

    try {
        // Carregar dados do salão
        const salao = await carregarDadosSalao(salaoId);
        if (!salao) {
            return;
        }

        // Carregar serviços do salão
        await carregarServicos(salaoId);
        
        console.log('✅ ServicosJS: Página inicializada com sucesso');
    } catch (error) {
        console.error('❌ ServicosJS: Erro ao inicializar página:', error);
        mostrarErro('Erro ao carregar dados');
    }
}

// Inicializar quando a página carregar
document.addEventListener('DOMContentLoaded', () => {
    console.log('🔧 ServicosJS: DOM carregado, iniciando...');
    inicializarPaginaServicos();
});

// Função para verificar se o usuário está logado (sem depender do authManager)
function isUserLoggedIn() {
    const isLoggedIn = localStorage.getItem('authToken') !== null;
    console.log('🔧 ServicosJS: Verificando se usuário está logado:', isLoggedIn);
    return isLoggedIn;
}

// Função para obter dados do usuário (sem depender do authManager)
function getUserData() {
    const userData = localStorage.getItem('userData');
    const parsed = userData ? JSON.parse(userData) : null;
    console.log('🔧 ServicosJS: Obtendo dados do usuário:', !!parsed);
    return parsed;
} 