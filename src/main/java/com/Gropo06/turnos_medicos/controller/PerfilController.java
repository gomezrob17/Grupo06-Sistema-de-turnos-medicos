package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.PacienteDTO;
import com.Gropo06.turnos_medicos.service.PacienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PerfilController {

    private final PacienteService pacienteService;

    public PerfilController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Principal principal) {
        // Obtener el email del usuario autenticado
        String email = principal.getName();

        // Buscar el paciente por email (DTO)
        PacienteDTO paciente = pacienteService.findByEmail(email);

        // Nombre completo
        String nombreMostrar = paciente.getNombre() + " " + paciente.getApellido();

        // Pasar datos al modelo
        model.addAttribute("pacienteDTO", paciente); 
        model.addAttribute("displayName", nombreMostrar);

        return "paciente/perfil";
    }

    // Vistas que aun no estan disponibles

    @GetMapping("/grupo-familiar")
    public String grupoFamiliar() {
        return "error/mantenimiento";
    }

    @GetMapping("/resultados")
    public String resultados() {
        return "error/mantenimiento";
    }
}
