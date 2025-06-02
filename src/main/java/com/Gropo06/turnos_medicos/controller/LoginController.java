package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Empleado;
import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        // Busco Usuario (Paciente o Empleado) por email
        Optional<Usuario> optUsuario = usuarioRepo.findByContactoEmail(email);
        if (optUsuario.isEmpty()) {
            model.addAttribute("error", "El Email o la contraseña no son válidos");
            return "login";
        }

        Usuario u = optUsuario.get();

        // Verifico que la contraseña raw coincida con el hash almacenado
        if (!passwordEncoder.matches(password, u.getPassword())) {
            model.addAttribute("error", "El Email o la contraseña no son válidos");
            return "login";
        }

        // Guardo el usuario en sesión
        session.setAttribute("usuarioLogueado", u);

        // Si es Empleado, verifico el Rol
        if (u instanceof Empleado) {
            Empleado emp = (Empleado) u;
            if (emp.getRol().getNombre().equals("ADMIN")) {
                return "redirect:/admin/dashboard";
            }
        }

        // Si es un Paciente envía a su home
        return "redirect:/paciente/home";
    }
}


