package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final SucursalRepository sucursalRepo;

    public ApiController(SucursalRepository sucursalRepo) {
        this.sucursalRepo = sucursalRepo;
    }

    @GetMapping("/api/sucursales-por-especialidad")
    public List<SucursalDTO> getSucursalesPorEspecialidad(
            @RequestParam("idEspecialidad") Long idEspecialidad) {

        List<Sucursal> listaSucursales = sucursalRepo
            .findDistinctByProfesionalesEspecialidadId(idEspecialidad);

        return listaSucursales.stream()
                .map(suc -> new SucursalDTO(suc.getIdSucursal(), suc.getNombre()))
                .collect(Collectors.toList());
    }

    public static class SucursalDTO {
        private Long id;
        private String nombre;

        public SucursalDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }
        public Long getId()       { return id; }
        public String getNombre() { return nombre; }
    }
}
