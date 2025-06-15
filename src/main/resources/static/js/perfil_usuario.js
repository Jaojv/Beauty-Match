const botaoPerfil = document.querySelector('.botao_logado');
const modalPerfil = document.getElementById('modal-perfil');
const fecharModal = document.getElementById('fechar-modal');

// Abrir modal ao clicar no perfil
botaoPerfil.addEventListener('click', function(e) {
    e.preventDefault();
    modalPerfil.style.display = 'flex';
});

// Fechar modal ao clicar no X
fecharModal.addEventListener('click', function() {
    modalPerfil.style.display = 'none';
});

// Fechar modal ao clicar fora do conteúdo
window.addEventListener('click', function(e) {
    if (e.target === modalPerfil) {
        modalPerfil.style.display = 'none';
    }
});