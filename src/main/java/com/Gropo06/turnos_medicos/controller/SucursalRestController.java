package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.service.SucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
@Tag(name = "Gestión de Sucursales", description = "Endpoints para crear y ver sucursales")
public class SucursalRestController {

    private final SucursalService sucursalService;

    @Autowired
    public SucursalRestController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @Operation(
        summary = "Crear una nueva sucursal (Empleado/Admin)",
        description = "Crea una nueva sucursal en el sistema. Requiere rol de Empleado o Admin.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SucursalDTO> crearSucursal(@Valid @RequestBody SucursalDTO request) {
        SucursalDTO nueva = sucursalService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(
        summary = "Ver una sucursal por ID (Empleado/Admin)",
        description = "Muestra el detalle de una sucursal por ID. Requiere rol de Empleado o Admin.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SucursalDTO> verSucursal(@PathVariable Long id) {
        SucursalDTO sucursal = sucursalService.findById(id);
        return (sucursal != null)
                ? ResponseEntity.ok(sucursal)
                : ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Obtener todas las sucursales (Empleado/Admin)",
        description = "Devuelve la lista completa de sucursales registradas. Requiere rol de Empleado o Admin.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SucursalDTO>> listarTodas() {
        List<SucursalDTO> lista = sucursalService.getAll();
        return ResponseEntity.ok(lista);
    }
}