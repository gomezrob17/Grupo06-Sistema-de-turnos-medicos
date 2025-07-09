package com.Gropo06.turnos_medicos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Gropo06.turnos_medicos.dto.TurnoDTO;
import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.dto.PacienteDTO;
import com.Gropo06.turnos_medicos.dto.ProfesionalDTO;
import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.service.TurnoService;
import com.Gropo06.turnos_medicos.service.PacienteService;
import com.Gropo06.turnos_medicos.service.ProfesionalService;
import com.Gropo06.turnos_medicos.service.AgendaService;
import com.Gropo06.turnos_medicos.service.EspecialidadService;

@Controller
@RequestMapping("/empleado/turnos")
public class AdminTurnoController {

	private final TurnoService turnoService;
	private final PacienteService pacienteService;
	private final ProfesionalService profesionalService;
	private final EspecialidadService especialidadService;
	private final AgendaService agendaService;

	public AdminTurnoController(TurnoService turnoService, PacienteService pacienteService,
			ProfesionalService profesionalService, EspecialidadService especialidadService, AgendaService agendaService) {
		this.turnoService = turnoService;
		this.pacienteService = pacienteService;
		this.profesionalService = profesionalService;
		this.especialidadService = especialidadService;
		this.agendaService=agendaService; 
	}

	// Mostramos la lista de DTOs en la vista
	@GetMapping
	public String mostrarVistaAdminTurnos(Model model) {
		List<TurnoDTO> turnos = turnoService.obtenerTodos();
	    List<PacienteDTO> pacientes = pacienteService.getAll();
	    List<ProfesionalDTO> profesionales = profesionalService.getAll();
	    List<EspecialidadDTO> especialidades = especialidadService.getAll();
	    List<Agenda> agendas = agendaService.getAll();

	    model.addAttribute("turnos", turnos);
	    model.addAttribute("pacientes", pacientes);
	    model.addAttribute("profesionales", profesionales);
	    model.addAttribute("especialidades", especialidades);
	    model.addAttribute("agendas", agendas);

	    return "empleado/adminturnos";
	}
	// Eliminamos un turno (lo marcamos como cancelado)
	@PostMapping("/eliminar")
	public String eliminarTurno(@RequestParam("idTurno") Long idTurno) {
	    turnoService.eliminarPorId(idTurno);
	    return "redirect:/empleado/turnos";
	}
}
