package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.EmpleadoDTO;
import com.Gropo06.turnos_medicos.dto.UsuarioDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Empleado;
import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/login")
	public String mostrarLogin(Model model) {
	    model.addAttribute("usuarioDTO", new UsuarioDTO());
	    return "login";
	}

	@PostMapping("/login")
	public String procesarLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpSession session, Model model) {

		// Buscamos usuario por email
		Optional<Usuario> optUsuario = usuarioRepo.findByContactoEmail(email);
		if (optUsuario.isEmpty()) {
			model.addAttribute("error", "El Email o la contraseña no son válidos");
			return "login";
		}

		Usuario usuario = optUsuario.get();

		// Validamos la contraseña
		if (!passwordEncoder.matches(password, usuario.getPassword())) {
			model.addAttribute("error", "El Email o la contraseña no son válidos");
			return "login";
		}

		// Convertimos a DTO para la sesión
		UsuarioDTO usuarioDto = MapperUtil.toDto(usuario);
		session.setAttribute("usuarioLogueado", usuarioDto);

		// Si es Empleado (ADMIN) redirigimos a admin/dashboard
		if (usuario instanceof Empleado empleado) {
			EmpleadoDTO empDto = MapperUtil.toDto(empleado);
			if (empDto.getRolId() != null && empDto.getRolId().equals(1L)) {
				session.setAttribute("usuarioLogueado", empDto);
				return "redirect:/admin/dashboard";
			}
		}

		// Si es Paciente envia a su Home
		return "redirect:/paciente/home";
	}
}
