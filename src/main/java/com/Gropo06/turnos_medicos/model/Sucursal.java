package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "sucursales")
public class Sucursal {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(name = "direccion", length = 200)
    private String direccion;

    // N−M con Especialidad
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sucursal_especialidad",
        joinColumns = @JoinColumn(name = "id_sucursal"),
        inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private Set<Especialidad> especialidades;

    // N−M con Profesional (vía la tabla "profesional_sucursal")
    @ManyToMany(mappedBy = "sucursales", fetch = FetchType.LAZY)
    private Set<Profesional> profesionales;

    public Sucursal() {}

    public Long getIdSucursal() {
        return idSucursal;
    }
    public void setIdSucursal(Long idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<Especialidad> getEspecialidades() {
        return especialidades;
    }
    public void setEspecialidades(Set<Especialidad> especialidades) {
        this.especialidades = especialidades;
    }

    public Set<Profesional> getProfesionales() {
        return profesionales;
    }
    public void setProfesionales(Set<Profesional> profesionales) {
        this.profesionales = profesionales;
    }

}
