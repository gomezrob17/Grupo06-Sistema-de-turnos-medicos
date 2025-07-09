package com.Gropo06.turnos_medicos.service;

import java.util.List;

import com.Gropo06.turnos_medicos.dto.TurnoDTO;

public interface TurnoService {

    TurnoDTO crearTurno(TurnoDTO turnoDto);
    TurnoDTO cancelarTurno(Long idTurno);
    List<TurnoDTO> obtenerTurnosDisponibles();
    List<TurnoDTO> obtenerTodos();
    List<TurnoDTO> findByPacienteId(Long pacienteId);
    void eliminarPorId(Long idTurno); 
}
