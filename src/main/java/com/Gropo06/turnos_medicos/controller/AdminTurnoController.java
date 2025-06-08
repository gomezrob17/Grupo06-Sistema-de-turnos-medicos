package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Turno;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminTurnoController {

    @Autowired
    private TurnoRepository turnoRepo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private ProfesionalRepository profesionalRepo;

    @Autowired
    private EspecialidadRepository especialidadRepo;
    
    @Autowired
    private AgendaRepository agendaRepo;
    
    @Autowired
    private EstadoTurnoRepository estadoTurnoRepo;

    @GetMapping("/empleado/turnos")
    public String mostrarVistaAdminTurnos(Model model) {
        List<Turno> turnos = turnoRepo.findAll(); 
        System.out.println("Turnos encontrados: " + turnos.size());

        List<Paciente> pacientes = pacienteRepo.findAll();
        List<Profesional> profesionales = profesionalRepo.findAll();
        List<Especialidad> especialidades = especialidadRepo.findAll();

        model.addAttribute("turnos", turnos);
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("profesionales", profesionales);
        model.addAttribute("especialidades", especialidades);

        return "empleado/adminturnos";
    }
    
    @PostMapping("/empleado/eliminar")
    public String eliminarTurno(@RequestParam("idTurno") Long idTurno) {
        Optional<Turno> optionalTurno = turnoRepo.findById(idTurno);

        if (optionalTurno.isPresent()) {
            Turno turno = optionalTurno.get();

            Agenda agenda = turno.getAgenda();
            if (agenda != null) {
                agenda.getTurnos().remove(turno);
                agendaRepo.save(agenda);
            }

            Paciente paciente = turno.getPaciente();
            if (paciente != null) {
                paciente.getTurnos().remove(turno);
                pacienteRepo.save(paciente);
            }

            EstadoTurno estado = turno.getEstado();
            if (estado != null) {
                estado.getTurnos().remove(turno);
                estadoTurnoRepo.save(estado);
            }
            turnoRepo.delete(turno);
        }

        return "redirect:/empleado/turnos";
    }
    
}