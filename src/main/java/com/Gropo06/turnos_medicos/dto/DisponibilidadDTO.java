package com.Gropo06.turnos_medicos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.Gropo06.turnos_medicos.model.enums.DiaSemana;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

public class DisponibilidadDTO {
	private Long idDisponibilidad;
	
	@JsonIgnore
	private LocalDate fecha;
	
	@Schema(description = "Día de la semana", example = "LUNES")
	private DiaSemana diaSemana;

	@DateTimeFormat(pattern = "HH:mm")
	@Schema(description = "Hora de inicio (formato HH:mm)", example = "08:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime horaInicio;
	
	@DateTimeFormat(pattern = "HH:mm")
	@Schema(description = "Hora de fin (formato HH:mm)", example = "16:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
	private LocalTime horaFin;

	private Long profesionalId;
	private Long sucursalId;
	
	@JsonIgnore
	private String sucursalNombre;

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

	public DiaSemana getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
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

	public Long getProfesionalId() {
		return profesionalId;
	}

	public void setProfesionalId(Long profesionalId) {
		this.profesionalId = profesionalId;
	}

	public Long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Long sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getSucursalNombre() {
		return sucursalNombre;
	}

	public void setSucursalNombre(String sucursalNombre) {
		this.sucursalNombre = sucursalNombre;
	}
}
