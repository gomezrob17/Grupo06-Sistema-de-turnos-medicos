package com.Gropo06.turnos_medicos.dto;

import java.time.LocalDate;
import java.util.List;

public class AgendaDTO {
    private Long idAgenda;
    private LocalDate dia;
    private Long tipoEspecialidadId;
    private Long sucursalId;
    private List<Long> turnoIds;

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
    public Long getTipoEspecialidadId() {
        return tipoEspecialidadId;
    }
    public void setTipoEspecialidadId(Long tipoEspecialidadId) {
        this.tipoEspecialidadId = tipoEspecialidadId;
    }
    public Long getSucursalId() {
        return sucursalId;
    }
    public void setSucursalId(Long sucursalId) {
        this.sucursalId = sucursalId;
    }
    public List<Long> getTurnoIds() {
        return turnoIds;
    }
    public void setTurnoIds(List<Long> turnoIds) {
        this.turnoIds = turnoIds;
    }
}