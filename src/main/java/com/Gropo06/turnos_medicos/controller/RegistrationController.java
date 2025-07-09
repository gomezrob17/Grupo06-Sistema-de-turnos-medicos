package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.PacienteDTO;
import com.Gropo06.turnos_medicos.service.PacienteService;
import com.Gropo06.turnos_medicos.service.RolService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

	private final PacienteService pacienteService;
	private final RolService rolService;
	private final PasswordEncoder passwordEncoder;

	public RegistrationController(PacienteService pacienteService, RolService rolService,
			PasswordEncoder passwordEncoder) {
		this.pacienteService = pacienteService;
		this.rolService = rolService;
		this.passwordEncoder = passwordEncoder;
	}

	// Formulario de registro
	@GetMapping("/register")
	public String showRegistrationForm(@RequestParam(value = "success", required = false) boolean success,
			Model model) {
		model.addAttribute("pacienteDto", new PacienteDTO());
		model.addAttribute("registered", success);
		return "registro";
	}

	// Se procesa el envío del form
	@PostMapping("/register")
	public String registerPacient(@ModelAttribute("pacienteDto") PacienteDTO pacienteDto, Model model) {
		// Seteamos el rol PACIENTE (buscado por nombre)
		var rol = rolService.findByName("PACIENTE");
		if (rol == null) {
			throw new RuntimeException("Rol PACIENTE no existe");
		}
		pacienteDto.setRolId(rol.getIdRol());

		// Codificamos la contraseña
		pacienteDto.setPassword(passwordEncoder.encode(pacienteDto.getPassword()));

		// Guardamos por service
		pacienteService.save(pacienteDto);

		return "redirect:/login?registered";
	}
}
