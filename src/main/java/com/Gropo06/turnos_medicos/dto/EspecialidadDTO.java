package com.Gropo06.turnos_medicos.dto;

public class EspecialidadDTO {

    private Long idEspecialidad;
    private String nombre;
    private Integer duracion;  // minutos

    public EspecialidadDTO() {}

    public EspecialidadDTO(Long idEspecialidad, String nombre, Integer duracion) {
        this.idEspecialidad = idEspecialidad;
        this.nombre = nombre;
        this.duracion = duracion;
    }

    public Long getIdEspecialidad() {
        return idEspecialidad;
    }
    public void setIdEspecialidad(Long idEspecialidad) {
        this.idEspecialidad = idEspecialidad;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
}