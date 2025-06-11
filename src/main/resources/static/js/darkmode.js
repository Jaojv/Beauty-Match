const link = document.getElementById('darkmode-botao');
const body = document.body;

link.addEventListener('click', (event) => {
  event.preventDefault(); // Impede o comportamento padrão de link (navegação)
  body.classList.toggle('dark-mode');
});
