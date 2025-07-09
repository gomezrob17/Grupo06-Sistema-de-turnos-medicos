package com.Gropo06.turnos_medicos.dto;

import java.util.List;

public class RolDTO {
    private Long idRol;
    private String nombre;
    private List<Long> usuarioIds;

    public Long getIdRol() {
        return idRol;
    }
    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<Long> getUsuarioIds() {
        return usuarioIds;
    }
    public void setUsuarioIds(List<Long> usuarioIds) {
        this.usuarioIds = usuarioIds;
    }
}
