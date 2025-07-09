package com.Gropo06.turnos_medicos.dto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class PacienteDTO {
    private Long idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private Long contactoId;
    private String contactoTelefono;
    private String contactoEmail;
    private Long rolId;
    private String password;
    private String obraSocial;
    private List<Long> turnoIds;

    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public Long getContactoId() {
        return contactoId;
    }
    public void setContactoId(Long contactoId) {
        this.contactoId = contactoId;
    }
    public String getContactoTelefono() {
        return contactoTelefono;
    }
    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }
    public String getContactoEmail() {
        return contactoEmail;
    }
    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }
    public Long getRolId() {
        return rolId;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getObraSocial() {
        return obraSocial;
    }
    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }
    public List<Long> getTurnoIds() {
        return turnoIds;
    }
    public void setTurnoIds(List<Long> turnoIds) {
        this.turnoIds = turnoIds;
    }
    
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
