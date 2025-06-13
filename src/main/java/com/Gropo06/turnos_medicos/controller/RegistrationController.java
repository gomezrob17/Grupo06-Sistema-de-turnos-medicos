package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Rol;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.RolRepository;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final PacienteRepository pacienteRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(PacienteRepository pacienteRepo, RolRepository rolRepo,
                                  PasswordEncoder passwordEncoder) {
        this.pacienteRepo = pacienteRepo;
        this.rolRepo = rolRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Mostrar formulario de registro
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("paciente")) {
            model.addAttribute("paciente", new Paciente());
        }
        return "registro";
    }

    // Procesar registro
    @PostMapping("/register")
    public String registerPacient(@ModelAttribute("paciente") Paciente paciente, Model model) {
        try {
            // Verificamos que no haya un paciente con mismo DNI (opcional, pero recomendado)
            if (pacienteRepo.findByDni(paciente.getDni()).isPresent()) {
                model.addAttribute("error", "Ya existe un paciente con ese DNI");
                return "registro";  // vuelve al formulario mostrando error
            }

            // Buscamos el rol PACIENTE
            Rol rolPaciente = rolRepo.findByNombre("PACIENTE")
                    .orElseThrow(() -> new RuntimeException("Rol PACIENTE no existe"));
            paciente.setRol(rolPaciente);

            // Codificamos la contraseña
            paciente.setPassword(passwordEncoder.encode(paciente.getPassword()));

            // Guardamos paciente en DB
            pacienteRepo.save(paciente);

            // Redirigimos al login con parámetro para mostrar mensaje
            return "redirect:/login?registered";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error interno al registrar paciente");
            return "registro";
        }
    }
}