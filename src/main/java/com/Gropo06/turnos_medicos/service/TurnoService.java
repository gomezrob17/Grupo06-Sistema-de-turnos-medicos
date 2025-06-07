package com.Gropo06.turnos_medicos.service;


import com.Gropo06.turnos_medicos.model.Turno;
import java.util.List;

public interface TurnoService {
    void crearTurno(Turno turno);
    List<Turno> obtenerTurnosDisponibles();
    List<Turno> obtenerTodos();
}