package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.service.EspecialidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/especialidades")
public class EspecialidadRestController {

	private final EspecialidadService service;

	public EspecialidadRestController(EspecialidadService service) {
		this.service = service;
	}

	@Operation(summary = "Obtener todas las Especialidades", description = "Devuelve la lista de especialidades registradas. Requiere rol Empleado o Admin.", security = @SecurityRequirement(name = "Bearer Authentication"))
	@PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EspecialidadDTO>> getAll() {
		List<EspecialidadDTO> lista = service.getAll();
		return ResponseEntity.ok(lista);
	}

	@Operation(summary = "Crea una nueva Especialidad", description = "Crea una nueva especialidad en el sistema. Requiere rol Empleado o Admin.", security = @SecurityRequirement(name = "Bearer Authentication"))
	@PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EspecialidadDTO> save(@RequestBody EspecialidadDTO dto) {
		EspecialidadDTO guardada = service.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
	}

	@Operation(summary = "Obtener una Especialidad por ID", description = "Devuelve la especialidad registrada según su ID. Requiere rol Empleado o Admin.", security = @SecurityRequirement(name = "Bearer Authentication"))
	@PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EspecialidadDTO> getById(@PathVariable Long id) {
		EspecialidadDTO dto = service.buscarEspecialidadPorId(id);
		if (dto == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(dto);
	}
}
