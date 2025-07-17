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
            const servicoNome = this.closest('tr').querySelector('td input').value;
            const servicoPreco = this.closest('tr').querySelector('.preco').value;
            
            console.log('🔧 ServicosJS: Botão agendar clicado - Serviço:', servicoId, 'Salão:', salaoId);
            
            // Abrir modal de agendamento
            abrirModalAgendamento(servicoId, salaoId, servicoNome, servicoPreco);
        });
    });
}

// Função para abrir modal de agendamento
function abrirModalAgendamento(servicoId, salaoId, servicoNome, servicoPreco) {
    console.log('🔧 ServicosJS: Abrindo modal de agendamento...');
    
    // Criar modal dinamicamente
    const modal = document.createElement('div');
    modal.id = 'modal-agendamento';
    modal.className = 'modal-agendamento';
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
    `;
    
    modal.innerHTML = `
        <div class="modal-conteudo" style="
            background: #2a2a2a;
            padding: 30px;
            border-radius: 10px;
            max-width: 500px;
            width: 90%;
            max-height: 80vh;
            overflow-y: auto;
            position: relative;
        ">
            <span class="fechar-modal" id="fechar-modal" style="
                position: absolute;
                top: 10px;
                right: 15px;
                font-size: 24px;
                cursor: pointer;
                color: #fff;
            ">&times;</span>
            
            <form id="form-agendamento" method="post" action="">
                <h3 style="color: #fff; margin-bottom: 20px;">Agendar Horário</h3>
                
                <!-- Informações do Serviço Selecionado -->
                <div class="servico-selecionado" style="margin-bottom: 20px; padding: 15px; background: #3a3a3a; border-radius: 5px;">
                    <h4 style="color: #fff; margin: 0 0 10px 0;">Serviço: <span id="servico-nome-modal">${servicoNome}</span></h4>
                    <p style="color: #fff; margin: 0;">Preço: <span id="servico-preco-modal">${servicoPreco}</span></p>
                </div>
                
                <!-- Calendário -->
                <div style="margin-bottom: 20px;">
                    <label for="data" style="color: #fff; display: block; margin-bottom: 5px;">Selecione a data:</label>
                    <input type="date" id="data" name="data" required min="" style="
                        width: 100%;
                        padding: 10px;
                        border: 1px solid #555;
                        border-radius: 5px;
                        background: #3a3a3a;
                        color: #fff;
                    ">
                </div>
                
                <!-- Profissionais -->
                <div class="profissionais" style="margin-bottom: 20px;">
                    <label style="color: #fff; display: block; margin-bottom: 10px;">Selecione o profissional:</label>
                    <div id="lista-profissionais" style="display: flex; gap: 10px; flex-wrap: wrap;">
                        <!-- Profissionais serão carregados dinamicamente -->
                    </div>
                </div>
                
                <!-- Horários -->
                <div class="horarios" style="margin-bottom: 20px;">
                    <label style="color: #fff; display: block; margin-bottom: 10px;">Horário:</label>
                    <div id="lista-horarios" style="display: flex; gap: 10px; flex-wrap: wrap;">
                        <!-- Horários serão inseridos via JS -->
                    </div>
                </div>
                
                <button type="submit" class="btn-agendar" style="
                    width: 100%;
                    padding: 12px;
                    background: #d46b6b;
                    color: white;
                    border: none;
                    border-radius: 5px;
                    cursor: pointer;
                    font-size: 16px;
                ">Confirmar Agendamento</button>
            </form>
        </div>
    `;
    
    // Adicionar modal ao body
    document.body.appendChild(modal);
    
    // Configurar funcionalidades do modal
    configurarModalAgendamento(servicoId, salaoId);
}

// Função para configurar modal de agendamento
async function configurarModalAgendamento(servicoId, salaoId) {
    console.log('🔧 ServicosJS: Configurando modal de agendamento...');
    
    // Fechar modal
    const fecharModal = document.getElementById('fechar-modal');
    if (fecharModal) {
        fecharModal.onclick = function() {
            document.getElementById('modal-agendamento').remove();
        };
    }

    // Fechar modal ao clicar fora
    window.onclick = function(event) {
        const modal = document.getElementById('modal-agendamento');
        if (event.target === modal) {
            modal.remove();
        }
    };

    // Configurar data mínima
    configurarDataMinima();
    
    // Renderizar horários
    renderizarHorarios();
    
    // Carregar profissionais
    await carregarProfissionaisModal(salaoId);
    
    // Configurar formulário
    configurarFormularioAgendamentoModal(servicoId, salaoId);
}

// Função para configurar data mínima
function configurarDataMinima() {
    const dataInput = document.getElementById('data');
    if (dataInput) {
        const hoje = new Date().toISOString().split('T')[0];
        dataInput.min = hoje;
        dataInput.value = hoje;
    }
}

// Função para renderizar horários
function renderizarHorarios() {
    console.log('🔧 ServicosJS: Renderizando horários...');
    
    const horariosDisponiveis = [
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
    ];
    
    const horariosBloqueados = ["12:00", "15:00"];
    
    const lista = document.getElementById('lista-horarios');
    if (!lista) return;

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
        label.style.cssText = `
            padding: 8px 12px;
            border: 1px solid #555;
            border-radius: 5px;
            cursor: pointer;
            background: #3a3a3a;
            color: #fff;
            margin: 2px;
        `;
        
        if (horariosBloqueados.includes(horario)) {
            label.style.opacity = '0.5';
            label.style.cursor = 'not-allowed';
        }
        
        lista.appendChild(input);
        lista.appendChild(label);
    });

    // Configurar eventos de horários
    lista.addEventListener('click', function(e) {
        if (e.target.classList.contains('horario-btn') && !e.target.classList.contains('bloqueado')) {
            document.querySelectorAll('.horario-btn').forEach(function(btn) {
                btn.style.background = '#3a3a3a';
            });
            e.target.style.background = '#d46b6b';
            
            const input = document.getElementById('horario-' + e.target.innerText);
            if (input) input.checked = true;
        }
    });
}

// Função para carregar profissionais no modal
async function carregarProfissionaisModal(salaoId) {
    console.log('🔧 ServicosJS: Carregando profissionais para modal...');
    try {
        const profissionais = await apiClient.getProfissionaisSalao(salaoId);
        console.log('✅ ServicosJS: Profissionais carregados:', profissionais.length);
        
        const container = document.getElementById('lista-profissionais');
        if (!container) return;

        container.innerHTML = '';

        if (profissionais.length === 0) {
            container.innerHTML = '<p style="color: #fff;">Nenhum profissional disponível</p>';
            return;
        }

        // Renderizar cada profissional
        profissionais.forEach((profissional, index) => {
            const input = document.createElement('input');
            input.type = 'radio';
            input.id = `prof${index + 1}`;
            input.name = 'profissional';
            input.value = profissional.idUsuario;
            input.required = index === 0;
            input.style.display = 'none';

            const label = document.createElement('label');
            label.htmlFor = `prof${index + 1}`;
            label.className = 'foto-profissional';
            label.style.cssText = `
                padding: 8px 12px;
                border-radius: 5px;
                cursor: pointer;
                margin: 2px;
                background: ${index % 2 === 0 ? '#d46b6b' : '#7d3535'};
                color: white;
            `;
            label.textContent = profissional.nome || `Profissional ${index + 1}`;

            container.appendChild(input);
            container.appendChild(label);
        });

    } catch (error) {
        console.error('❌ ServicosJS: Erro ao carregar profissionais:', error);
        carregarProfissionaisMockModal();
    }
}

// Função para carregar profissionais mock no modal
function carregarProfissionaisMockModal() {
    console.log('⚠️ ServicosJS: Usando profissionais mock no modal');
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
        input.style.display = 'none';

        const label = document.createElement('label');
        label.htmlFor = `prof${index + 1}`;
        label.className = 'foto-profissional';
        label.style.cssText = `
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
            margin: 2px;
            background: ${index % 2 === 0 ? '#d46b6b' : '#7d3535'};
            color: white;
        `;
        label.textContent = prof.nome;

        container.appendChild(input);
        container.appendChild(label);
    });
}

// Função para configurar formulário de agendamento no modal
async function configurarFormularioAgendamentoModal(servicoId, salaoId) {
    const form = document.getElementById('form-agendamento');
    if (!form) return;

    // Verificar se o usuário está logado
    if (!isUserLoggedIn()) {
        alert('Você precisa estar logado para fazer um agendamento. Redirecionando para a página de login...');
        window.location.href = 'login.html';
        return;
    }

    // Configurar eventos para carregar horários dinamicamente
    const dataInput = document.getElementById('data');
    const profissionalInputs = document.querySelectorAll('input[name="profissional"]');
    const horariosContainer = document.getElementById('lista-horarios');

    // Evento para mudança de data
    dataInput.addEventListener('change', async function() {
        const dataSelecionada = this.value;
        if (!dataSelecionada) return;

        // Limpar horários anteriores
        horariosContainer.innerHTML = '<p style="color: #fff;">Selecione um profissional para ver os horários disponíveis</p>';
        
        // Verificar se há profissional selecionado
        const profissionalSelecionado = document.querySelector('input[name="profissional"]:checked');
        if (profissionalSelecionado) {
            await carregarHorariosDisponiveis(profissionalSelecionado.value, dataSelecionada, salaoId);
        }
    });

    // Evento para mudança de profissional
    profissionalInputs.forEach(input => {
        input.addEventListener('change', async function() {
            const dataSelecionada = dataInput.value;
            if (!dataSelecionada) {
                alert('Selecione uma data primeiro');
                this.checked = false;
                return;
            }
            
            await carregarHorariosDisponiveis(this.value, dataSelecionada, salaoId);
        });
    });

    // Configurar envio do formulário
    form.onsubmit = async function(e) {
        e.preventDefault();
        
        const formData = new FormData(form);
        const data = formData.get('data');
        const profissional = formData.get('profissional');
        const horario = formData.get('horario');
        
        if (!data || !profissional || !horario) {
            alert('Por favor, preencha todos os campos');
            return;
        }

        try {
            // Combinar data e horário
            const dataHora = new Date(data + 'T' + horario);
            
            const dadosAgendamento = {
                profissionalId: parseInt(profissional),
                servicoId: parseInt(servicoId),
                salaoId: parseInt(salaoId),
                dataHora: dataHora.toISOString(),
                observacoes: formData.get('observacoes') || ''
            };

            console.log('🔧 ServicosJS: Enviando agendamento:', dadosAgendamento);
            
            // Mostrar loading
            const submitButton = form.querySelector('button[type="submit"]');
            const originalText = submitButton.textContent;
            submitButton.textContent = 'Agendando...';
            submitButton.disabled = true;
            
            // Enviar para a API
            const response = await apiClient.criarAgendamento(dadosAgendamento);
            
            console.log('✅ ServicosJS: Agendamento criado com sucesso:', response);
            
            // Mostrar sucesso
            alert('Agendamento realizado com sucesso!');
            
            // Fechar modal e redirecionar
            document.getElementById('modal-agendamento').remove();
            window.location.href = 'agendamentos.html';
            
        } catch (error) {
            console.error('❌ ServicosJS: Erro ao criar agendamento:', error);
            
            // Restaurar botão
            submitButton.textContent = originalText;
            submitButton.disabled = false;
            
            // Mostrar erro específico
            let mensagemErro = 'Erro ao criar agendamento';
            if (error.message) {
                if (error.message.includes('Profissional não está disponível')) {
                    mensagemErro = 'O profissional não está disponível neste horário. Tente outro horário.';
                } else if (error.message.includes('Horário está bloqueado')) {
                    mensagemErro = 'Este horário está bloqueado. Tente outro horário.';
                } else if (error.message.includes('Existe conflito')) {
                    mensagemErro = 'Este horário já foi agendado. Tente outro horário.';
                } else if (error.message.includes('passado')) {
                    mensagemErro = 'Não é possível agendar para datas/horários no passado.';
                } else {
                    mensagemErro = error.message;
                }
            }
            
            alert(mensagemErro);
        }
    };
}

// Função para carregar horários disponíveis
async function carregarHorariosDisponiveis(profissionalId, data, salaoId) {
    console.log('🔧 ServicosJS: Carregando horários disponíveis...', { profissionalId, data, salaoId });
    
    try {
        // Mostrar loading
        const horariosContainer = document.getElementById('lista-horarios');
        horariosContainer.innerHTML = '<p style="color: #fff;">Carregando horários...</p>';
        
        // Buscar horários disponíveis na API
        const horariosDisponiveis = await apiClient.getHorariosDisponiveis(salaoId, profissionalId, data);
        
        console.log('✅ ServicosJS: Horários disponíveis carregados:', horariosDisponiveis);
        
        // Renderizar horários
        renderizarHorariosDisponiveis(horariosDisponiveis);
        
    } catch (error) {
        console.error('❌ ServicosJS: Erro ao carregar horários:', error);
        const horariosContainer = document.getElementById('lista-horarios');
        horariosContainer.innerHTML = '<p style="color: #ff6b6b;">Erro ao carregar horários. Tente novamente.</p>';
    }
}

// Função para renderizar horários disponíveis
function renderizarHorariosDisponiveis(horariosDisponiveis) {
    const container = document.getElementById('lista-horarios');
    if (!container) return;

    container.innerHTML = '';

    if (horariosDisponiveis.length === 0) {
        container.innerHTML = '<p style="color: #ff6b6b;">Nenhum horário disponível para esta data</p>';
        return;
    }

    // Renderizar cada horário disponível
    horariosDisponiveis.forEach(horario => {
        const input = document.createElement('input');
        input.type = 'radio';
        input.name = 'horario';
        input.value = horario;
        input.id = 'horario-' + horario;
        input.style.display = 'none';

        const label = document.createElement('label');
        label.className = 'horario-btn';
        label.htmlFor = input.id;
        label.innerText = horario;
        label.style.cssText = `
            padding: 8px 12px;
            border: 1px solid #555;
            border-radius: 5px;
            cursor: pointer;
            background: #3a3a3a;
            color: #fff;
            margin: 2px;
            transition: background-color 0.2s;
        `;

        // Adicionar evento de hover
        label.addEventListener('mouseenter', function() {
            this.style.background = '#d46b6b';
        });

        label.addEventListener('mouseleave', function() {
            this.style.background = '#3a3a3a';
        });

        // Adicionar evento de clique
        label.addEventListener('click', function() {
            // Remover seleção anterior
            document.querySelectorAll('.horario-btn').forEach(btn => {
                btn.style.background = '#3a3a3a';
            });
            
            // Selecionar este horário
            this.style.background = '#d46b6b';
        });

        container.appendChild(input);
        container.appendChild(label);
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