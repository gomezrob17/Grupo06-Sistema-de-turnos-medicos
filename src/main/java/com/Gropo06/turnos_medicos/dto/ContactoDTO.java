package com.Gropo06.turnos_medicos.dto;

public class ContactoDTO {
    private Long idContacto;
    private String email;
    private String telefono;
    private Long usuarioId;

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
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
