document.addEventListener('DOMContentLoaded', function () {
	const userBtn = document.querySelector('.dropdown .btn-user');
	const menu = document.querySelector('.dropdown-menu');

	userBtn.addEventListener('click', function (e) {
		e.stopPropagation();
		menu.classList.toggle('show');
	});

	document.addEventListener('click', function () {
		menu.classList.remove('show');
	});
});