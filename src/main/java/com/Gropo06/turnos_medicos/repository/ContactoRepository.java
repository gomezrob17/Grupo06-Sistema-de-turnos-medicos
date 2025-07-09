package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

	boolean existsByEmail(String email);
	
	@Query("SELECT c.usuario.idUsuario FROM Contacto c WHERE c.email = :email")
    Long findIdUsuarioByEmail(@Param("email") String email);

}
