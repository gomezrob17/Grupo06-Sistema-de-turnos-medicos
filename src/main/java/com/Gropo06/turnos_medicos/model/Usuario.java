package com.Gropo06.turnos_medicos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank(message = "El DNI es obligatorio.")
    @Size(max = 20, message = "El DNI debe tener máximo 20 caracteres.")
    @Column(name = "dni", unique = true, nullable = false, length = 20)
    private String dni;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre debe tener máximo 50 caracteres.")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 50, message = "El apellido debe tener máximo 50 caracteres.")
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género es obligatorio.")
    @Size(max = 20, message = "El género debe tener máximo 20 caracteres.")
    @Column(name = "genero", nullable = false, length = 20)
    private String genero;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    @NotNull(message = "El contacto es obligatorio.")
    private Contacto contacto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    @NotNull(message = "El rol es obligatorio.")
    private Rol rol;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Column(name = "password", nullable = false)
    private String password;

    public Usuario() {
    }

    public Usuario(String dni, String nombre, String apellido, LocalDate fechaNacimiento, String genero,
            Contacto contacto, Rol rol) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.contacto = contacto;
        this.rol = rol;
    }

    // Getters y setters...

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

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}