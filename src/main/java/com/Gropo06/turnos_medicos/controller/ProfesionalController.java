package com.Gropo06.turnos_medicos.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.Gropo06.turnos_medicos.dto.*;
import com.Gropo06.turnos_medicos.exceptions.*;
import com.Gropo06.turnos_medicos.service.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleado/profesionales")
public class ProfesionalController {

	private final ProfesionalService profesionalService;
	private final EspecialidadService especialidadService;
	private final SucursalService sucursalService;
	private final RolService rolService;
	private final ContactoService contactoService;
	private final PasswordEncoder passwordEncoder;
	private final TurnoService turnoService;

	public ProfesionalController(ProfesionalService profesionalService, EspecialidadService especialidadService,
			SucursalService sucursalService, RolService rolService, ContactoService contactoService,
			PasswordEncoder passwordEncoder, TurnoService turnoService) {
		this.profesionalService = profesionalService;
		this.especialidadService = especialidadService;
		this.sucursalService = sucursalService;
		this.rolService = rolService;
		this.contactoService = contactoService;
		this.passwordEncoder = passwordEncoder;
		this.turnoService = turnoService;
	}

	// Mostramos la lista y form vacío (DTO)
	@GetMapping
	public String listarProfesionales(Model model) {
		model.addAttribute("profesionales", profesionalService.getAll());
		model.addAttribute("profesionalDto", new ProfesionalDTO());
		model.addAttribute("especialidadesList", especialidadService.getAll());
		model.addAttribute("sucursalesList", sucursalService.getAll());
		model.addAttribute("rolesList", rolService.getAll());
		return "empleado/profesionales";
	}

	// Guardamos profesional via DTO
	@PostMapping("/save")
	public String guardarProfesional(
	    @ModelAttribute("profesionalDto") ProfesionalDTO dto,
	    Model model) {
	    if (dto.getFechaNacimiento() == null || dto.getFechaNacimiento().isAfter(LocalDate.now())
	            || dto.getFechaNacimiento().isAfter(LocalDate.now().minusYears(18))) {
	        model.addAttribute("errorFecha", "Fecha de nacimiento inválida");
	        return recargarVista(model, dto);
	    }

	    boolean esEdicion = dto.getIdUsuario() != null;
	    if (esEdicion && turnoService.obtenerTodos().stream()
	            .anyMatch(t -> t.getProfesionalId().equals(dto.getIdUsuario()))) {
	        throw new TurnoYaAsignado("No se puede editar, ya tiene turnos asignados");
	    }

	    if (contactoService.existsByEmail(dto.getContactoEmail())) {
	        Long idUsuarioExistente = contactoService.getIdUsuarioByEmail(dto.getContactoEmail());
	        if (!esEdicion || (idUsuarioExistente != null && !idUsuarioExistente.equals(dto.getIdUsuario()))) {
	            throw new EmailExistente("Email ya registrado");
	        }
	    }
	    
	    List<SucursalDTO> todas = sucursalService.getAll();
	    List<Long> idsSeleccionados = dto.getSucursalIds();

	    List<SucursalDTO> soloSeleccionadas = todas.stream()
	        .filter(s -> idsSeleccionados.contains(s.getIdSucursal()))
	        .toList();

	    // Volvés a cargar el formulario con las sucursales seleccionadas para mostrar solo esas en los selects de disponibilidad
	    model.addAttribute("profesionalDto", dto);
	    model.addAttribute("sucursalesList", soloSeleccionadas);
	    model.addAttribute("todasSucursales", todas);

	    dto.setRolId(rolService.findByName("PROFESIONAL").getIdRol());
	    
	    System.out.println("DISPONIBILIDADES:");
	    for (DisponibilidadDTO d : dto.getDisponibilidades()) {
	        System.out.println("  Día: " + d.getDiaSemana());
	        System.out.println("  Fecha: " + d.getFecha());
	        System.out.println("  Sucursal ID: " + d.getSucursalId());
	    }
	    
	    profesionalService.save(dto);
	    return "redirect:/empleado/profesionales";
	}


	// Cargamos DTO para la edición
	@GetMapping("/edit/{id}")
	public String editarProfesional(@PathVariable Long id, Model model) {
		ProfesionalDTO dto = profesionalService.findById(id);
		if (dto == null)
			throw new ProfesionalInvalido("Profesional inválido Id: " + id);
		if (dto.getDisponibilidades() == null)
	        dto.setDisponibilidades(new ArrayList<>());
		
		model.addAttribute("profesionalDto", dto);
		model.addAttribute("profesionales", profesionalService.getAll());
		model.addAttribute("especialidadesList", especialidadService.getAll());
		model.addAttribute("sucursalesList", sucursalService.getAll());
		model.addAttribute("rolesList", rolService.getAll());
		return "empleado/profesionales";
	}

	// Eliminamos profesional (verifica turnos asociados)
	@GetMapping("/delete/{id}")
	public String eliminarProfesional(@PathVariable Long id) {
		if (turnoService.obtenerTodos().stream().anyMatch(t -> t.getProfesionalId().equals(id))) {
			throw new TurnoYaAsignado("El profesional posee Turnos asignados.");
		}
		profesionalService.deleteById(id);
		return "redirect:/empleado/profesionales";
	}

	// Recargamos la vista en caso de validación fallida
	private String recargarVista(Model model, ProfesionalDTO dto) {
		model.addAttribute("profesionales", profesionalService.getAll());
		model.addAttribute("especialidadesList", especialidadService.getAll());
		model.addAttribute("sucursalesList", sucursalService.getAll());
		model.addAttribute("rolesList", rolService.getAll());
		model.addAttribute("profesionalDto", dto);
		return "empleado/profesionales";
	}
}
