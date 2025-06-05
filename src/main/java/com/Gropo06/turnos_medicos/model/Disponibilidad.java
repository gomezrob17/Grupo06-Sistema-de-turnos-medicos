package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidades")
public class Disponibilidad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_disponibilidad")
	private Long idDisponibilidad;

	@Column(name = "fecha", nullable = false)
	private LocalDate fecha;

	@Column(name = "hora_inicio", nullable = false)
	private LocalTime horaInicio;

	@Column(name = "hora_fin", nullable = false)
	private LocalTime horaFin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_profesional", nullable = false)
	private Profesional profesional;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_sucursal", nullable = false)
	private Sucursal sucursal;

	public Disponibilidad() {
	}

	public Disponibilidad(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Profesional profesional,
			Sucursal sucursal) {
		super();
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.profesional = profesional;
		this.sucursal = sucursal;
	}

	public Long getIdDisponibilidad() {
		return idDisponibilidad;
	}

	public void setIdDisponibilidad(Long idDisponibilidad) {
		this.idDisponibilidad = idDisponibilidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
}
