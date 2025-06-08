package com.Gropo06.turnos_medicos.model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "empleados")
public class Empleado extends Usuario {

	@Column(name = "activo", nullable = false)
	private Boolean activo;

	public Empleado() {
	}

	public Empleado(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String genero,
			Contacto contacto, Rol rol, String password, Boolean activo) {
		super(dni, nombre, apellido, fechaNacimiento, genero, contacto, rol);
		this.activo = activo;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
