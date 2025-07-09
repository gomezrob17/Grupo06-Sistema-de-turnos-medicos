package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.service.SucursalService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sucursales")
public class SucursalRestController {

    private final SucursalService sucursalService;

    @Autowired
    public SucursalRestController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    // Obtener todas las sucursales
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SucursalDTO>> listarTodas() {
        List<SucursalDTO> lista = sucursalService.getAll();
        return ResponseEntity.ok(lista);
    }

    // Obtener una sucursal por ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SucursalDTO> buscarPorId(@PathVariable Long id) {
        SucursalDTO dto = sucursalService.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // Crear una nueva sucursal
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SucursalDTO> crearSucursal(@Valid @RequestBody SucursalDTO dto) {
        SucursalDTO creada = sucursalService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // Actualizar una sucursal existente
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SucursalDTO> actualizarSucursal(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        dto.setIdSucursal(id);
        SucursalDTO actualizada = sucursalService.save(dto);
        return ResponseEntity.ok(actualizada);
    }

    // Eliminar una sucursal
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Empleado')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        sucursalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}