package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Long idRol;

	@Column(name = "nombre", nullable = false, unique = true, length = 50)
	private String nombre;

	/** Un Rol puede asignarse a muchas Usuarios */
	@OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Usuario> usuarios = new ArrayList<>();

	public Rol() {}

	/** El atributo Nombre del rol puede ser "EMPLEADO", "PACIENTE", "PROFESIONAL" */
	public Rol(String nombre) {
		this.nombre = nombre;
	}

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Usuario> getPersonas() {
		return usuarios;
	}

	public void setPersonas(List<Usuario> personas) {
		this.usuarios = personas;
	}

}
