package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "turnos")
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesional", nullable = false)
    private Profesional profesional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoTurno estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agenda", nullable = false)
    private Agenda agenda;

    public Turno() {}

    public Turno(LocalTime hora, Paciente paciente, Profesional profesional, 
                 EstadoTurno estado, Agenda agenda) {
        this.hora = hora;
        this.paciente = paciente;
        this.profesional = profesional;
        this.estado = estado;
        this.agenda = agenda;
    }


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

    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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

    public Agenda getAgenda() {
        return agenda;
    }
    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }
}
