package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

	boolean existsByEmail(String email);

}
