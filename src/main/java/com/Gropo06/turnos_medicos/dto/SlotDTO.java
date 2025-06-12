package com.Gropo06.turnos_medicos.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SlotDTO {
	private Long idProfesional;
	private Long idEspecialidad;
	private Long idSucursal;
	private LocalDate fecha;
	private LocalTime horaInicio;
	private LocalTime horaFin;

	public SlotDTO(Long idProfesional, Long idEspecialidad, Long idSucursal, LocalDate fecha, LocalTime horaInicio,
			LocalTime horaFin) {
		this.idProfesional = idProfesional;
		this.idEspecialidad = idEspecialidad;
		this.idSucursal = idSucursal;
		this.fecha = fecha;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}

	public Long getIdProfesional() {
		return idProfesional;
	}

	public Long getIdEspecialidad() {
		return idEspecialidad;
	}

	public Long getIdSucursal() {
		return idSucursal;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}
}
