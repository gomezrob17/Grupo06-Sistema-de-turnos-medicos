package com.Gropo06.turnos_medicos.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Sucursal;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {

    List<Disponibilidad> findByProfesional_EspecialidadAndSucursalAndFechaBetween(
            Especialidad especialidad,
            Sucursal sucursal,
            LocalDate desde,
            LocalDate hasta
    );

	List<Disponibilidad> findByProfesional_EspecialidadAndSucursal(Especialidad espElegida, Sucursal sucElegida);
}

