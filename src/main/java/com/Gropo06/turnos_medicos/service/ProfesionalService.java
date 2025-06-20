package com.Gropo06.turnos_medicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;

@Service
public class ProfesionalService {
    @Autowired private ProfesionalRepository repo;

    public List<Profesional> getAll() {
        return repo.findAll();
    }
    
    public Profesional guardar(Profesional profesional) {
        return repo.save(profesional);
    }
    
    public Profesional save(Profesional profesional) {
        return repo.save(profesional);
    }
    
}
