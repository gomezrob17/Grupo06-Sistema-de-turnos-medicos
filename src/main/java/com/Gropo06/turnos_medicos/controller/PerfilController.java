package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PerfilController {

    private final UsuarioRepository personaRepo;

    public PerfilController(UsuarioRepository personaRepo) {
        this.personaRepo = personaRepo;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Principal principal) {
        // Obtener el email del usuario autenticado
        String email = principal.getName();

        // Buscar la persona por email del contacto
        Usuario persona = personaRepo
            .findByContactoEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Armar nombre completo
        String nombreMostrar = persona.getNombre() + " " + persona.getApellido();

        // Pasar datos al modelo
        model.addAttribute("user", persona); // para el HTML perfil.html
        model.addAttribute("displayName", nombreMostrar); // para el dropdown del header

        return "paciente/perfil"; // nombre del archivo HTML sin extensión
    }
    
    @GetMapping("/grupo-familiar")
    public String grupoFamiliar() {
    	return "error/mantenimiento";
    }
    
    @GetMapping("/resultados")
    public String resultados() {
    	return "error/mantenimiento";
    }
}