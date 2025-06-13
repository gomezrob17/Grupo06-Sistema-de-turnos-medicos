package com.Gropo06.turnos_medicos.dto;

public class ProfesionalDTO {

    private Long idProfesional;
    private String dni;
    private String nombre;
    private String apellido;
    private String matricula;
    private String nombreEspecialidad;

    public ProfesionalDTO() {}

    public ProfesionalDTO(Long idProfesional, String dni, String nombre, String apellido, String matricula, String nombreEspecialidad) {
        this.idProfesional = idProfesional;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public Long getIdProfesional() {
        return idProfesional;
    }
    public void setIdProfesional(Long idProfesional) {
        this.idProfesional = idProfesional;
    }

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }
    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
}