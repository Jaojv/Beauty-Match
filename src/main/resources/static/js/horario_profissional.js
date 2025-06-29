 // Abrir modal ao clicar em qualquer botão "Selecionar"
 document.querySelectorAll('.btn-selecionar').forEach(function(btn) {
    btn.addEventListener('click', function(e) {
      e.preventDefault();
      document.getElementById('modal-agendamento').style.display = 'flex';
      // Resetar seleção ao abrir
      document.getElementById('form-agendamento').reset();
      renderizarHorarios();
    });
  });

  // Fechar modal
  document.getElementById('fechar-modal').onclick = function() {
    document.getElementById('modal-agendamento').style.display = 'none';
  };
  window.onclick = function(event) {
    if (event.target == document.getElementById('modal-agendamento')) {
      document.getElementById('modal-agendamento').style.display = 'none';
    }
  };

  // Horários disponíveis (mock)
  const horariosDisponiveis = [
    "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"
  ];
  // Exemplo de horários bloqueados (mock)
  const horariosBloqueados = ["12:00", "15:00"];

  function renderizarHorarios() {
    const lista = document.getElementById('lista-horarios');
    lista.innerHTML = '';
    horariosDisponiveis.forEach(function(horario) {
      const input = document.createElement('input');
      input.type = 'radio';
      input.name = 'horario';
      input.value = horario;
      input.id = 'horario-' + horario;
      input.style.display = 'none';
      if (horariosBloqueados.includes(horario)) {
        input.disabled = true;
      }
      const label = document.createElement('label');
      label.className = 'horario-btn' + (horariosBloqueados.includes(horario) ? ' bloqueado' : '');
      label.htmlFor = input.id;
      label.innerText = horario;
      lista.appendChild(input);
      lista.appendChild(label);
    });
  }

  // Selecionar horário (visual)
  document.getElementById('lista-horarios').addEventListener('click', function(e) {
    if (e.target.classList.contains('horario-btn') && !e.target.classList.contains('bloqueado')) {
      document.querySelectorAll('.horario-btn').forEach(function(btn) {
        btn.classList.remove('selecionado');
      });
      e.target.classList.add('selecionado');
      // Seleciona o input correspondente
      const input = document.getElementById('horario-' + e.target.innerText);
      if (input) input.checked = true;
    }
  });

  // Ao submeter, pode enviar para backend ou redirecionar
  document.getElementById('form-agendamento').onsubmit = function(e) {
    // Aqui você pode manipular os dados antes de enviar
    // e.preventDefault(); // Descomente para testar sem enviar
  };