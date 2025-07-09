document.addEventListener('DOMContentLoaded', () => {
  // 1) Listener de AJAX para cargar sucursales
  const selectEsp = document.getElementById('selectEspecialidad');
  const selectSuc = document.getElementById('selectSucursal');

  selectEsp.addEventListener('change', () => {
    const idEsp = selectEsp.value;
    if (!idEsp) {
      selectSuc.innerHTML = '<option value="">-- Elige sucursal --</option>';
      selectSuc.disabled = true;
      return;
    }

    fetch(`/api/sucursales-por-especialidad?idEspecialidad=${idEsp}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Error al obtener sucursales');
        }
        return response.json();
      })
      .then(data => {
        let opciones = '<option value="">-- Elige sucursal --</option>';
        data.forEach(suc => {
          opciones += `<option value="${suc.idSucursal}">${suc.nombre}</option>`;
        });
        selectSuc.innerHTML = opciones;
        selectSuc.disabled = false;
      })
      .catch(err => {
        console.error('Fetch sucursales falló:', err);
        selectSuc.innerHTML = '<option value="">Error al cargar sucursales</option>';
        selectSuc.disabled = true;
      });
  });

  // 2) Lógica para “seleccionar un slot” y mostrar bloque de Guardar Turno
  const sectionGuardar      = document.getElementById('guardadoTurnoSection');
  const btnCancelar         = document.getElementById('btnCancelarSeleccion');
  const textoFecha          = document.getElementById('textoFecha');
  const textoHoraInicio     = document.getElementById('textoHoraInicio');
  const textoEspecialidad   = document.getElementById('textoEspecialidad');
  const textoProfesional    = document.getElementById('textoProfesional');
  const textoSucursal       = document.getElementById('textoSucursal');

  const inputIdProfesional  = document.getElementById('inputIdProfesional');
  const inputIdEspecialidad = document.getElementById('inputIdEspecialidad');
  const inputIdSucursal     = document.getElementById('inputIdSucursal');
  const inputFecha          = document.getElementById('inputFecha');
  const inputHoraInicio     = document.getElementById('inputHoraInicio');

  // Función auxiliar para formatear fecha tipo "2025-06-05" → "05/06/2025"
  function formatearFecha(yyyyMMdd) {
    const partes = yyyyMMdd.split('-');
    return partes[2] + '/' + partes[1] + '/' + partes[0];
  }

  // Handler cuando el usuario hace clic en un slot
  function onClickSlot(evt) {
    const btn = evt.currentTarget;
    // Si ya está seleccionado, lo deseleccionamos y ocultamos sección
    if (btn.classList.contains('slot-seleccionado')) {
      btn.classList.remove('slot-seleccionado');
      sectionGuardar.style.display = 'none';
      return;
    }
    // Si no estaba seleccionado, primero desmarcamos cualquier otro
    document.querySelectorAll('.slot-seleccionado').forEach(el => {
      el.classList.remove('slot-seleccionado');
    });

    // Ahora marcamos este
    btn.classList.add('slot-seleccionado');

    // Obtenemos los data-* del botón
    const idProf    = btn.getAttribute('data-idp');
    const idEsp     = btn.getAttribute('data-idesp');
    const idSuc     = btn.getAttribute('data-idsuc');
    const fechaRaw   = btn.getAttribute('data-fecha');
    const horaIniRaw = btn.getAttribute('data-horaini');

    // Formateamos la fecha para mostrarla
    textoFecha.textContent      = formatearFecha(fechaRaw);
    textoHoraInicio.textContent = horaIniRaw;

    // Obtenemos el nombre del profesional leyendo el <strong> dentro de .card-header
    const cardHeader = btn.closest('.card').querySelector('.card-header');
    textoProfesional.textContent = cardHeader.querySelector('strong').textContent;

    // Para la especialidad, leemos el texto del SELECT de Especialidad
    textoEspecialidad.textContent = selectEsp.options[selectEsp.selectedIndex].text;

    // Para la sucursal, buscamos la opción cuyo value coincide con data-idsuc
    const optSuc = selectSuc.querySelector(`option[value="${idSuc}"]`);
    textoSucursal.textContent = optSuc ? optSuc.textContent : '--';

    // Rellenamos los inputs ocultos del form
    inputIdProfesional.value  = idProf;
    inputIdEspecialidad.value = idEsp;
    inputIdSucursal.value     = idSuc;
    inputFecha.value          = fechaRaw;
    inputHoraInicio.value     = horaIniRaw;

    // Mostramos el bloque “Guardar Turno”
    sectionGuardar.style.display = 'block';
    sectionGuardar.scrollIntoView({ behavior: 'smooth' });
  }

  // Registramos el event listener en todos los botones con clase .slot-button
  document.querySelectorAll('.slot-button').forEach(btn => {
    btn.addEventListener('click', onClickSlot);
  });

  // Botón “Cancelar” para ocultar el bloque y desmarcar seleccionados
  btnCancelar.addEventListener('click', () => {
    sectionGuardar.style.display = 'none';
    document.querySelectorAll('.slot-seleccionado').forEach(el => {
      el.classList.remove('slot-seleccionado');
    });
  });
});