package com.Gropo06.turnos_medicos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /** Muestro el form de login, Spring busca login.html en src/main/resources/templates/*/
	
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

