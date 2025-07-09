package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sucursales")
public class Sucursal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "sucursal_especialidad",
        joinColumns = @JoinColumn(name = "id_sucursal"),
        inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private Set<Especialidad> especialidades = new HashSet<>();

    @ManyToMany(mappedBy = "sucursales", fetch = FetchType.LAZY)
    private Set<Profesional> profesionales = new HashSet<>();

    @OneToMany(mappedBy = "sucursal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Disponibilidad> disponibilidades;

    public Sucursal() {}
    
    public Sucursal(String nombre, String direccion) {
		super();
		this.nombre = nombre;
		this.direccion = direccion;
	}

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

	public List<Disponibilidad> getDisponibilidades() {
		return disponibilidades;
	}

	public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}

    
    
}
