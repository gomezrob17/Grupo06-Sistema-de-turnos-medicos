package com.Gropo06.turnos_medicos.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.exceptions.NombreDuplicado;
import com.Gropo06.turnos_medicos.exceptions.SucursalInvalida;
import com.Gropo06.turnos_medicos.service.SucursalService;

@Controller
@RequestMapping("/empleado/sucursales")
public class SucursalController {

	private final SucursalService sucursalService;

	public SucursalController(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}

	// Mostramos la lista de sucursales y form vacío para DTO
	@GetMapping
	public String listarSucursales(Model model) {
		List<SucursalDTO> lista = sucursalService.getAll();
		model.addAttribute("sucursales", lista);
		model.addAttribute("sucursalDto", new SucursalDTO());
		return "empleado/sucursales";
	}

	// Guardamos o actualizamos una sucursal via DTO
	@PostMapping("/save")
	public String guardarSucursal(@ModelAttribute("sucursalDto") SucursalDTO dto) {
	    if (sucursalService.existeNombreEnOtraSucursal(dto.getNombre(), dto.getIdSucursal())) {
	        throw new NombreDuplicado("Nombre Duplicado");
	    }
	    sucursalService.save(dto);
	    return "redirect:/empleado/sucursales";
	}

	// Cargamos DTO en el form para editar
	@GetMapping("/edit/{id}")
	public String editarSucursal(@PathVariable("id") Long id, Model model) {
		SucursalDTO dto = sucursalService.findById(id);
		if (dto == null) {
			throw new SucursalInvalida("Sucursal inválida Id:" + id);
		}
		model.addAttribute("sucursalDto", dto);
		model.addAttribute("sucursales", sucursalService.getAll());
		return "empleado/sucursales";
	}

	// Eliminar una sucursal
	@GetMapping("/delete/{id}")
	public String eliminarSucursal(@PathVariable("id") Long id) {
		sucursalService.deleteById(id);
		return "redirect:/empleado/sucursales";
	}
}
