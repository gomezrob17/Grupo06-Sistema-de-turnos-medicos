package com.Gropo06.turnos_medicos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Gropo06.turnos_medicos.dto.DisponibilidadDTO;
import com.Gropo06.turnos_medicos.service.DisponibilidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/v1/disponibilidades")
public class DisponibilidadRestController {

	private final DisponibilidadService service;

	public DisponibilidadRestController(DisponibilidadService service) {
		this.service = service;
	}

    @Operation(
            summary = "Obtener todas las Disponibilidades",
            description = "Devuelve la lista de las disponibilidades registradas. Requiere rol Empleado o Admin.",
            security = @SecurityRequirement(name = "Bearer Authentication")
        )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DisponibilidadDTO>> getAll() {
		List<DisponibilidadDTO> lista = service.getAll();
		return ResponseEntity.ok(lista);
	}
    
    @Operation(
            summary = "Crea una nueva Disponibilidad",
            description = "Crea una nueva disponibilidad en el sistema. Requiere rol Empleado o Admin && Un profesional y Sucursal registrado.",
            security = @SecurityRequirement(name = "Bearer Authentication")
        )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DisponibilidadDTO> save(@RequestBody DisponibilidadDTO dto) {
		DisponibilidadDTO guardada = service.save(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
	}

    @Operation(
            summary = "Obtener una id de Disponibilidades",
            description = "Devuelve la disponibilidad registradas segun su ID. Requiere rol Empleado o Admin.",
            security = @SecurityRequirement(name = "Bearer Authentication")
        )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DisponibilidadDTO> getById(@PathVariable Long id) {
		DisponibilidadDTO dto = service.findById(id);
		if (dto == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(dto);
	}
	
}
