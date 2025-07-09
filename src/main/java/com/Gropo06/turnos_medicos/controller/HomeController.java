package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.UsuarioDTO;
import com.Gropo06.turnos_medicos.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({ "/", "/home" })
    public String home(Model model, Principal principal) {
        String email = principal.getName();
        UsuarioDTO usuario = usuarioService.findByEmail(email);

        String nombreMostrar;
        if (usuario != null) {
            nombreMostrar = usuario.getNombre() + " " + usuario.getApellido();
        } else {
            nombreMostrar = "Usuario";
        }
        model.addAttribute("displayName", nombreMostrar);

        return "home";
    }

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
