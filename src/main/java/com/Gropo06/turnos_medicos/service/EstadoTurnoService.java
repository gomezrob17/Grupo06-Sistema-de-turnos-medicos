package com.Gropo06.turnos_medicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;

@Service
public class EstadoTurnoService {

    @Autowired
    private EstadoTurnoRepository estadoTurnoRepository;

    public EstadoTurno obtenerPorNombre(String nombre) {
        return estadoTurnoRepository.findByNombre(nombre)
            .orElseThrow(() -> new RuntimeException("EstadoTurno no encontrado: " + nombre));
    }
    
    public EstadoTurno obtenerEstadoCancelado() {
        return obtenerPorNombre("CANCELADO");
    }

    public List<EstadoTurno> listarTodos() {
        return estadoTurnoRepository.findAll();
    }
}