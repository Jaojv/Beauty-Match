// Script para controlar o modo escuro/claro da aplicação
// Gerencia a alternância entre temas e salva a preferência do usuário

console.log('🔧 DarkModeJS: Inicializando...');

// Elementos do DOM
const link = document.getElementById('darkmode-botao');
const body = document.body;

// Verifica se o botão de dark mode existe
if (!link) {
    console.error('❌ DarkModeJS: Botão darkmode não encontrado');
} else {
    console.log('✅ DarkModeJS: Botão darkmode encontrado');
}

// Ao carregar a página, verifica o storage
// Restaura a preferência salva do usuário
if (localStorage.getItem('dark-mode') === 'enabled') {
    body.classList.add('dark-mode');
    console.log('🔧 DarkModeJS: Modo escuro ativado do localStorage');
} else {
    console.log('🔧 DarkModeJS: Modo claro ativado');
}

// Configura o event listener para o botão de dark mode
if (link) {
    link.addEventListener('click', (event) => {
        event.preventDefault();
        console.log('🔧 DarkModeJS: Botão darkmode clicado');
        
        // Alterna entre modo claro e escuro
        body.classList.toggle('dark-mode');
        
        // Salva a preferência no localStorage
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