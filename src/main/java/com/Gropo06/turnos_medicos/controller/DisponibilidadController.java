package com.Gropo06.turnos_medicos.controller;

import java.time.LocalDate;
import java.util.List;

import com.Gropo06.turnos_medicos.dto.DisponibilidadDTO;
import com.Gropo06.turnos_medicos.service.DisponibilidadService;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleado/disponibilidades")
public class DisponibilidadController {

	private final DisponibilidadService service;

	public DisponibilidadController(DisponibilidadService service) {
		this.service = service;
	}

	@GetMapping
	public String list(Model model) {
		model.addAttribute("disponibilidades", service.getAll());
		model.addAttribute("dispDto", new DisponibilidadDTO());
		return "empleado/disponibilidades";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute("dispDto") DisponibilidadDTO dto) {
		service.save(dto);
		return "redirect:/empleado/disponibilidades";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		DisponibilidadDTO dto = service.findById(id);
		model.addAttribute("dispDto", dto);
		model.addAttribute("disponibilidades", service.getAll());
		return "empleado/disponibilidades";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		Long profesionalId = service.findById(id).getProfesionalId();
		service.deleteById(id);
		return "redirect:/empleado/profesionales/edit/" + profesionalId;
	}

	@Hidden
	@GetMapping("/api/filtrar")
	@ResponseBody
	public ResponseEntity<List<DisponibilidadDTO>> filtrar(@RequestParam("idEspecialidad") Long idEspecialidad,
			@RequestParam("idSucursal") Long idSucursal,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		List<DisponibilidadDTO> res = service.findByFilter(idEspecialidad, idSucursal, desde, hasta);
		return ResponseEntity.ok(res);
	}
}
