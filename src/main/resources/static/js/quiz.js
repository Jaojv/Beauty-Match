/**
 * Quiz Manager - Gerenciador do Quiz de Recomendações
 * Responsável por toda a lógica do quiz do lado do cliente
 */
class QuizManager {
    constructor() {
        this.perguntas = [];
        this.respostas = {};
        this.perguntaAtual = 0;
        this.clienteId = null;
        this.quizCompleto = false;
        this.recomendacao = null;
        
        this.init();
    }
    
    /**
     * Inicializa o quiz
     */
    async init() {
        try {
            console.log('Iniciando QuizManager...');
            
            // Verificar se usuário está logado
            if (!this.verificarAutenticacao()) {
                console.log('Usuário não autenticado, redirecionando...');
                window.location.href = 'login.html';
                return;
            }
            
            console.log('Usuário autenticado, clienteId:', this.clienteId);
            
            // Carregar dados do usuário
            await this.carregarDadosUsuario();
            
            // Verificar status do quiz
            await this.verificarStatusQuiz();
            
            // Se já respondeu, não carregar perguntas
            if (this.quizCompleto) {
                console.log('Quiz já respondido, mostrando recomendação existente');
                return;
            }
            
            // Carregar perguntas
            await this.carregarPerguntas();
            
            // Inicializar interface
            this.inicializarInterface();
            
        } catch (error) {
            console.error('Erro ao inicializar quiz:', error);
            this.mostrarErro(`Erro ao carregar o quiz: ${error.message}`);
        }
    }
    
    /**
     * Verifica se o usuário está autenticado
     */
    verificarAutenticacao() {
        const token = localStorage.getItem('authToken');
        const userData = localStorage.getItem('userData');
        
        console.log('=== INÍCIO DO DEBUG VERIFICAR AUTENTICAÇÃO ===');
        console.log('Verificando autenticação...', { token: !!token, userData: !!userData });
        
        if (!token || !userData) {
            console.log('Token ou userData não encontrados no localStorage');
            console.log('=== FIM DO DEBUG VERIFICAR AUTENTICAÇÃO ===');
            return false;
        }
        
        try {
            const user = JSON.parse(userData);
            console.log('Dados do usuário:', user);
            console.log('user.id:', user.id);
            console.log('user.tipoUsuario:', user.tipoUsuario);
            
            if (user.tipoUsuario !== 'CLIENTE') {
                console.log('Usuário não é CLIENTE, tipo:', user.tipoUsuario);
                this.mostrarErro('Apenas clientes podem acessar o quiz.');
                console.log('=== FIM DO DEBUG VERIFICAR AUTENTICAÇÃO ===');
                return false;
            }
            
            this.clienteId = user.id;
            console.log('this.clienteId definido como:', this.clienteId);
            console.log('=== FIM DO DEBUG VERIFICAR AUTENTICAÇÃO ===');
            return true;
            
        } catch (error) {
            console.error('Erro ao verificar autenticação:', error);
            console.log('=== FIM DO DEBUG VERIFICAR AUTENTICAÇÃO ===');
            return false;
        }
    }
    
    /**
     * Carrega dados do usuário logado
     */
    async carregarDadosUsuario() {
        try {
            console.log('=== INÍCIO DO DEBUG CARREGAR DADOS USUÁRIO ===');
            console.log('Carregando dados do usuário...');
            
            const userData = await apiClient.getUserProfile();
            console.log('Dados do usuário carregados:', userData);
            console.log('userData.idUsuario:', userData.idUsuario);
            console.log('Tipo do userData.idUsuario:', typeof userData.idUsuario);
            
            this.clienteId = userData.idUsuario;
            console.log('this.clienteId definido como:', this.clienteId);
            console.log('=== FIM DO DEBUG CARREGAR DADOS USUÁRIO ===');
            
        } catch (error) {
            console.error('Erro ao carregar dados do usuário:', error);
            throw error;
        }
    }
    
    /**
     * Verifica se o cliente já respondeu o quiz
     */
    async verificarStatusQuiz() {
        try {
            console.log('Verificando status do quiz para cliente:', this.clienteId);
            const response = await apiClient.getStatusQuiz(this.clienteId);
            console.log('Status do quiz:', response);
            
            if (response.jaRespondeu) {
                this.quizCompleto = true;
                this.recomendacao = response.resposta;
                this.mostrarRecomendacaoExistente();
            }
            
        } catch (error) {
            console.error('Erro ao verificar status do quiz:', error);
            // Não interrompe o fluxo se houver erro na verificação
        }
    }
    
    /**
     * Carrega as perguntas do quiz
     */
    async carregarPerguntas() {
        try {
            console.log('Carregando perguntas...');
            this.perguntas = await apiClient.getPerguntasQuiz();
            console.log('Perguntas carregadas:', this.perguntas);
            
            if (!this.perguntas || this.perguntas.length === 0) {
                throw new Error('Nenhuma pergunta encontrada');
            }
            
        } catch (error) {
            console.error('Erro ao carregar perguntas:', error);
            throw error;
        }
    }
    
    /**
     * Inicializa a interface do quiz
     */
    inicializarInterface() {
        console.log('Inicializando interface...');
        this.atualizarProgressBar();
        this.mostrarPergunta();
        this.atualizarBotoesNavegacao();
        this.adicionarEventListeners();
        console.log('Interface inicializada com sucesso');
    }
    
    /**
     * Atualiza a barra de progresso
     */
    atualizarProgressBar() {
        const progressBar = document.getElementById('progress-bar');
        const progressText = document.getElementById('progress-text');
        
        if (progressBar && progressText) {
            const progresso = ((this.perguntaAtual + 1) / this.perguntas.length) * 100;
            progressBar.style.width = `${progresso}%`;
            progressText.textContent = `${this.perguntaAtual + 1} de ${this.perguntas.length}`;
        }
    }
    
    /**
     * Mostra a pergunta atual
     */
    mostrarPergunta() {
        if (this.perguntaAtual >= this.perguntas.length) {
            this.finalizarQuiz();
            return;
        }
        
        const pergunta = this.perguntas[this.perguntaAtual];
        const container = document.getElementById('pergunta-container');
        
        if (!container) {
            console.error('Container de pergunta não encontrado');
            return;
        }
        
        console.log('Mostrando pergunta:', pergunta);
        
        container.innerHTML = `
            <div class="pergunta-header">
                <h2 class="pergunta-titulo">${pergunta.texto}</h2>
            </div>
            <div class="alternativas-container">
                ${pergunta.alternativas.map((alt, index) => `
                    <div class="alternativa-item" data-alternativa="${alt.texto}">
                        <input type="radio" 
                               name="pergunta-${this.perguntaAtual}" 
                               id="alt-${this.perguntaAtual}-${index}" 
                               value="${alt.texto}"
                               ${this.respostas[pergunta.texto] === alt.texto ? 'checked' : ''}>
                        <label for="alt-${this.perguntaAtual}-${index}" class="alternativa-label">
                            <span class="alternativa-texto">${alt.texto}</span>
                        </label>
                    </div>
                `).join('')}
            </div>
        `;
        
        // Adicionar event listeners para as alternativas
        this.adicionarEventListenersAlternativas();
        this.atualizarSelecaoAlternativa();
    }
    
    /**
     * Adiciona event listeners para as alternativas
     */
    adicionarEventListenersAlternativas() {
        const alternativas = document.querySelectorAll('.alternativa-item');
        
        alternativas.forEach(alt => {
            alt.addEventListener('click', (e) => {
                const radio = alt.querySelector('input[type="radio"]');
                radio.checked = true;
                
                // Salvar resposta
                const pergunta = this.perguntas[this.perguntaAtual];
                this.respostas[pergunta.texto] = radio.value;
                
                // Atualizar visual
                this.atualizarSelecaoAlternativa();
                this.atualizarBotoesNavegacao();
            });
        });
    }
    
    /**
     * Atualiza a seleção visual das alternativas
     */
    atualizarSelecaoAlternativa() {
        const alternativas = document.querySelectorAll('.alternativa-item');
        
        alternativas.forEach(alt => {
            const radio = alt.querySelector('input[type="radio"]');
            if (radio.checked) {
                alt.classList.add('selecionada');
            } else {
                alt.classList.remove('selecionada');
            }
        });
    }
    
    /**
     * Atualiza os botões de navegação
     */
    atualizarBotoesNavegacao() {
        const btnAnterior = document.getElementById('btn-anterior');
        const btnProximo = document.getElementById('btn-proximo');
        const btnFinalizar = document.getElementById('btn-finalizar');
        
        if (btnAnterior) {
            btnAnterior.disabled = this.perguntaAtual === 0;
        }
        
        const perguntaAtual = this.perguntas[this.perguntaAtual];
        const respostaAtual = this.respostas[perguntaAtual?.texto];
        const podeAvancar = respostaAtual && respostaAtual.trim() !== '';
        
        if (btnProximo) {
            btnProximo.disabled = !podeAvancar;
        }
        
        if (btnFinalizar) {
            btnFinalizar.style.display = this.perguntaAtual === this.perguntas.length - 1 && podeAvancar ? 'block' : 'none';
        }
    }
    
    /**
     * Adiciona event listeners principais
     */
    adicionarEventListeners() {
        const btnAnterior = document.getElementById('btn-anterior');
        const btnProximo = document.getElementById('btn-proximo');
        const btnFinalizar = document.getElementById('btn-finalizar');
        const btnRefazer = document.getElementById('btn-refazer');
        
        if (btnAnterior) {
            btnAnterior.addEventListener('click', () => this.perguntaAnterior());
        }
        
        if (btnProximo) {
            btnProximo.addEventListener('click', () => this.proximaPergunta());
        }
        
        if (btnFinalizar) {
            btnFinalizar.addEventListener('click', () => this.finalizarQuiz());
        }
        
        if (btnRefazer) {
            btnRefazer.addEventListener('click', () => this.refazerQuiz());
        }
    }
    
    /**
     * Vai para a pergunta anterior
     */
    perguntaAnterior() {
        if (this.perguntaAtual > 0) {
            this.perguntaAtual--;
            this.atualizarProgressBar();
            this.mostrarPergunta();
            this.atualizarBotoesNavegacao();
        }
    }
    
    /**
     * Vai para a próxima pergunta
     */
    proximaPergunta() {
        const perguntaAtual = this.perguntas[this.perguntaAtual];
        const respostaAtual = this.respostas[perguntaAtual.texto];
        
        if (!respostaAtual || respostaAtual.trim() === '') {
            this.mostrarErro('Por favor, selecione uma alternativa antes de continuar.');
            return;
        }
        
        if (this.perguntaAtual < this.perguntas.length - 1) {
            this.perguntaAtual++;
            this.atualizarProgressBar();
            this.mostrarPergunta();
            this.atualizarBotoesNavegacao();
        }
    }
    
    /**
     * Finaliza o quiz e envia as respostas
     */
    async finalizarQuiz() {
        try {
            console.log('=== INÍCIO DO DEBUG FINALIZAR QUIZ ===');
            console.log('this.clienteId:', this.clienteId);
            console.log('Tipo do clienteId:', typeof this.clienteId);
            console.log('this.respostas:', this.respostas);
            console.log('Número de respostas:', Object.keys(this.respostas).length);
            
            // Verificar se clienteId está preenchido
            if (!this.clienteId) {
                console.error('ERRO: clienteId está nulo ou undefined!');
                console.log('Dados do localStorage:');
                console.log('- authToken:', !!localStorage.getItem('authToken'));
                console.log('- userData:', localStorage.getItem('userData'));
                
                // Tentar recarregar dados do usuário
                try {
                    await this.carregarDadosUsuario();
                    console.log('clienteId após recarregar:', this.clienteId);
                } catch (reloadError) {
                    console.error('Erro ao recarregar dados do usuário:', reloadError);
                }
                
                if (!this.clienteId) {
                    throw new Error('Não foi possível obter o ID do cliente. Faça login novamente.');
                }
            }
            
            this.mostrarLoading('Processando suas respostas...');
            
            const respostaQuiz = {
                clienteId: this.clienteId,
                respostas: this.respostas
            };
            
            console.log('Payload que será enviado:', JSON.stringify(respostaQuiz, null, 2));
            console.log('=== FIM DO DEBUG FINALIZAR QUIZ ===');
            
            const recomendacao = await apiClient.enviarRespostasQuiz(respostaQuiz);
            
            this.recomendacao = recomendacao;
            this.quizCompleto = true;
            
            this.ocultarLoading();
            this.mostrarRecomendacao();
            
        } catch (error) {
            console.error('Erro ao finalizar quiz:', error);
            this.ocultarLoading();
            this.mostrarErro('Erro ao processar suas respostas. Tente novamente.');
        }
    }
    
    /**
     * Mostra a recomendação final
     */
    mostrarRecomendacao() {
        const modal = document.getElementById('modal-recomendacao');
        const titulo = document.getElementById('modal-titulo');
        const descricao = document.getElementById('modal-descricao');
        const btnRefazer = document.getElementById('btn-refazer');
        
        if (modal && titulo && descricao) {
            titulo.textContent = 'Sua Recomendação Personalizada';
            descricao.innerHTML = this.recomendacao.descricao.replace(/\n/g, '<br>');
            
            if (btnRefazer) {
                btnRefazer.style.display = 'block';
            }
            
            modal.style.display = 'flex';
        }
    }
    
    /**
     * Mostra recomendação existente (se já respondeu)
     */
    mostrarRecomendacaoExistente() {
        const container = document.getElementById('quiz-container');
        
        if (container) {
            container.innerHTML = `
                <div class="quiz-completo">
                    <div class="quiz-completo-header">
                        <h2>Quiz Já Respondido</h2>
                        <p>Você já completou o quiz anteriormente. Aqui está sua recomendação:</p>
                    </div>
                    <div class="recomendacao-existente">
                        <h3>Sua Recomendação Personalizada</h3>
                        <p>${this.recomendacao.descricao}</p>
                    </div>
                    <div class="quiz-completo-acoes">
                        <button id="btn-refazer" class="botao botao-secundario">Refazer Quiz</button>
                    </div>
                </div>
            `;
            
            // Adicionar event listener para refazer
            const btnRefazer = document.getElementById('btn-refazer');
            if (btnRefazer) {
                btnRefazer.addEventListener('click', () => this.refazerQuiz());
            }
        }
    }
    
    /**
     * Refaz o quiz
     */
    refazerQuiz() {
        this.respostas = {};
        this.perguntaAtual = 0;
        this.quizCompleto = false;
        this.recomendacao = null;
        
        // Recarregar perguntas e interface
        this.carregarPerguntas().then(() => {
            this.inicializarInterface();
        }).catch(error => {
            console.error('Erro ao recarregar perguntas:', error);
            this.mostrarErro('Erro ao recarregar o quiz. Tente novamente.');
        });
        
        // Fechar modal se estiver aberto
        const modal = document.getElementById('modal-recomendacao');
        if (modal) {
            modal.style.display = 'none';
        }
    }
    
    /**
     * Mostra loading
     */
    mostrarLoading(mensagem = 'Carregando...') {
        const loading = document.getElementById('loading');
        const loadingText = document.getElementById('loading-text');
        
        if (loading && loadingText) {
            loadingText.textContent = mensagem;
            loading.style.display = 'flex';
        }
    }
    
    /**
     * Oculta loading
     */
    ocultarLoading() {
        const loading = document.getElementById('loading');
        if (loading) {
            loading.style.display = 'none';
        }
    }
    
    /**
     * Mostra erro
     */
    mostrarErro(mensagem) {
        const erro = document.getElementById('erro');
        const erroText = document.getElementById('erro-text');
        
        if (erro && erroText) {
            erroText.textContent = mensagem;
            erro.style.display = 'block';
            
            setTimeout(() => {
                erro.style.display = 'none';
            }, 5000);
        } else {
            alert(mensagem);
        }
    }
}

// Inicializar quiz quando a página carregar
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM carregado, inicializando QuizManager...');
    new QuizManager();
}); 