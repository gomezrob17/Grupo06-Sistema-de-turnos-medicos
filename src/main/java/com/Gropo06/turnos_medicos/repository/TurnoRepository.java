package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByPaciente(Paciente paciente);
    List<Turno> findByEstadoNombre(String estado);
}
