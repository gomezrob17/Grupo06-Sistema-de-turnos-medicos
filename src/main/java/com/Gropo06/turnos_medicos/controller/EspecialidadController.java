package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empleado/especialidades")
public class EspecialidadController {

	private final EspecialidadRepository especialidadRepo;
	
	@Autowired
	private AgendaRepository agendaRepo;

	public EspecialidadController(EspecialidadRepository especialidadRepo) {
		this.especialidadRepo = especialidadRepo;
	}

	// Muestra la lista de especialidades + formulario (vacío o con info) para
	// crear/editar.

	@GetMapping
	public String listarEspecialidades(Model model) {
		List<Especialidad> lista = especialidadRepo.findAll();
		model.addAttribute("especialidades", lista);
		model.addAttribute("especialidadForm", new Especialidad());
		return "empleado/especialidades";
	}

	// Guarda nueva especialidad o actualiza una ya existente
	// Si el campo idEspecialidad es null, se crea si no, se actualiza

	@PostMapping("/save")
	public String guardarEspecialidad(@ModelAttribute("especialidadForm") Especialidad esp) {
		if (especialidadRepo.existsByNombre(esp.getNombre())) {
		    throw new CustomException("Nombre Duplicado");
		}
		especialidadRepo.save(esp);
		return "redirect:/empleado/especialidades";
	}

	// Cargar datos de la especialidad seleccionada para edición

	@GetMapping("/edit/{id}")
	public String editarEspecialidad(@PathVariable("id") Long id, Model model) {
		Especialidad esp = especialidadRepo.findById(id)
				.orElseThrow(() -> new CustomException("Especialidad inválida Id:" + id));
		model.addAttribute("especialidadForm", esp);

		// Recargamos la lista
		List<Especialidad> lista = especialidadRepo.findAll();
		model.addAttribute("especialidades", lista);

		return "empleado/especialidades";
	}

	// Eliminar una especialidad.

	@GetMapping("/delete/{id}")
	public String eliminarEspecialidad(@PathVariable("id") Long id) {
		if (agendaRepo.existsByTipoEspecialidad_IdEspecialidad(id)) throw new CustomException("Especialidad ya tiene una Agenda asignada.");
		especialidadRepo.deleteById(id);
		return "redirect:/empleado/especialidades";
	}
}
