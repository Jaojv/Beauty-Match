// Função para criar o card do salão
function criarCardSalao(salao) {
    const card = document.createElement('div');
    card.className = 'card';
    card.style.cursor = 'pointer';
    card.onclick = () => {
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

    return card;
}

// Função para renderizar os salões
async function renderizarSaloes(filtro = '') {
    const container = document.querySelector('#carrossel-container');
    if (!container) return;
    container.innerHTML = '';
    try {
        const saloes = await apiClient.getSaloes();
        const saloesFiltrados = saloes.filter(s => s.nome.toLowerCase().includes(filtro.toLowerCase()));
        if (saloesFiltrados.length === 0) {
            container.innerHTML = '<p>Nenhum salão encontrado.</p>';
            return;
        }
        saloesFiltrados.forEach(salao => {
            container.appendChild(criarCardSalao(salao));
        });
    } catch (e) {
        container.innerHTML = '<p>Erro ao carregar salões.</p>';
    }
}

// Função para configurar a barra de pesquisa
function configurarBuscaSaloes() {
    const searchBar = document.querySelector('.search-bar');
    if (!searchBar) return;
    searchBar.addEventListener('input', (e) => {
        renderizarSaloes(e.target.value);
    });
}

// Inicialização automática ao carregar a página
window.addEventListener('DOMContentLoaded', () => {
    renderizarSaloes();
    configurarBuscaSaloes();
}); 