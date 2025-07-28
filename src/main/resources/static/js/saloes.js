// Sistema de gerenciamento de salões
// Controla exibição, busca e favoritos dos salões

// Função para criar o card do salão
// Cria elemento visual para exibir informações do salão
async function criarCardSalao(salao) {
    console.log('🔧 SaloesJS: Criando card para salão:', salao.nome);
    const card = document.createElement('div');
    card.className = 'card';
    card.style.cursor = 'pointer';
    card.onclick = (e) => {
        // Não redirecionar se clicou na estrela
        if (e.target.closest('.favorito-btn')) {
            return;
        }
        console.log('🔧 SaloesJS: Card clicado, redirecionando para salão ID:', salao.id);
        window.location.href = `pages/servicos.html?id=${salao.id}`;
    };

    // Container para imagem e botão de favorito
    const imgContainer = document.createElement('div');
    imgContainer.style.position = 'relative';
    imgContainer.style.width = '100%';
    imgContainer.style.height = '130px';

    // Imagem do salão
    const img = document.createElement('img');
    img.className = 'card-logo';
    img.src = salao.imagemUrl ? salao.imagemUrl : 'images/logo.png'; // fallback se não houver imagem
    img.alt = `Logo do salão ${salao.nome}`;
    img.style.width = '100%';
    img.style.height = '100%';
    img.style.objectFit = 'cover';
    imgContainer.appendChild(img);

    // Botão de favorito
    const favoritoBtn = document.createElement('button');
    favoritoBtn.className = 'favorito-btn';
    favoritoBtn.innerHTML = '❤';
    favoritoBtn.style.cssText = `
        position: absolute;
        top: 8px;
        right: 8px;
        background: transparent;
        border: 2px solid #ccc;
        font-size: 20px;
        color: #ccc;
        cursor: pointer;
        z-index: 10;
        transition: all 0.3s ease;
        width: 32px;
        height: 32px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
    `;

    // Verificar se o usuário está logado
    const isLoggedIn = localStorage.getItem('authToken') !== null;
    
    // Se não estiver logado, coração fica cinza
    if (!isLoggedIn) {
        favoritoBtn.style.color = '#ccc';
        favoritoBtn.style.borderColor = '#ccc';
    } else {
        // Se estiver logado, verificar status via API
        try {
            const isFavoritado = await apiClient.verificarFavorito(salao.id);
            if (isFavoritado) {
                favoritoBtn.style.color = '#d46b6b';
                favoritoBtn.style.borderColor = '#d46b6b';
                favoritoBtn.style.textShadow = '0 0 5px rgba(212, 107, 107, 0.5)';
            }
        } catch (error) {
            console.error('❌ SaloesJS: Erro ao verificar favorito:', error);
            // Em caso de erro, manter coração cinza
        }
    }

    // Evento de clique no botão de favorito
    favoritoBtn.addEventListener('click', (e) => {
        e.stopPropagation(); // Evitar que o clique propague para o card
        toggleFavorito(salao, favoritoBtn);
    });

    imgContainer.appendChild(favoritoBtn);
    card.appendChild(imgContainer);

    // Nome do salão
    const nome = document.createElement('div');
    nome.className = 'card-nome';
    nome.innerText = salao.nome;
    nome.style.textAlign = 'center';
    nome.style.marginTop = '8px';
    card.appendChild(nome);

    console.log('✅ SaloesJS: Card criado com sucesso');
    return card;
}

// Função para alternar favorito
// Adiciona ou remove salão dos favoritos
async function toggleFavorito(salao, btnElement) {
    console.log('🔧 SaloesJS: Alternando favorito para salão:', salao.nome);
    console.log('🔍 DEBUG: Tipo do salao.id:', typeof salao.id);
    console.log('🔍 DEBUG: Valor do salao.id:', salao.id);
    
    // Verificar se o usuário está logado
    const isLoggedIn = localStorage.getItem('authToken') !== null;
    
    if (!isLoggedIn) {
        console.log('⚠️ SaloesJS: Usuário não logado, redirecionando para login');
        window.location.href = 'pages/login.html';
        return;
    }
    
    try {
        // Verificar se o salão está favoritado
        console.log('🔍 DEBUG: Chamando verificarFavorito com ID:', salao.id);
        const isFavoritado = await apiClient.verificarFavorito(salao.id);
        console.log('🔍 DEBUG: Resultado verificarFavorito:', isFavoritado);
        
        if (!isFavoritado) {
            // Adicionar aos favoritos
            console.log('🔍 DEBUG: Chamando adicionarFavorito com ID:', salao.id);
            await apiClient.adicionarFavorito(salao.id);
            
            // Atualizar visual do botão
            btnElement.style.color = '#d46b6b';
            btnElement.style.borderColor = '#d46b6b';
            btnElement.style.textShadow = '0 0 5px rgba(212, 107, 107, 0.5)';
            
            console.log('✅ SaloesJS: Salão adicionado aos favoritos');
        } else {
            // Remover dos favoritos
            console.log('🔍 DEBUG: Chamando removerFavorito com ID:', salao.id);
            await apiClient.removerFavorito(salao.id);
            
            // Atualizar visual do botão
            btnElement.style.color = '#ccc';
            btnElement.style.borderColor = '#ccc';
            btnElement.style.textShadow = 'none';
            
            console.log('✅ SaloesJS: Salão removido dos favoritos');
        }
        
        console.log('💾 SaloesJS: Favoritos atualizados no backend');
    } catch (error) {
        console.error('❌ SaloesJS: Erro ao alternar favorito:', error);
        alert('Erro ao atualizar favoritos. Tente novamente.');
    }
}

// Função para renderizar os salões
async function renderizarSaloes(filtro = '') {
    console.log('🔧 SaloesJS: Renderizando salões, filtro:', filtro);
    const container = document.querySelector('#carrossel-container');
    if (!container) {
        console.error('❌ SaloesJS: Container de salões não encontrado');
        return;
    }
    
    // Limpar container
    container.innerHTML = '';
    
    try {
        const saloes = await apiClient.getSaloes();
        console.log('✅ SaloesJS: Salões carregados do backend:', saloes.length);
        
        const saloesFiltrados = saloes.filter(s => s.nome.toLowerCase().includes(filtro.toLowerCase()));
        console.log('🔧 SaloesJS: Salões após filtro:', saloesFiltrados.length);
        
        if (saloesFiltrados.length === 0) {
            container.innerHTML = '<p style="width: 100%; text-align: center; color: #CC5B6F; font-family: HelveticaWorld;">Nenhum salão encontrado.</p>';
            console.log('⚠️ SaloesJS: Nenhum salão encontrado');
            return;
        }
        
        // Adicionar cards ao container
        for (const salao of saloesFiltrados) {
            const card = await criarCardSalao(salao);
            container.appendChild(card);
        }
        
        console.log('✅ SaloesJS: Salões renderizados com sucesso');
    } catch (e) {
        console.error('❌ SaloesJS: Erro ao carregar salões:', e);
        container.innerHTML = '<p style="width: 100%; text-align: center; color: #CC5B6F; font-family: HelveticaWorld;">Erro ao carregar salões.</p>';
    }
}

// Função para configurar a barra de pesquisa
function configurarBuscaSaloes() {
    console.log('🔧 SaloesJS: Configurando barra de pesquisa...');
    const searchBar = document.querySelector('.search-bar');
    if (!searchBar) {
        console.error('❌ SaloesJS: Barra de pesquisa não encontrada');
        return;
    }
    searchBar.addEventListener('input', (e) => {
        console.log('🔧 SaloesJS: Busca realizada:', e.target.value);
        renderizarSaloes(e.target.value);
    });
    console.log('✅ SaloesJS: Barra de pesquisa configurada');
}

// Inicialização automática ao carregar a página
window.addEventListener('DOMContentLoaded', () => {
    console.log('🔧 SaloesJS: DOM carregado, iniciando...');
    renderizarSaloes();
    configurarBuscaSaloes();
}); 