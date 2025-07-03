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
            // Verificar se usuário está logado
            if (!this.verificarAutenticacao()) {
                window.location.href = 'login.html';
                return;
            }
            
            // Carregar dados do usuário
            await this.carregarDadosUsuario();
            
            // Verificar status do quiz
            await this.verificarStatusQuiz();
            
            // Carregar perguntas
            await this.carregarPerguntas();
            
            // Inicializar interface
            this.inicializarInterface();
            
        } catch (error) {
            console.error('Erro ao inicializar quiz:', error);
            this.mostrarErro('Erro ao carregar o quiz. Tente novamente.');
        }
    }
    
    /**
     * Verifica se o usuário está autenticado
     */
    verificarAutenticacao() {
        const token = localStorage.getItem('authToken');
        const userData = localStorage.getItem('userData');
        
        if (!token || !userData) {
            return false;
        }
        
        try {
            const user = JSON.parse(userData);
            if (user.tipoUsuario !== 'CLIENTE') {
                this.mostrarErro('Apenas clientes podem acessar o quiz.');
                return false;
            }
            
            this.clienteId = user.id;
            return true;
            
        } catch (error) {
            console.error('Erro ao verificar autenticação:', error);
            return false;
        }
    }
    
    /**
     * Carrega dados do usuário logado
     */
    async carregarDadosUsuario() {
        try {
            const userData = await apiClient.getUserProfile();
            this.clienteId = userData.id;
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
            const response = await apiClient.request(`/quiz/cliente/${this.clienteId}/status`, {
                method: 'GET'
            });
            
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
            this.perguntas = await apiClient.request('/quiz/perguntas', {
                method: 'GET'
            });
            
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
        this.atualizarProgressBar();
        this.mostrarPergunta();
        this.atualizarBotoesNavegacao();
        this.adicionarEventListeners();
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
        
        if (!container) return;
        
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
            this.mostrarLoading('Processando suas respostas...');
            
            const respostaQuiz = {
                clienteId: this.clienteId,
                respostas: this.respostas
            };
            
            const recomendacao = await apiClient.request('/quiz/responder', {
                method: 'POST',
                body: JSON.stringify(respostaQuiz)
            });
            
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
        
        // Recarregar interface
        this.inicializarInterface();
        
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
    new QuizManager();
}); 