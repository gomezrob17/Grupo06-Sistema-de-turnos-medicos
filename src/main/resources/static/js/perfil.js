
// Dropdown del usuario
document.querySelector(".btn-user").addEventListener("click", e => {
  e.stopPropagation();
  document.querySelector(".dropdown-menu").classList.toggle("show");
});
document.addEventListener("click", () => {
  document.querySelector(".dropdown-menu").classList.remove("show");
});