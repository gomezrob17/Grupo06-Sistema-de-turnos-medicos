document.addEventListener('DOMContentLoaded', () => {
			const formDiv = document.getElementById('form-sucursal');
			const toggleBtn = document.getElementById('toggle-form');
			const cancelBtn = document.getElementById('cancel-form');

			toggleBtn.addEventListener('click', () => {
				formDiv.classList.toggle('d-none');
			});
			cancelBtn.addEventListener('click', () => {
				formDiv.classList.add('d-none');
				formDiv.querySelectorAll('input[type="text"]').forEach(input => input.value = '');
				formDiv.querySelector('input[name="idSucursal"]').value = '';
			});
		});