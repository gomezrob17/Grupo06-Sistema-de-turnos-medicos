package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.dto.ProfesionalDTO;
import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sucursales")
@Tag(name = "Sucursales", description = "Operaciones relacionadas con las sucursales médicas")
public class SucursalRestController {

    @Autowired
    private SucursalRepository sucursalRepo;

    @GetMapping
    @Operation(summary = "Obtener todas las sucursales", description = "Devuelve la lista de todas las sucursales registradas")
    public ResponseEntity<List<SucursalDTO>> getAllSucursales() {
        List<Sucursal> lista = sucursalRepo.findAll();
        List<SucursalDTO> dtos = lista.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva sucursal", description = "Agrega una sucursal nueva al sistema si no existe una con el mismo nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sucursal creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Ya existe una sucursal con ese nombre")
    })
    public ResponseEntity<?> createSucursal(@RequestBody SucursalDTO dto) {
        if (sucursalRepo.existsByNombre(dto.getNombre())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe una sucursal con ese nombre.");
        }
        Sucursal sucursal = toEntity(dto);
        Sucursal saved = sucursalRepo.save(sucursal);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por ID", description = "Devuelve una sucursal según su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<?> getSucursalById(
            @Parameter(description = "ID de la sucursal a obtener") @PathVariable Long id) {
        Optional<Sucursal> optionalSucursal = sucursalRepo.findById(id);

        if (optionalSucursal.isPresent()) {
            Sucursal sucursal = optionalSucursal.get();
            SucursalDTO dto = toDto(sucursal);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sucursal no encontrada con ID: " + id);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza los datos de una sucursal existente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<?> updateSucursal(@PathVariable Long id, @RequestBody SucursalDTO dto) {
        Optional<Sucursal> optionalSucursal = sucursalRepo.findById(id);

        if (optionalSucursal.isPresent()) {
            Sucursal sucursal = optionalSucursal.get();
            sucursal.setNombre(dto.getNombre());
            sucursal.setDireccion(dto.getDireccion());

            Sucursal updated = sucursalRepo.save(sucursal);
            SucursalDTO responseDto = toDto(updated);

            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Sucursal no encontrada con ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal por ID si existe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sucursal eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<?> deleteSucursal(@PathVariable Long id) {
        if (!sucursalRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se puede eliminar. Sucursal no encontrada con ID: " + id);
        }
        sucursalRepo.deleteById(id);
        return ResponseEntity.ok("Sucursal eliminada con éxito.");
    }

    // Métodos auxiliares de conversión DTO <-> Entidad
    private SucursalDTO toDto(Sucursal sucursal) {
        SucursalDTO dto = new SucursalDTO();
        dto.setIdSucursal(sucursal.getIdSucursal());
        dto.setNombre(sucursal.getNombre());
        dto.setDireccion(sucursal.getDireccion());

        if (sucursal.getEspecialidades() != null) {
            Set<EspecialidadDTO> especialidadesDTO = sucursal.getEspecialidades()
                    .stream()
                    .map(this::especialidadToDto)
                    .collect(Collectors.toSet());
            dto.setEspecialidades(especialidadesDTO);
        }

        if (sucursal.getProfesionales() != null) {
            Set<ProfesionalDTO> profesionalesDTO = sucursal.getProfesionales()
                    .stream()
                    .map(this::profesionalToDto)
                    .collect(Collectors.toSet());
            dto.setProfesionales(profesionalesDTO);
        }
        return dto;
    }

    private Sucursal toEntity(SucursalDTO dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        return sucursal;
    }

    private EspecialidadDTO especialidadToDto(Especialidad esp) {
        EspecialidadDTO dto = new EspecialidadDTO();
        dto.setIdEspecialidad(esp.getIdEspecialidad());
        dto.setNombre(esp.getNombre());
        return dto;
    }

    private ProfesionalDTO profesionalToDto(Profesional prof) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setIdProfesional(prof.getIdUsuario()); // o getIdProfesional() si tu modelo lo tiene así
        dto.setDni(prof.getDni());
        dto.setNombre(prof.getNombre());
        dto.setApellido(prof.getApellido());
        dto.setMatricula(prof.getMatricula());
        dto.setNombreEspecialidad(
                prof.getEspecialidad() != null ? prof.getEspecialidad().getNombre() : null
        );
        return dto;
    }
}