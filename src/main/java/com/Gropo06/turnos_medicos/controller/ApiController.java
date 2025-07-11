package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.service.SucursalService;

import io.swagger.v3.oas.annotations.Hidden;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    private final SucursalService sucursalService;

    public ApiController(SucursalService sucursalService) {
        this.sucursalService = sucursalService;
    }

    @Hidden
    @GetMapping("/api/sucursales-por-especialidad")
    public List<SucursalDTO> getSucursalesPorEspecialidad(
            @RequestParam("idEspecialidad") Long idEspecialidad) {

        // Usamos el método del Service que retorna DTOs
        return sucursalService.findByEspecialidadId(idEspecialidad);
    }
}
