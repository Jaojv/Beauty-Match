const link = document.getElementById('darkmode-botao');
const body = document.body;

// Ao carregar a página, verifica o storage
if (localStorage.getItem('dark-mode') === 'enabled') {
  body.classList.add('dark-mode');
}

link.addEventListener('click', (event) => {
  event.preventDefault();
  body.classList.toggle('dark-mode');
  // Salva a preferência
  if (body.classList.contains('dark-mode')) {
    localStorage.setItem('dark-mode', 'enabled');
  } else {
    localStorage.setItem('dark-mode', 'disabled');
  }
});