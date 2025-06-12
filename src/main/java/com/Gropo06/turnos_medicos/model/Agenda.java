package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agenda")
    private Long idAgenda;


    @Column(name = "dia", nullable = false)
    private LocalDate dia;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad tipoEspecialidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sucursal", nullable = false)
    private Sucursal sucursal;


    @OneToMany(mappedBy = "agenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Turno> turnos = new ArrayList<>();

    public Agenda() {}

    public Agenda(LocalDate dia, Especialidad tipoEspecialidad, Sucursal sucursal) {
        this.dia = dia;
        this.tipoEspecialidad = tipoEspecialidad;
        this.sucursal = sucursal;
    }


    public Long getIdAgenda() {
        return idAgenda;
    }
    public void setIdAgenda(Long idAgenda) {
        this.idAgenda = idAgenda;
    }

    public LocalDate getDia() {
        return dia;
    }
    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Especialidad getTipoEspecialidad() {
        return tipoEspecialidad;
    }
    public void setTipoEspecialidad(Especialidad tipoEspecialidad) {
        this.tipoEspecialidad = tipoEspecialidad;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }
    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }
    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }


    public void addTurno(Turno t) {
        turnos.add(t);
        t.setAgenda(this);
    }

    public void removeTurno(Turno t) {
        turnos.remove(t);
        t.setAgenda(null);
    }
}
