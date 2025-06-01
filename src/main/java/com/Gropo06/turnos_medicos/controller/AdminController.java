package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.*;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import com.Gropo06.turnos_medicos.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/adminturno")
    public String adminTurno(Model model, Principal principal) {
        String email = principal.getName(); // Email del usuario logueado
        Optional<Usuario> usuarioOpt = usuarioRepository.findByContactoEmail(email);

        if (usuarioOpt.isPresent()) {
            model.addAttribute("user", usuarioOpt.get());
        } else {
            return "redirect:/login?error";
        }

        // Aquí agregamos el objeto turno vacío para el formulario
        model.addAttribute("turno", new Turno());

        return "adminturno";
    }
}