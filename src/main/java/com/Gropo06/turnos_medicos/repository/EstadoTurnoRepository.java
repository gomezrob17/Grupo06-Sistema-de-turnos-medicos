package com.Gropo06.turnos_medicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Gropo06.turnos_medicos.model.EstadoTurno;

@Repository
public interface EstadoTurnoRepository extends JpaRepository<EstadoTurno, Long> {
	
	Optional<EstadoTurno> findByNombre(String nombre);

}
