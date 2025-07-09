package com.Gropo06.turnos_medicos.dto;

import java.time.LocalTime;

public class TurnoDTO {
    private Long idTurno;
    private LocalTime hora;
    private Long pacienteId;
    private Long profesionalId;
    private Long estadoId;
    private Long agendaId;

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
    public Long getPacienteId() {
        return pacienteId;
    }
    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
    public Long getProfesionalId() {
        return profesionalId;
    }
    public void setProfesionalId(Long profesionalId) {
        this.profesionalId = profesionalId;
    }
    public Long getEstadoId() {
        return estadoId;
    }
    public void setEstadoId(Long estadoId) {
        this.estadoId = estadoId;
    }
    public Long getAgendaId() {
        return agendaId;
    }
    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }
}
