// Sistema de gerenciamento de favoritos
// Controla exibição, filtro e remoção de salões favoritados

// Gerenciador de Favoritos
class GerenciadorFavoritos {
    constructor() {
        this.inicializar();
    }

    // Inicializa o sistema de favoritos
    async inicializar() {
        console.log('🔧 FavoritosJS: Inicializando gerenciador de favoritos');
        await this.carregarFavoritos();
        this.configurarEventos();
    }

    // Configura eventos de interação
    configurarEventos() {
        // Configurar barra de pesquisa
        const searchBar = document.querySelector('.search-bar');
        if (searchBar) {
            searchBar.addEventListener('input', (e) => {
                this.filtrarFavoritos(e.target.value);
            });
        }
    }

    // Carrega favoritos da API
    async carregarFavoritos() {
        console.log('🔧 FavoritosJS: Carregando favoritos da API');
        
        try {
            const favoritos = await apiClient.getFavoritos();
            console.log('✅ FavoritosJS: Favoritos carregados da API:', favoritos.length);
            
            this.renderizarFavoritos(favoritos);
        } catch (error) {
            console.error('❌ FavoritosJS: Erro ao carregar favoritos:', error);
            this.renderizarFavoritos([]);
        }
    }

    // Renderiza lista de favoritos na tela
    renderizarFavoritos(favoritos) {
        const container = document.querySelector('#carrossel-container');
        if (!container) {
            console.error('❌ FavoritosJS: Container de favoritos não encontrado');
            return;
        }

        // Limpar container
        container.innerHTML = '';

        if (favoritos.length === 0) {
            container.innerHTML = `
                <div style="width: 100%; text-align: center; padding: 40px;">
                    <h3 style="color: #CC5B6F; font-family: HelveticaWorld;">Nenhum salão favoritado</h3>
                    <p style="color: #666; margin-top: 10px;">Adicione salões aos seus favoritos clicando no coração ❤️</p>
                    <a href="../index.html" style="color: #d46b6b; text-decoration: none; font-weight: bold;">
                        Voltar para Salões
                    </a>
                </div>
            `;
            console.log('⚠️ FavoritosJS: Nenhum favorito encontrado');
            return;
        }

        // Renderizar cada favorito
        favoritos.forEach(favorito => {
            const card = this.criarCardFavorito(favorito);
            container.appendChild(card);
        });

        console.log('✅ FavoritosJS: Favoritos renderizados com sucesso');
    }

    // Cria card visual para um favorito
    criarCardFavorito(favorito) {
        console.log('🔧 FavoritosJS: Criando card para favorito:', favorito.nomeSalao);
        
        const card = document.createElement('div');
        card.className = 'card';
        card.style.cursor = 'pointer';
        card.onclick = (e) => {
            // Não redirecionar se clicou na estrela
            if (e.target.closest('.favorito-btn')) {
                return;
            }
            console.log('🔧 FavoritosJS: Card clicado, redirecionando para salão ID:', favorito.salaoId);
            window.location.href = `servicos.html?id=${favorito.salaoId}`;
        };

        // Container para imagem e botão de favorito
        const imgContainer = document.createElement('div');
        imgContainer.style.position = 'relative';
        imgContainer.style.width = '100%';
        imgContainer.style.height = '130px';

        // Imagem do salão
        const img = document.createElement('img');
        img.className = 'card-logo';
        img.src = favorito.imagemUrlSalao || 'images/logo.png';
        img.alt = `Logo do salão ${favorito.nomeSalao}`;
        img.style.width = '100%';
        img.style.height = '100%';
        img.style.objectFit = 'cover';
        imgContainer.appendChild(img);

        // Botão de favorito (sempre preenchido na página de favoritos)
        const favoritoBtn = document.createElement('button');
        favoritoBtn.className = 'favorito-btn';
        favoritoBtn.innerHTML = '❤';
        favoritoBtn.style.cssText = `
            position: absolute;
            top: 8px;
            right: 8px;
            background: transparent;
            border: 2px solid #d46b6b;
            font-size: 20px;
            color: #d46b6b;
            cursor: pointer;
            z-index: 10;
            transition: all 0.3s ease;
            width: 32px;
            height: 32px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            text-shadow: 0 0 5px rgba(212, 107, 107, 0.5);
        `;

        // Evento de clique no botão de favorito
        favoritoBtn.addEventListener('click', (e) => {
            e.stopPropagation(); // Evitar que o clique propague para o card
            this.removerFavorito(favorito.salaoId, card);
        });

        imgContainer.appendChild(favoritoBtn);
        card.appendChild(imgContainer);

        // Nome do salão
        const nome = document.createElement('div');
        nome.className = 'card-nome';
        nome.innerText = favorito.nomeSalao;
        nome.style.textAlign = 'center';
        nome.style.marginTop = '8px';
        card.appendChild(nome);

        // Informações adicionais (opcional)
        if (favorito.enderecoSalao) {
            const endereco = document.createElement('div');
            endereco.style.cssText = `
                font-size: 0.8em;
                color: #666;
                text-align: center;
                margin-top: 4px;
                padding: 0 8px;
            `;
            endereco.innerText = favorito.enderecoSalao;
            card.appendChild(endereco);
        }

        console.log('✅ FavoritosJS: Card de favorito criado com sucesso');
        return card;
    }

    async removerFavorito(salaoId, cardElement) {
        console.log('🔧 FavoritosJS: Removendo favorito ID:', salaoId);
        
        try {
            // Remover do backend
            await apiClient.removerFavorito(salaoId);
            
            // Remover card da tela com animação
            cardElement.style.transition = 'all 0.3s ease';
            cardElement.style.opacity = '0';
            cardElement.style.transform = 'scale(0.8)';
            
            setTimeout(() => {
                cardElement.remove();
                console.log('✅ FavoritosJS: Salão removido dos favoritos');
                
                // Recarregar favoritos se não houver mais nenhum
                const container = document.querySelector('#carrossel-container');
                if (container && container.children.length === 0) {
                    this.carregarFavoritos();
                }
            }, 300);
        } catch (error) {
            console.error('❌ FavoritosJS: Erro ao remover favorito:', error);
            alert('Erro ao remover favorito. Tente novamente.');
        }
    }

    async filtrarFavoritos(termo) {
        console.log('🔧 FavoritosJS: Filtrando favoritos com termo:', termo);
        
        try {
            const favoritos = await apiClient.getFavoritos();
            const favoritosFiltrados = favoritos.filter(fav => 
                fav.nomeSalao.toLowerCase().includes(termo.toLowerCase()) ||
                (fav.enderecoSalao && fav.enderecoSalao.toLowerCase().includes(termo.toLowerCase()))
            );
            
            this.renderizarFavoritos(favoritosFiltrados);
        } catch (error) {
            console.error('❌ FavoritosJS: Erro ao filtrar favoritos:', error);
            this.renderizarFavoritos([]);
        }
    }
}

// Inicializar quando a página carregar
document.addEventListener('DOMContentLoaded', async () => {
    console.log('🔧 FavoritosJS: DOM carregado, iniciando...');
    window.gerenciadorFavoritos = new GerenciadorFavoritos();
    await window.gerenciadorFavoritos.inicializar();
}); 