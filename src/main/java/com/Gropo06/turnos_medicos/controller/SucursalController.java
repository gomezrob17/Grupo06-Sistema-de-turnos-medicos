package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/empleado/sucursales")
public class SucursalController {

	private final SucursalRepository sucursalRepo;

	public SucursalController(SucursalRepository sucursalRepo) {
		this.sucursalRepo = sucursalRepo;
	}

	@GetMapping
	public String listarSucursales(Model model) {
		List<Sucursal> lista = sucursalRepo.findAll();
		model.addAttribute("sucursales", lista);
		model.addAttribute("sucursalForm", new Sucursal());
		return "empleado/sucursales";
	}

	@PostMapping("/save")
	public String guardarSucursal(@ModelAttribute("sucursalForm") Sucursal sucursal) {
		if (sucursalRepo.existsByNombre(sucursal.getNombre())) {
		    throw new CustomException("Nombre Duplicado");
		}
		sucursalRepo.save(sucursal);
		return "redirect:/empleado/sucursales";
	}

	@GetMapping("/edit/{id}")
	public String editarSucursal(@PathVariable("id") Long id, Model model) {
		Sucursal suc = sucursalRepo.findById(id)
				.orElseThrow(() -> new CustomException("Sucursal inválida Id:" + id));
		model.addAttribute("sucursalForm", suc);

		List<Sucursal> lista = sucursalRepo.findAll();
		model.addAttribute("sucursales", lista);

		return "empleado/sucursales";
	}

	@GetMapping("/delete/{id}")
	public String eliminarSucursal(@PathVariable("id") Long id) {
		sucursalRepo.deleteById(id);
		return "redirect:/empleado/sucursales";
	}
}
