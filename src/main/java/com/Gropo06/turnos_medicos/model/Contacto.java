package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contactos")
public class Contacto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_contacto")
	private Long idContacto;

	@Column(name = "email", unique = true, nullable = false, length = 100)
	private String email;

	@Column(name = "telefono", nullable = false, length = 20)
	private String telefono;

	@OneToOne(mappedBy = "contacto")
	private Usuario usuario;

	public Contacto() {
	}

	public Contacto(String email, String telefono) {
		this.email = email;
		this.telefono = telefono;
	}

	public Long getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Long idContacto) {
		this.idContacto = idContacto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
