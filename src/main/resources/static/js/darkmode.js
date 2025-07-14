console.log('🔧 DarkModeJS: Inicializando...');

const link = document.getElementById('darkmode-botao');
const body = document.body;

if (!link) {
    console.error('❌ DarkModeJS: Botão darkmode não encontrado');
} else {
    console.log('✅ DarkModeJS: Botão darkmode encontrado');
}

// Ao carregar a página, verifica o storage
if (localStorage.getItem('dark-mode') === 'enabled') {
    body.classList.add('dark-mode');
    console.log('🔧 DarkModeJS: Modo escuro ativado do localStorage');
} else {
    console.log('🔧 DarkModeJS: Modo claro ativado');
}

if (link) {
    link.addEventListener('click', (event) => {
        event.preventDefault();
        console.log('🔧 DarkModeJS: Botão darkmode clicado');
        body.classList.toggle('dark-mode');
        // Salva a preferência
        if (body.classList.contains('dark-mode')) {
            localStorage.setItem('dark-mode', 'enabled');
            console.log('✅ DarkModeJS: Modo escuro salvo');
        } else {
            localStorage.setItem('dark-mode', 'disabled');
            console.log('✅ DarkModeJS: Modo claro salvo');
        }
    });
    console.log('✅ DarkModeJS: Event listener configurado');
}

console.log('✅ DarkModeJS: Inicializado com sucesso');