package com.Gropo06.turnos_medicos.dto;

import java.time.LocalTime;

import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.model.Profesional;

public class TurnoVistaDTO {
	private Long idTurno;
	private LocalTime hora;
	private Agenda agenda;
	private Profesional profesional;
	private EstadoTurno estado;

	public Long getIdTurno() {
		return idTurno;
	}

	public void setIdTurno(Long idTurno) {
		this.idTurno = idTurno;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}

	public Profesional getProfesional() {
		return profesional;
	}

	public void setProfesional(Profesional profesional) {
		this.profesional = profesional;
	}

	public EstadoTurno getEstado() {
		return estado;
	}

	public void setEstado(EstadoTurno estado) {
		this.estado = estado;
	}

}
