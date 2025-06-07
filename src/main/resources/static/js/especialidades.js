document.addEventListener('DOMContentLoaded', () => {
			const formDiv = document.getElementById('form-especialidad');
			const toggleBtn = document.getElementById('toggle-form');
			const cancelBtn = document.getElementById('cancel-form');

			toggleBtn.addEventListener('click', () => {
				formDiv.classList.toggle('d-none');
			});
			cancelBtn.addEventListener('click', () => {
				formDiv.classList.add('d-none');
				formDiv.querySelectorAll('input[type="text"], input[type="number"]').forEach(input => input.value = '');
				// Reinicia el Id para que el formulario quede en modo de creación
				formDiv.querySelector('input[name="idEspecialidad"]').value = '';
			});
		});