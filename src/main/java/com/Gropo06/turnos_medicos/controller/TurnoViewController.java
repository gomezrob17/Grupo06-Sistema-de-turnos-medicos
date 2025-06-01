package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.*;
import com.Gropo06.turnos_medicos.repository.PersonaRepository;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import com.Gropo06.turnos_medicos.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

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

    @GetMapping("/turnos")
    public String mostrarTurnos(Model model, Principal principal) {
        String email = principal.getName();
        Usuario persona = personaRepo.findByContactoEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!(persona instanceof Paciente)) {
            throw new RuntimeException("El usuario no es un cliente válido");
        }

        Paciente cliente = (Paciente) persona;

        model.addAttribute("user", cliente);
        model.addAttribute("displayName", cliente.getNombre() + " " + cliente.getApellido());
        model.addAttribute("turno", new Turno());
        model.addAttribute("profesionales", profesionalService.getAll());
        model.addAttribute("especialidades", especialidadService.getAll());
        model.addAttribute("sucursales", sucursalService.getAll());

        // Aquí paso el rol al modelo para Thymeleaf
        String nombreRol = cliente.getRol().getNombre(); // obtenemos el nombre del rol
        model.addAttribute("rol", nombreRol);

        return "turno";
    }

    
}