package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    Optional<Agenda> findByDiaAndSucursalAndTipoEspecialidad(
        LocalDate dia,
        Sucursal sucursal,
        Especialidad tipoEspecialidad
    );
}
