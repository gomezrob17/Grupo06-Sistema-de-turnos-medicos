package com.Gropo06.turnos_medicos.repository;

import com.Gropo06.turnos_medicos.model.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    //Busca todas las sucursales que tengan al menos un Profesional cuya especialidad tenga el ID dado.
   
    @Query("""
        SELECT DISTINCT s
          FROM Sucursal s 
          JOIN s.profesionales p 
         WHERE p.especialidad.idEspecialidad = :idEsp
        """)
    
    List<Sucursal> findDistinctByProfesionalesEspecialidadId(@Param("idEsp") Long idEsp);

	boolean existsByNombre(String nombre);
	
	Sucursal findByNombre(String nombre);
	
	boolean existsByDireccion(String direccion);
	
	Sucursal findByDireccion(String direccion);
}
