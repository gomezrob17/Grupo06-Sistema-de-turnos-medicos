package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.exceptions.AgendaYaAsignada;
import com.Gropo06.turnos_medicos.exceptions.EspecialidadInvalida;
import com.Gropo06.turnos_medicos.exceptions.NombreDuplicado;
import com.Gropo06.turnos_medicos.exceptions.ProfesionalYaAsignado;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.service.EspecialidadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empleado/especialidades")
public class EspecialidadController {

	private final EspecialidadService especialidadService;
	private final EspecialidadRepository especialidadRepo;
	private final AgendaRepository agendaRepo;
	private final ProfesionalRepository profesionalRepo;

	@Autowired
	public EspecialidadController(EspecialidadService especialidadService, EspecialidadRepository especialidadRepo,
			AgendaRepository agendaRepo, ProfesionalRepository profesionalRepo) {
		this.especialidadService = especialidadService;
		this.especialidadRepo = especialidadRepo;
		this.agendaRepo = agendaRepo;
		this.profesionalRepo = profesionalRepo;
	}

	// Listamos todos los DTOs y preparamos un DTO vacio para el formulario
	@GetMapping
	public String listarEspecialidades(Model model) {
		List<EspecialidadDTO> lista = especialidadService.getAll();
		model.addAttribute("especialidades", lista);
		model.addAttribute("especialidadDto", new EspecialidadDTO());
		return "empleado/especialidades";
	}

	// Guarda o actualiza una Especialidad a partir del DTO
	@PostMapping("/save")
	public String guardarEspecialidad(@ModelAttribute("especialidadDto") EspecialidadDTO dto) {
		Especialidad existente = especialidadRepo.findByNombre(dto.getNombre());
		if (existente != null) {
			if (dto.getIdEspecialidad() == null || !existente.getIdEspecialidad().equals(dto.getIdEspecialidad())) {
				throw new NombreDuplicado("Nombre Duplicado");
			}
		}
		especialidadService.save(dto);
		return "redirect:/empleado/especialidades";
	}

	// Cargamos un DTO en el form para editar
	@GetMapping("/edit/{id}")
	public String editarEspecialidad(@PathVariable("id") Long id, Model model) {
		EspecialidadDTO dto = especialidadService.buscarEspecialidadPorId(id);
		if (dto == null) {
			throw new EspecialidadInvalida("Especialidad inválida Id:" + id);
		}
		model.addAttribute("especialidadDto", dto);
		model.addAttribute("especialidades", especialidadService.getAll());
		return "empleado/especialidades";
	}

	// Eliminamos la Especialidad (validando integridad)
	@GetMapping("/delete/{id}")
	public String eliminarEspecialidad(@PathVariable("id") Long id) {
		if (agendaRepo.existsByTipoEspecialidad_IdEspecialidad(id)) {
			throw new AgendaYaAsignada("Especialidad ya tiene una Agenda asignada.");
		}
		if (profesionalRepo.existsByEspecialidad_IdEspecialidad(id)) {
			throw new ProfesionalYaAsignado("Especialidad ya tiene Profesionales asignados.");
		}
		especialidadService.deleteById(id);
		return "redirect:/empleado/especialidades";
	}
}
