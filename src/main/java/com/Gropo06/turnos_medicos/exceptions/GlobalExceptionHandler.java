package com.Gropo06.turnos_medicos.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CustomException.class)
    public String manejarCustom(CustomException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
}
