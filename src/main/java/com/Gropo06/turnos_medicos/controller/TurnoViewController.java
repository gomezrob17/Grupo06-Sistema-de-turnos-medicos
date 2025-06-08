package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.*;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import com.Gropo06.turnos_medicos.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
public class TurnoViewController {

    private final UsuarioRepository personaRepo;
    private final TurnoService turnoService;
    private final ProfesionalService profesionalService;
    private final EspecialidadService especialidadService;
    private final SucursalService sucursalService;

    public TurnoViewController(
    	UsuarioRepository personaRepo,
        TurnoService turnoService,
        ProfesionalService profesionalService,
        EspecialidadService especialidadService,
        SucursalService sucursalService
    ) {
        this.personaRepo = personaRepo;
        this.turnoService = turnoService;
        this.profesionalService = profesionalService;
        this.especialidadService = especialidadService;
        this.sucursalService = sucursalService;
    }

    @GetMapping("/turno")
    public String mostrarTurnos(Model model, Principal principal) {
        String email = principal.getName();
        Usuario usuario = personaRepo.findByContactoEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        model.addAttribute("user", usuario);  // Pasamos Usuario, no Paciente
        model.addAttribute("displayName", usuario.getNombre() + " " + usuario.getApellido());
        model.addAttribute("turno", new Turno());
        model.addAttribute("profesionales", profesionalService.getAll());
        model.addAttribute("especialidades", especialidadService.getAll());
        model.addAttribute("sucursales", sucursalService.getAll());

        // Agregamos los turnos según el rol
        if ("ADMIN".equals(usuario.getRol().getNombre())) {
            model.addAttribute("turnos", turnoService.obtenerTodos()); // Admin ve todos
        } else {
            // Aseguramos que sea un Paciente
            if (!(usuario instanceof Paciente)) {
                throw new RuntimeException("El usuario no es un cliente válido");
            }
            
            Paciente paciente = (Paciente) usuario;
            model.addAttribute("turno", paciente.getTurnos()); // El paciente ve solo los suyos

        }

        return "paciente/turno";
    }
    
}