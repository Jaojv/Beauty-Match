// Sistema de gerenciamento de horários profissionais
// Controla seleção de serviços, profissionais e horários disponíveis

// Função para extrair parâmetros da URL
// Obtém IDs do serviço e salão da query string
function getUrlParams() {
    const urlParams = new URLSearchParams(window.location.search);
    return {
        servicoId: urlParams.get('servicoId'),
        salaoId: urlParams.get('salaoId')
    };
}

// Função para carregar dados do salão
// Busca informações do salão na API e atualiza a interface
async function carregarDadosSalao(salaoId) {
    console.log('🔧 HorarioJS: Carregando dados do salão ID:', salaoId);
    try {
        const salao = await apiClient.getSalao(salaoId);
        console.log('✅ HorarioJS: Dados do salão carregados:', salao.nome);
        
        // Atualizar informações do salão
        const nomeElement = document.getElementById('nome-salao');
        if (nomeElement) {
            nomeElement.textContent = salao.nome ?? '';
        }

        const enderecoElement = document.getElementById('endereco-salao');
        if (enderecoElement) {
            enderecoElement.innerHTML = `<i class="fas fa-map-marker-alt"></i> ${salao.endereco ?? ''}`;
        }

        const telefoneElement = document.getElementById('telefone-salao');
        if (telefoneElement) {
            telefoneElement.innerHTML = `<i class="fas fa-phone-alt"></i> <i> ${salao.telefone ?? ''} </i>`;
        }

        const emailElement = document.getElementById('email-salao');
        if (emailElement) {
            emailElement.innerHTML = `<i class="fas fa-envelope"></i> <b> ${salao.email ?? ''} </b>`;
        }

        // Atualizar imagem do salão se existir
        const logoElement = document.getElementById('imagem-salao');
        if (logoElement && salao.imagemUrl) {
            logoElement.src = salao.imagemUrl;
            logoElement.alt = `Logo do ${salao.nome}`;
        }

        return salao;
    } catch (error) {
        console.error('❌ HorarioJS: Erro ao carregar dados do salão:', error);
        mostrarErro('Erro ao carregar dados do salão');
        return null;
    }
}

// Função para carregar e renderizar serviços
// Busca serviços do salão e exibe na tabela para seleção
async function carregarServicos(salaoId) {
    console.log('🔧 HorarioJS: Carregando serviços do salão ID:', salaoId);
    try {
        const servicos = await apiClient.getServicosSalao(salaoId);
        console.log('✅ HorarioJS: Serviços carregados:', servicos.length);
        
        const tbody = document.getElementById('servicos-list');
        if (!tbody) {
            console.error('❌ HorarioJS: Tabela de serviços não encontrada');
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
            console.log('⚠️ HorarioJS: Nenhum serviço encontrado');
            return;
        }

        // Renderizar cada serviço
        servicos.forEach(servico => {
            const tr = document.createElement('tr');
            tr.className = 'itemtabela';
            
            tr.innerHTML = `
                <td><input type="text" value="${servico.nome}" readonly></td>
                <td><input type="text" value="R$ ${servico.preco.toFixed(2).replace('.', ',')}" class="preco" readonly></td>
                <td><button class="btn-selecionar" data-servico-id="${servico.id}" data-servico-nome="${servico.nome}" data-servico-preco="${servico.preco}">Selecionar</button></td>
            `;
            
            tbody.appendChild(tr);
        });

        console.log('🔧 HorarioJS: Serviços renderizados na tabela');

        // Configurar botões de seleção
        configurarBotoesSelecao();
        
    } catch (error) {
        console.error('❌ HorarioJS: Erro ao carregar serviços:', error);
        mostrarErro('Erro ao carregar serviços');
    }
}

// Função para configurar botões de seleção
function configurarBotoesSelecao() {
    console.log('🔧 HorarioJS: Configurando botões de seleção...');
    const botoes = document.querySelectorAll('.btn-selecionar');
    console.log('🔧 HorarioJS: Encontrados', botoes.length, 'botões de seleção');
    
    botoes.forEach(botao => {
        botao.addEventListener('click', function() {
            const servicoId = this.getAttribute('data-servico-id');
            const servicoNome = this.getAttribute('data-servico-nome');
            const servicoPreco = this.getAttribute('data-servico-preco');
            
            console.log('🔧 HorarioJS: Botão selecionar clicado - Serviço:', servicoId, servicoNome);
            
            // Atualizar informações no modal
            document.getElementById('servico-nome-modal').textContent = servicoNome;
            document.getElementById('servico-preco-modal').textContent = `R$ ${parseFloat(servicoPreco).toFixed(2).replace('.', ',')}`;
            
            // Abrir modal
            document.getElementById('modal-agendamento').style.display = 'flex';
            document.getElementById('form-agendamento').reset();
            configurarDataMinima();
            renderizarHorarios();
        });
    });
}

// Função para carregar profissionais do salão
async function carregarProfissionais(salaoId) {
    console.log('🔧 HorarioJS: Carregando profissionais do salão ID:', salaoId);
    try {
        const profissionais = await apiClient.getProfissionaisSalao(salaoId);
        console.log('✅ HorarioJS: Profissionais carregados:', profissionais.length);
        
        const container = document.getElementById('lista-profissionais');
        if (!container) {
            console.error('❌ HorarioJS: Container de profissionais não encontrado');
            return;
        }

        container.innerHTML = '';

        if (profissionais.length === 0) {
            container.innerHTML = '<p>Nenhum profissional disponível</p>';
            return;
        }

        // Renderizar cada profissional
        profissionais.forEach((profissional, index) => {
            const input = document.createElement('input');
            input.type = 'radio';
            input.id = `prof${index + 1}`;
            input.name = 'profissional';
            input.value = profissional.idUsuario;
            input.required = index === 0; // Primeiro é obrigatório

            const label = document.createElement('label');
            label.htmlFor = `prof${index + 1}`;
            label.className = 'foto-profissional';
            label.style.background = index % 2 === 0 ? '#d46b6b' : '#7d3535';
            label.textContent = profissional.nome || `Profissional ${index + 1}`;

            container.appendChild(input);
            container.appendChild(label);
        });

        return profissionais;
    } catch (error) {
        console.error('❌ HorarioJS: Erro ao carregar profissionais:', error);
        // Se não conseguir carregar, usar profissionais mock
        carregarProfissionaisMock();
        return [];
    }
}

// Função para carregar profissionais mock (fallback)
function carregarProfissionaisMock() {
    console.log('⚠️ HorarioJS: Usando profissionais mock');
    const container = document.getElementById('lista-profissionais');
    if (!container) return;

    const profissionaisMock = [
        { idUsuario: 1, nome: 'Malu' },
        { idUsuario: 2, nome: 'Ana' },
        { idUsuario: 3, nome: 'Maria' }
    ];

    container.innerHTML = '';
    profissionaisMock.forEach((prof, index) => {
        const input = document.createElement('input');
        input.type = 'radio';
        input.id = `prof${index + 1}`;
        input.name = 'profissional';
        input.value = prof.idUsuario;
        input.required = index === 0;

        const label = document.createElement('label');
        label.htmlFor = `prof${index + 1}`;
        label.className = 'foto-profissional';
        label.style.background = index % 2 === 0 ? '#d46b6b' : '#7d3535';
        label.textContent = prof.nome;

        container.appendChild(input);
        container.appendChild(label);
    });
}

// Função para configurar data mínima (hoje)
function configurarDataMinima() {
    const dataInput = document.getElementById('data');
    if (dataInput) {
        const hoje = new Date().toISOString().split('T')[0];
        dataInput.min = hoje;
        dataInput.value = hoje;
    }
}

// Função para renderizar horários disponíveis
function renderizarHorarios() {
    console.log('🔧 HorarioJS: Renderizando horários...');
    
    // Horários disponíveis (mock - pode ser integrado com API)
    const horariosDisponiveis = [
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
    ];
    
    // Exemplo de horários bloqueados (mock)
    const horariosBloqueados = ["12:00", "15:00"];
    
    const lista = document.getElementById('lista-horarios');
    if (!lista) {
        console.error('❌ HorarioJS: Lista de horários não encontrada');
        return;
    }

    lista.innerHTML = '';
    
    horariosDisponiveis.forEach(function(horario) {
        const input = document.createElement('input');
        input.type = 'radio';
        input.name = 'horario';
        input.value = horario;
        input.id = 'horario-' + horario;
        input.style.display = 'none';
        
        if (horariosBloqueados.includes(horario)) {
            input.disabled = true;
        }
        
        const label = document.createElement('label');
        label.className = 'horario-btn' + (horariosBloqueados.includes(horario) ? ' bloqueado' : '');
        label.htmlFor = input.id;
        label.innerText = horario;
        
        lista.appendChild(input);
        lista.appendChild(label);
    });
}

// Função para configurar eventos de horários
function configurarEventosHorarios() {
    const lista = document.getElementById('lista-horarios');
    if (!lista) return;

    lista.addEventListener('click', function(e) {
        if (e.target.classList.contains('horario-btn') && !e.target.classList.contains('bloqueado')) {
            // Remover seleção anterior
            document.querySelectorAll('.horario-btn').forEach(function(btn) {
                btn.classList.remove('selecionado');
            });
            
            // Adicionar seleção atual
            e.target.classList.add('selecionado');
            
            // Seleciona o input correspondente
            const input = document.getElementById('horario-' + e.target.innerText);
            if (input) input.checked = true;
        }
    });
}

// Função para configurar modal
function configurarModal() {
    console.log('🔧 HorarioJS: Configurando modal...');
    
    // Fechar modal
    const fecharModal = document.getElementById('fechar-modal');
    if (fecharModal) {
        fecharModal.onclick = function() {
            document.getElementById('modal-agendamento').style.display = 'none';
        };
    }

    // Fechar modal ao clicar fora
    window.onclick = function(event) {
        const modal = document.getElementById('modal-agendamento');
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    };
}

// Função para configurar formulário de agendamento
function configurarFormularioAgendamento() {
    const form = document.getElementById('form-agendamento');
    if (!form) return;

    form.onsubmit = function(e) {
        e.preventDefault();
        
        const formData = new FormData(form);
        const dados = {
            servicoId: getUrlParams().servicoId,
            salaoId: getUrlParams().salaoId,
            data: formData.get('data'),
            profissional: formData.get('profissional'),
            horario: formData.get('horario')
        };

        console.log('🔧 HorarioJS: Dados do agendamento:', dados);
        
        // Aqui você pode enviar para a API
        // apiClient.criarAgendamento(dados);
        
        alert('Agendamento realizado com sucesso!');
        window.location.href = '../index.html';
    };
}

// Função para mostrar erro
function mostrarErro(mensagem) {
    console.error('❌ HorarioJS: Mostrando erro:', mensagem);
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

// Função principal para inicializar a página
async function inicializarPaginaHorarios() {
    console.log('🔧 HorarioJS: Inicializando página de horários...');
    
    const params = getUrlParams();
    console.log('🔧 HorarioJS: Parâmetros da URL:', params);
    
    if (!params.salaoId) {
        console.error('❌ HorarioJS: ID do salão não fornecido');
        mostrarErro('ID do salão não fornecido');
        return;
    }

    try {
        // Carregar dados do salão
        const salao = await carregarDadosSalao(params.salaoId);
        if (!salao) {
            return;
        }

        // Carregar serviços do salão
        await carregarServicos(params.salaoId);
        
        // Carregar profissionais
        await carregarProfissionais(params.salaoId);
        
        // Configurar funcionalidades
        configurarModal();
        configurarEventosHorarios();
        configurarFormularioAgendamento();
        
        console.log('✅ HorarioJS: Página inicializada com sucesso');
    } catch (error) {
        console.error('❌ HorarioJS: Erro ao inicializar página:', error);
        mostrarErro('Erro ao carregar dados');
    }
}

// Inicializar quando a página carregar
document.addEventListener('DOMContentLoaded', () => {
    console.log('🔧 HorarioJS: DOM carregado, iniciando...');
    inicializarPaginaHorarios();
});