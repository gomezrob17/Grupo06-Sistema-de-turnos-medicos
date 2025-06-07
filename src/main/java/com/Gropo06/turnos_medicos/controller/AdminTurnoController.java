package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Turno;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
    
	@GetMapping("/delete/{id}")
	public String eliminarTurno(@PathVariable("id") Long id) {
		turnoRepo.deleteById(id);
		return "redirect:/empleado/sucursales";
	}
	
}