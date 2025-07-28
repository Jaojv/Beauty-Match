// Script para controlar o menu mobile da navbar
// Gerencia a abertura e fechamento do menu responsivo

// Elementos do DOM
const menuIcon = document.getElementById('menu-icon');
const menu = document.getElementById('menu');

// Event listener para o ícone do menu
// Alterna entre abrir e fechar o menu com animação
menuIcon.addEventListener('click', () => {
    if (menu.style.display === 'block') {
        // Fecha o menu com animação de fade out
        menu.style.opacity = '0';
        menuIcon.classList.remove('active');
        setTimeout(() => {
            menu.style.display = 'none';
        }, 300);
    } else {
        // Abre o menu com animação de fade in
        menu.style.display = 'block';
        setTimeout(() => {
            menu.style.opacity = '1';
            menuIcon.classList.add('active');
        }, 10);
    }
});