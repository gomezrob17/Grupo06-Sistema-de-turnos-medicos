package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "profesionales")
public class Profesional extends Usuario {

	@Column(name = "matricula", length = 50, nullable = false, unique = true)
	private String matricula;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_especialidad", nullable = false)
	private Especialidad especialidad;

	@OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("fecha ASC, horaInicio ASC")
	private List<Disponibilidad> disponibilidades = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "profesional_sucursal", joinColumns = @JoinColumn(name = "id_profesional"), inverseJoinColumns = @JoinColumn(name = "id_sucursal"))
	private Set<Sucursal> sucursales;

	public Profesional() {
	}

	public Profesional(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String genero,
			Contacto contacto, Rol rol, String matricula, Especialidad especialidad) {
		super(dni, nombre, apellido, fechaNacimiento, genero, contacto, rol);
		this.matricula = matricula;
		this.especialidad = especialidad;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	public List<Disponibilidad> getDisponibilidades() {
		return disponibilidades;
	}

	public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}

	public Set<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(Set<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

}
