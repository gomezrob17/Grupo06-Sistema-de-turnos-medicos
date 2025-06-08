package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Turno;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;

@Service
public class TurnoServiceImpl implements TurnoService {

    @Autowired private TurnoRepository turnoRepository;
    @Autowired private EstadoTurnoService estadoTurnoService;
    

    @Override
    public void crearTurno(Turno turno) {
        turno.setEstado(estadoTurnoService.obtenerPorNombre("Disponible"));
        turnoRepository.save(turno);
    }
    

    @Override
    public void cancelarTurno(Long idTurno) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new CustomException("Turno no encontrado"));

        EstadoTurno cancelado = estadoTurnoService.obtenerEstadoCancelado();

        turno.setEstado(cancelado);
        turnoRepository.save(turno);
    }

    @Override
    public List<Turno> obtenerTurnosDisponibles() {
        return turnoRepository.findByEstadoNombre("Disponible");
    }

    @Override
    public List<Turno> obtenerTodos() {
        return turnoRepository.findAll();
    }
    
    public void guardar(Turno turno) {
        turnoRepository.save(turno);
    }
    
}