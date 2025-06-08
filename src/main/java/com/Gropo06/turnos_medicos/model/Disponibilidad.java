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

    // Nuevo campo para guardar el día de la semana en español
    @Column(name = "dia_semana", nullable = false)
    private String diaSemana;

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
        this.setFecha(fecha);  // Usamos el setter para que calcule el día automáticamente
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

    // Al asignar la fecha, calcula el día de la semana en español y lo asigna
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
        if (fecha != null) {
            this.diaSemana = fecha.getDayOfWeek()
                .getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));
        } else {
            this.diaSemana = null;
        }
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
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