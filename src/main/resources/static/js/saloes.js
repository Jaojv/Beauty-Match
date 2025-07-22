// Função para criar o card do salão
function criarCardSalao(salao) {
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

    // Verificar se o salão está favoritado
    const favoritos = JSON.parse(localStorage.getItem('favoritos') || '[]');
    const isFavoritado = favoritos.some(fav => fav.id === salao.id);
    
    if (isFavoritado) {
        favoritoBtn.style.color = '#d46b6b';
        favoritoBtn.style.borderColor = '#d46b6b';
        favoritoBtn.style.textShadow = '0 0 5px rgba(212, 107, 107, 0.5)';
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
function toggleFavorito(salao, btnElement) {
    console.log('🔧 SaloesJS: Alternando favorito para salão:', salao.nome);
    
    const favoritos = JSON.parse(localStorage.getItem('favoritos') || '[]');
    const salaoIndex = favoritos.findIndex(fav => fav.id === salao.id);
    
    if (salaoIndex === -1) {
        // Adicionar aos favoritos
        favoritos.push({
            id: salao.id,
            nome: salao.nome,
            imagemUrl: salao.imagemUrl || 'images/logo.png',
            endereco: salao.endereco || '',
            telefone: salao.telefone || '',
            email: salao.email || '',
            dataFavoritado: new Date().toISOString()
        });
        
        // Atualizar visual do botão
        btnElement.style.color = '#d46b6b';
        btnElement.style.borderColor = '#d46b6b';
        btnElement.style.textShadow = '0 0 5px rgba(212, 107, 107, 0.5)';
        
        console.log('✅ SaloesJS: Salão adicionado aos favoritos');
    } else {
        // Remover dos favoritos
        favoritos.splice(salaoIndex, 1);
        
        // Atualizar visual do botão
        btnElement.style.color = '#ccc';
        btnElement.style.borderColor = '#ccc';
        btnElement.style.textShadow = 'none';
        
        console.log('✅ SaloesJS: Salão removido dos favoritos');
    }
    
    // Salvar no localStorage
    localStorage.setItem('favoritos', JSON.stringify(favoritos));
    console.log('💾 SaloesJS: Favoritos salvos no localStorage');
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
        saloesFiltrados.forEach(salao => {
            const card = criarCardSalao(salao);
            container.appendChild(card);
        });
        
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