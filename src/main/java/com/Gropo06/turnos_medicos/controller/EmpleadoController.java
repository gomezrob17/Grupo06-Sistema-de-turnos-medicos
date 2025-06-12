package com.Gropo06.turnos_medicos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpleadoController {

    @GetMapping("/empleado/dashboard")
    public String mostrarDashboardEmpleado() {
        // Redirección a src/main/resources/templates/dashboard.html
        return "dashboard";
    }
}
