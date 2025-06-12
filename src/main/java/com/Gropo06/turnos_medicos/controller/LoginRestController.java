package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.LoginRequest;
import com.Gropo06.turnos_medicos.dto.LoginResponse;
import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginRestController {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req, HttpSession session) {

		// Buscamos el usuario por email
		var opt = usuarioRepo.findByContactoEmail(req.email());
		if (opt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponse("Email o contraseña inválidos"));
		}
		Usuario u = opt.get();

		// Verificamos su contraseña
		if (!passwordEncoder.matches(req.password(), u.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginResponse("Email o contraseña inválidos"));
		}

		// Guardamos sesión
		session.setAttribute("usuarioLogueado", u);

		// Respuesta
		return ResponseEntity.ok(new LoginResponse("Login exitoso"));
	}
}
