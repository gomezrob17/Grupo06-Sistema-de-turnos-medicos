package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;

@Service
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda findById(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agenda no encontrada: " + id));
    }
    public List<Agenda> getAll() {
        return agendaRepository.findAll();
    }
}