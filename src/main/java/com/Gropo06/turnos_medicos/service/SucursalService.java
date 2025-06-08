package com.Gropo06.turnos_medicos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;

@Service
public class SucursalService {
    @Autowired private SucursalRepository repo;

    public List<Sucursal> getAll() {
        return repo.findAll();
    }
    
    public List<Sucursal> buscarSucursalesPorIds(List<Long> ids) {
        return repo.findAllById(ids);
    }
}
