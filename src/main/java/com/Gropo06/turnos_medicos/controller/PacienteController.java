package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.EspecialidadDTO;
import com.Gropo06.turnos_medicos.dto.SucursalDTO;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.repository.DisponibilidadRepository;
import com.Gropo06.turnos_medicos.service.EspecialidadService;
import com.Gropo06.turnos_medicos.service.SucursalService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/paciente/turnos")
public class PacienteController {

	private final DisponibilidadRepository dispRepo;
	private final EspecialidadService especialidadService;
	private final SucursalService sucursalService;

	public PacienteController(DisponibilidadRepository dispRepo, EspecialidadService especialidadService,
			SucursalService sucursalService) {
		this.dispRepo = dispRepo;
		this.especialidadService = especialidadService;
		this.sucursalService = sucursalService;
	}

	@GetMapping("/buscar")
	public ResponseEntity<List<ResultadoParaFront>> buscarTurnosDisponibles(@RequestParam("idEspecialidad") Long idEsp,
			@RequestParam("idSucursal") Long idSuc,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

		// 1) Obtenemos los DTOs y los convertimos a entidades
		EspecialidadDTO espDto = especialidadService.buscarEspecialidadPorId(idEsp);
		SucursalDTO sucDto = sucursalService.findById(idSuc);
		var espEnt = MapperUtil.toEntity(espDto);
		var sucEnt = MapperUtil.toEntity(sucDto);

		// 2) Consultamos las disponibilidades
		List<Disponibilidad> listaDisp = dispRepo.findByProfesional_EspecialidadAndSucursalAndFechaBetween(espEnt,
				sucEnt, desde, hasta);

		// 3) Agrupamos por profesional + día
		Map<String, List<Disponibilidad>> agrupado = new HashMap<>();
		for (Disponibilidad d : listaDisp) {
			String key = d.getProfesional().getIdUsuario() + "|" + d.getFecha();
			agrupado.computeIfAbsent(key, k -> new ArrayList<>()).add(d);
		}

		// 4) Generamos resultado para el front
		List<ResultadoParaFront> resultado = new ArrayList<>();
		for (List<Disponibilidad> dList : agrupado.values()) {
			var primera = dList.get(0);
			var prof = primera.getProfesional();
			LocalDate dia = primera.getFecha();
			LocalTime inicio = primera.getHoraInicio();
			LocalTime fin = primera.getHoraFin();
			int durMin = espDto.getDuracion();

			List<String> franjas = new ArrayList<>();
			LocalTime cursor = inicio;
			while (!cursor.plusMinutes(durMin).isAfter(fin)) {
				LocalTime tope = cursor.plusMinutes(durMin);
				franjas.add(cursor + "-" + tope);
				cursor = tope;
			}

			resultado.add(new ResultadoParaFront(
					new ProfesionalDTO(prof.getIdUsuario(), prof.getNombre(), prof.getApellido()), dia.toString(),
					franjas));
		}

		return ResponseEntity.ok(resultado);
	}

	// DTO para enviar al front:
	public static class ResultadoParaFront {
		private ProfesionalDTO profesional;
		private String dia;
		private List<String> franjas;

		public ResultadoParaFront(ProfesionalDTO profesional, String dia, List<String> franjas) {
			this.profesional = profesional;
			this.dia = dia;
			this.franjas = franjas;
		}

		public ProfesionalDTO getProfesional() {
			return profesional;
		}

		public String getDia() {
			return dia;
		}

		public List<String> getFranjas() {
			return franjas;
		}
	}

	public static class ProfesionalDTO {
		private Long idUsuario;
		private String nombre;
		private String apellido;

		public ProfesionalDTO(Long idUsuario, String nombre, String apellido) {
			this.idUsuario = idUsuario;
			this.nombre = nombre;
			this.apellido = apellido;
		}

		public Long getIdUsuario() {
			return idUsuario;
		}

		public String getNombre() {
			return nombre;
		}

		public String getApellido() {
			return apellido;
		}
	}
}
