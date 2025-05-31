package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Rol;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.RolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegistrationController {

    private final PacienteRepository pacienteRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(PacienteRepository pacienteRepo,RolRepository rolRepo,PasswordEncoder passwordEncoder) {
        this.pacienteRepo = pacienteRepo;
        this.rolRepo = rolRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Form de registro
    @GetMapping("/register")
    public String showRegistrationForm(
            @RequestParam(value="success", required=false) boolean success, Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("registered", success);
        return "registro";
    }

    // Se procesa el envío del form
    @PostMapping("/register")
    public String registerPacient(@ModelAttribute("paciente") Paciente paciente, Model model) {
        // Asigna rol CLIENTE
        Rol rolPaciente = rolRepo.findByNombre("PACIENTE").orElseThrow(() -> new RuntimeException("Rol PACIENTE no existe"));
        paciente.setRol(rolPaciente);

        // Codifica la Contraseña
        paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));

        // Guardamos la info del cliente en BD
        pacienteRepo.save(paciente);

        return "redirect:/login?registered";
    }
}
