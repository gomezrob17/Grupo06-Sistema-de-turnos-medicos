package com.Gropo06.turnos_medicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;

@Service
public class EspecialidadService {
    @Autowired private EspecialidadRepository repo;

    public List<Especialidad> getAll() {
        return repo.findAll();
    }
    
    public Especialidad buscarEspecialidadPorId(Long id) {
        return repo.findById(id).orElse(null); // o lanzar una excepción
    }
}