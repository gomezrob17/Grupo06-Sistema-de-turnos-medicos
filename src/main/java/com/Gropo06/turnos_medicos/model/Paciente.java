package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paciente")
public class Paciente extends Usuario {

	@Column(name = "obra_social", length = 20)
	private String obraSocial;

	@OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
	private List<Turno> turnos = new ArrayList<>();

	public Paciente() {
	}

	public Paciente(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String genero,
			Contacto contacto, Rol rol, String obraSocial) {
		super(dni, nombre, apellido, fechaNacimiento, genero, contacto, rol);
		this.obraSocial = obraSocial;
	}

	public String getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(String obraSocial) {
		this.obraSocial = obraSocial;
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}
}
