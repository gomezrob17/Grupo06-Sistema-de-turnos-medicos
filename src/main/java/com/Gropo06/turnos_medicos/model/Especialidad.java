package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "especialidades")
public class Especialidad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_especialidad")
	private Long idEspecialidad;

	@Column(name = "nombre", length = 100, nullable = false, unique = true)
	private String nombre;

	// Duración estándar del turno en minutos
	@Column(name = "duracion", nullable = false)
	private Integer duracion;

	@ManyToMany(mappedBy = "especialidades", fetch = FetchType.LAZY)
	private Set<Sucursal> sucursales;

	@OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
	private Set<Profesional> profesionales;

	public Especialidad() {
	}

	public Especialidad(String nombre, Integer duracion) {
		super();
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

	public Set<Sucursal> getSucursales() {
		return sucursales;
	}

	public void setSucursales(Set<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}

	public Set<Profesional> getProfesionales() {
		return profesionales;
	}

	public void setProfesionales(Set<Profesional> profesionales) {
		this.profesionales = profesionales;
	}
}
