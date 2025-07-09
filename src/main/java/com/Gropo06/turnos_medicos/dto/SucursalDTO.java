package com.Gropo06.turnos_medicos.dto;

import java.util.List;
import java.util.Set;

public class SucursalDTO {
    private Long idSucursal;
    private String nombre;
    private String direccion;
    private Set<Long> especialidadIds;
    private Set<Long> profesionalIds;
    private List<Long> disponibilidadIds;

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
    public Set<Long> getEspecialidadIds() {
        return especialidadIds;
    }
    public void setEspecialidadIds(Set<Long> especialidadIds) {
        this.especialidadIds = especialidadIds;
    }
    public Set<Long> getProfesionalIds() {
        return profesionalIds;
    }
    public void setProfesionalIds(Set<Long> profesionalIds) {
        this.profesionalIds = profesionalIds;
    }
    public List<Long> getDisponibilidadIds() {
        return disponibilidadIds;
    }
    public void setDisponibilidadIds(List<Long> disponibilidadIds) {
        this.disponibilidadIds = disponibilidadIds;
    }
}
