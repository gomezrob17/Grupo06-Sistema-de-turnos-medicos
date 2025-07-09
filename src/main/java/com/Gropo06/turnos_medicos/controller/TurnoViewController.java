package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.PacienteDTO;
import com.Gropo06.turnos_medicos.dto.TurnoDTO;
import com.Gropo06.turnos_medicos.service.PacienteService;
import com.Gropo06.turnos_medicos.service.TurnoService;
import com.Gropo06.turnos_medicos.service.ProfesionalService;
import com.Gropo06.turnos_medicos.service.EspecialidadService;
import com.Gropo06.turnos_medicos.service.SucursalService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class TurnoViewController {

	private final PacienteService pacienteService;
	private final TurnoService turnoService;
	private final ProfesionalService profesionalService;
	private final EspecialidadService especialidadService;
	private final SucursalService sucursalService;

	public TurnoViewController(PacienteService pacienteService, TurnoService turnoService,
			ProfesionalService profesionalService, EspecialidadService especialidadService,
			SucursalService sucursalService) {
		this.pacienteService = pacienteService;
		this.turnoService = turnoService;
		this.profesionalService = profesionalService;
		this.especialidadService = especialidadService;
		this.sucursalService = sucursalService;
	}

	@GetMapping("/turno")
	public String mostrarTurno(Model model, Principal principal) {
		// 1) Buscamos al paciente logueado como DTO
		PacienteDTO paciente = pacienteService.findByEmail(principal.getName());

		// 2) Datos del usuario para la cabecera
		model.addAttribute("user", paciente);
		model.addAttribute("displayName", paciente.getNombre() + " " + paciente.getApellido());

		// 3) Listas para el formulario de reserva
		model.addAttribute("profesionales", profesionalService.getAll());
		model.addAttribute("especialidades", especialidadService.getAll());
		model.addAttribute("sucursales", sucursalService.getAll());

		// 4) Turnos ya reservados por este paciente
		List<TurnoDTO> misTurnos = turnoService.findByPacienteId(paciente.getIdUsuario());
		model.addAttribute("misTurnos", misTurnos);

		return "paciente/turno";
	}
}
