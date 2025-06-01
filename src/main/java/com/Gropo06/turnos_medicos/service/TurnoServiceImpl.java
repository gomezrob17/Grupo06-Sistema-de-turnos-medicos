package com.Gropo06.turnos_medicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.Turno;
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
    public List<Turno> obtenerTurnosDisponibles() {
        return turnoRepository.findByEstadoNombre("Disponible");
    }

    @Override
    public List<Turno> obtenerTodos() {
        return turnoRepository.findAll();
    }
}