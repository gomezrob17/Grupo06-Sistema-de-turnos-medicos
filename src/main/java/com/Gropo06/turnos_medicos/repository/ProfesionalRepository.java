package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Profesional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
 
    @Query("""
        SELECT p 
        FROM Profesional p 
        LEFT JOIN FETCH p.disponibilidades d 
        WHERE p.idUsuario = :id
    """)
    Optional<Profesional> findByIdWithDisponibilidades(@Param("id") Long id);

	boolean existsByEspecialidad_IdEspecialidad(Long id);
}
