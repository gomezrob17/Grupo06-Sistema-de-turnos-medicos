document.addEventListener('DOMContentLoaded', () => {
			const formDiv = document.getElementById('form-profesional');
			const toggleBtn = document.getElementById('toggle-form');
			const cancelBtn = document.getElementById('cancel-form');

			toggleBtn.addEventListener('click', () => {
				formDiv.classList.toggle('d-none');
			});
			cancelBtn.addEventListener('click', () => {
				formDiv.classList.add('d-none');
				formDiv.querySelectorAll('input[type="text"], input[type="date"], input[type="email"], input[type="number"], input[type="time"]').forEach(input => input.value = '');
				formDiv.querySelectorAll('select').forEach(sel => sel.selectedIndex = -1);
				formDiv.querySelector('input[name="idUsuario"]').value = '';
				const tbodyDisp = document.querySelector('#disp-table tbody');
				tbodyDisp.querySelectorAll('tr').forEach(row => row.remove());
			});

			// Reindexar filas de disponibilidad después de eliminar o agregar la misma
			function reindexDisp() {
				const rows = Array.from(document.querySelectorAll('#disp-table tbody tr'));
				rows.forEach((row, i) => {
					row.id = 'disp-row-' + i;
					const hidden = row.querySelector('input[type="hidden"]');
					if (hidden) hidden.name = `disponibilidades[${i}].idDisponibilidad`;
					const sucSelect = row.querySelector('select[name*=".sucursal.idSucursal"]');
					if (sucSelect) sucSelect.name = `disponibilidades[${i}].sucursal.idSucursal`;
					const fechaIn = row.querySelector('input[type="date"][name*=".fecha"]');
					if (fechaIn) fechaIn.name = `disponibilidades[${i}].fecha`;
					const horaIn = row.querySelector('input[type="time"][name*=".horaInicio"]');
					if (horaIn) horaIn.name = `disponibilidades[${i}].horaInicio`;
					const horaFn = row.querySelector('input[type="time"][name*=".horaFin"]');
					if (horaFn) horaFn.name = `disponibilidades[${i}].horaFin`;
				});
			}

			document.getElementById('add-disp').addEventListener('click', () => {
				const tbody = document.querySelector('#disp-table tbody');
				const idx = tbody.querySelectorAll('tr').length;
				const newRow = document.createElement('tr');
				newRow.id = 'disp-row-' + idx;

				const sucursalesOptions = document.getElementById('tpl-suc-options').innerHTML;

				newRow.innerHTML = `
          <input type="hidden" name="disponibilidades[${idx}].idDisponibilidad" value="" />

          <!-- Selector Sucursal -->
          <td>
            <select class="form-select" name="disponibilidades[${idx}].sucursal.idSucursal" required>
              ${sucursalesOptions}
            </select>
          </td>

          <!-- Input Fecha -->
          <td>
            <input type="date" class="form-control" name="disponibilidades[${idx}].fecha" required />
          </td>

          <!-- Input Hora Inicio -->
          <td>
            <input type="time" class="form-control" name="disponibilidades[${idx}].horaInicio" required />
          </td>

          <!-- Input Hora Fin -->
          <td>
            <input type="time" class="form-control" name="disponibilidades[${idx}].horaFin" required />
          </td>

          <!-- Botón Eliminar -->
          <td>
            <button type="button" class="btn btn-sm btn-danger btn-remove-disp">Eliminar</button>
          </td>
        `;
				tbody.appendChild(newRow);
			});

			document.addEventListener('click', (e) => {
				if (e.target.matches('.btn-remove-disp')) {
					const row = e.target.closest('tr');
					if (row) {
						row.remove();
						reindexDisp();
					}
				}
			});
		});