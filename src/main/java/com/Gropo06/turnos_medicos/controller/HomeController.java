package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

	private final UsuarioRepository usuarioRepo;

	public HomeController(UsuarioRepository usuarioRepo) {
		this.usuarioRepo = usuarioRepo;
	}

	@GetMapping({ "/", "/home" })
	public String home(Model model, Principal principal) {
		// recupero el email
		String email = principal.getName();

		// busco la Usuario por email
		Usuario us = usuarioRepo.findByContactoEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// armo el nombre completo
		String nombreMostrar = us.getNombre() + " " + us.getApellido();

		// lo asigno en el model
		model.addAttribute("displayName", nombreMostrar);

		return "home";
	}
	
	// Vistas que aun no estan disponibles
	
	@GetMapping("/ayuda")
	public String ayuda() {
		return "error/mantenimiento";
	}
	
    @GetMapping("/atencion")
    public String atencion() {
    	return "error/mantenimiento";
    }
    
    @GetMapping("/hospital")
    public String hospital() {
    	return "error/mantenimiento";
    }
}
