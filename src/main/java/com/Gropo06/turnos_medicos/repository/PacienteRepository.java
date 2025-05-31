package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
 
}