package com.Gropo06.turnos_medicos.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(NombreDuplicado.class)
    public String manejarNombreDuplicado(NombreDuplicado ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(RuntimeException.class)
    public String manejarRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(SucursalInvalida.class)
    public String manejarSucursalInvalida(SucursalInvalida ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(EspecialidadInvalida.class)
    public String manejarEspecialidadInvalida(EspecialidadInvalida ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(AgendaYaAsignada.class)
    public String manejarAgendaYaAsignada(AgendaYaAsignada ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(ProfesionalYaAsignado.class)
    public String manejarProfesionalYaAsignado(ProfesionalYaAsignado ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(ProfesionalInvalido.class)
    public String manejarProfesionalInvalido(ProfesionalInvalido ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(EmailExistente.class)
    public String manejarEmailExistente(EmailExistente ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(RolNoEncontrado.class)
    public String manejarRolNoEncontrado(RolNoEncontrado ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(TurnoYaAsignado.class)
    public String manejarTurnoYaAsignado(TurnoYaAsignado ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(TurnoInvalido.class)
    public String manejarTurnoInvalido(TurnoInvalido ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(EstadoTurnoInvalido.class)
    public String manejarEstadoTurnoInvalido(EstadoTurnoInvalido ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(PacienteInvalido.class)
    public String manejarPacienteInvalido(PacienteInvalido ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(MatriculaExistente.class)
    public String manejarDniExistente(MatriculaExistente ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }
    
    @ExceptionHandler(DireccionDuplicada.class)
    public String manejarDireccionExistente(DireccionDuplicada ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/surgioError";
    }

    
}
