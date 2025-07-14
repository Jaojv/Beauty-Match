// Função para criar o card do salão
function criarCardSalao(salao) {
    console.log('🔧 SaloesJS: Criando card para salão:', salao.nome);
    const card = document.createElement('div');
    card.className = 'card';
    card.style.cursor = 'pointer';
    card.onclick = () => {
        console.log('🔧 SaloesJS: Card clicado, redirecionando para salão ID:', salao.id);
        window.location.href = `pages/servicos.html?id=${salao.id}`;
    };

    // Imagem do salão
    const img = document.createElement('img');
    img.className = 'card-logo';
    img.src = salao.imagemUrl ? salao.imagemUrl : 'images/logo.png'; // fallback se não houver imagem
    img.alt = `Logo do salão ${salao.nome}`;
    img.style.width = '100%';
    img.style.height = '120px';
    img.style.objectFit = 'cover';
    card.appendChild(img);

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

// Função para renderizar os salões
async function renderizarSaloes(filtro = '') {
    console.log('🔧 SaloesJS: Renderizando salões, filtro:', filtro);
    const container = document.querySelector('#carrossel-container');
    if (!container) {
        console.error('❌ SaloesJS: Container de salões não encontrado');
        return;
    }
    container.innerHTML = '';
    try {
        const saloes = await apiClient.getSaloes();
        console.log('✅ SaloesJS: Salões carregados do backend:', saloes.length);
        
        const saloesFiltrados = saloes.filter(s => s.nome.toLowerCase().includes(filtro.toLowerCase()));
        console.log('🔧 SaloesJS: Salões após filtro:', saloesFiltrados.length);
        
        if (saloesFiltrados.length === 0) {
            container.innerHTML = '<p>Nenhum salão encontrado.</p>';
            console.log('⚠️ SaloesJS: Nenhum salão encontrado');
            return;
        }
        saloesFiltrados.forEach(salao => {
            container.appendChild(criarCardSalao(salao));
        });
        console.log('✅ SaloesJS: Salões renderizados com sucesso');
    } catch (e) {
        console.error('❌ SaloesJS: Erro ao carregar salões:', e);
        container.innerHTML = '<p>Erro ao carregar salões.</p>';
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