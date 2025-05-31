package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
	
    /** Permite buscar un Rol por su nombre "PACIENTE", "EMPLEADO",etc.*/
	
    Optional<Rol> findByNombre(String nombre);
}