package com.Gropo06.turnos_medicos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfesionalDTO {
    private Long idUsuario;
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private Long contactoId;
    private String contactoEmail;
    private String contactoTelefono;
    private Long rolId;
    private String password;
    private String matricula;
    private Long especialidadId;
    private String especialidadNombre;
    private List<Long> disponibilidadIds;
    private List<DisponibilidadDTO> disponibilidades = new ArrayList<>();
    private List<Long> sucursalIds = new ArrayList<>();
    private List<SucursalDTO> sucursales;
    
	public ProfesionalDTO() {
	}
	
    public ProfesionalDTO(Long idUsuario2, String nombre2, String apellido2) {
	}

	public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
    public Long getContactoId() {
        return contactoId;
    }
    public void setContactoId(Long contactoId) {
        this.contactoId = contactoId;
    }
    public String getContactoEmail() {
        return contactoEmail;
    }
    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }
    public String getContactoTelefono() {
        return contactoTelefono;
    }
    public void setContactoTelefono(String contactoTelefono) {
        this.contactoTelefono = contactoTelefono;
    }
    public Long getRolId() {
        return rolId;
    }
    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public Long getEspecialidadId() {
        return especialidadId;
    }
    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }
    public List<Long> getDisponibilidadIds() {
        return disponibilidadIds;
    }
    public void setDisponibilidadIds(List<Long> disponibilidadIds) {
        this.disponibilidadIds = disponibilidadIds;
    }

	public List<Long> getSucursalIds() {
		return sucursalIds;
	}

	public void setSucursalIds(List<Long> sucursalIds) {
		this.sucursalIds = sucursalIds;
	}

	public String getEspecialidadNombre() {
		return especialidadNombre;
	}

	public void setEspecialidadNombre(String especialidadNombre) {
		this.especialidadNombre = especialidadNombre;
	}

	public List<DisponibilidadDTO> getDisponibilidades() {
		return disponibilidades;
	}

	public void setDisponibilidades(List<DisponibilidadDTO> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}
	
	public List<SucursalDTO> getSucursales() {
	    return sucursales;
	}
	public void setSucursales(List<SucursalDTO> sucursales) {
	    this.sucursales = sucursales;
	}

}
