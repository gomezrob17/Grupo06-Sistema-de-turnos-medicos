package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "agenda")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia", nullable = false)
    private LocalDate dia;

    @ManyToOne
    @JoinColumn(name = "id_turno")
    private Turno turnos;

    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    private Especialidad tipoEspecialidad;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    // Constructor vacío (requerido por JPA)
    public Agenda() {
    }

    // Constructor sin ID
    public Agenda(LocalDate dia, Turno turnos, Especialidad tipoEspecialidad, Sucursal sucursal) {
        this.dia = dia;
        this.turnos = turnos;
        this.tipoEspecialidad = tipoEspecialidad;
        this.sucursal = sucursal;
    }

    // Constructor con ID (opcional)
    public Agenda(Long id, LocalDate dia, Turno turnos, Especialidad tipoEspecialidad, Sucursal sucursal) {
        this.id = id;
        this.dia = dia;
        this.turnos = turnos;
        this.tipoEspecialidad = tipoEspecialidad;
        this.sucursal = sucursal;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public Turno getTurnos() {
        return turnos;
    }

    public void setTurnos(Turno turnos) {
        this.turnos = turnos;
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
}