package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estados_turnos")
public class EstadoTurno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_estado")
	private Long idEstado;

	@Column(name = "nombre", nullable = false, length = 50)
	private String nombre;

	@OneToMany(mappedBy = "estado", cascade = CascadeType.ALL)
	private List<Turno> turnos = new ArrayList<>();

	public EstadoTurno() {
	}

	public EstadoTurno(String nombre) {
		this.nombre = nombre;
	}

	// Getters y Setters

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Turno> getTurnos() {
		return turnos;
	}

	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}
}
