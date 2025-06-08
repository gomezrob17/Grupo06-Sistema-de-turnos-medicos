package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Paciente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
	Optional<Paciente> findByContactoEmail(String email);
}