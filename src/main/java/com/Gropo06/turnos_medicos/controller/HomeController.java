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

    @GetMapping({"/", "/home"})
    public String home(Model model, Principal principal) {
        // recupero el email 
        String email = principal.getName();

        //busco la Persona por email
        Usuario us = usuarioRepo
            .findByContactoEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //armo el nombre completo
        String nombreMostrar = us.getNombre() 
                             + " " 
                             + us.getApellido();

        //lo asigno en el modelo
        model.addAttribute("displayName", nombreMostrar);

        return "home";
    }
}


