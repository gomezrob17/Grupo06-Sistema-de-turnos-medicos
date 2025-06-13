package com.Gropo06.turnos_medicos.dto;

import java.util.Set;

public class SucursalDTO {

    private Long idSucursal;
    private String nombre;
    private String direccion;
    private Set<EspecialidadDTO> especialidades;
    private Set<ProfesionalDTO> profesionales;

    public SucursalDTO() {}

    public SucursalDTO(Long idSucursal, String nombre, String direccion,
                       Set<EspecialidadDTO> especialidades,
                       Set<ProfesionalDTO> profesionales) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.especialidades = especialidades;
        this.profesionales = profesionales;
    }

    // Getters y setters

    public Long getIdSucursal() {
        return idSucursal;
    }
    public void setIdSucursal(Long idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<EspecialidadDTO> getEspecialidades() {
        return especialidades;
    }
    public void setEspecialidades(Set<EspecialidadDTO> especialidades) {
        this.especialidades = especialidades;
    }

    public Set<ProfesionalDTO> getProfesionales() {
        return profesionales;
    }
    public void setProfesionales(Set<ProfesionalDTO> profesionales) {
        this.profesionales = profesionales;
    }
    
    
}