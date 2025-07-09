package com.Gropo06.turnos_medicos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gropo06.turnos_medicos.dto.TurnoDTO;
import com.Gropo06.turnos_medicos.exceptions.PacienteInvalido;
import com.Gropo06.turnos_medicos.exceptions.TurnoInvalido;
import com.Gropo06.turnos_medicos.mapper.MapperUtil;
import com.Gropo06.turnos_medicos.model.Agenda;
import com.Gropo06.turnos_medicos.model.EstadoTurno;
import com.Gropo06.turnos_medicos.model.Paciente;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Turno;
import com.Gropo06.turnos_medicos.repository.AgendaRepository;
import com.Gropo06.turnos_medicos.repository.EstadoTurnoRepository;
import com.Gropo06.turnos_medicos.repository.PacienteRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.TurnoRepository;

@Service
public class TurnoServiceImpl implements TurnoService {

	@Autowired
	private TurnoRepository turnoRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private EstadoTurnoService estadoTurnoService;

	@Autowired
	private ProfesionalRepository profesionalRepository;

	@Autowired
	private EstadoTurnoRepository estadoTurnoRepository;

	@Autowired
	private AgendaRepository agendaRepository;

	@Override
	public TurnoDTO crearTurno(TurnoDTO turnoDto) {
		System.out.println("=== LLAMADO A crearTurno (SERVICE) ===");
		System.out.println("DTO recibido: Profesional=" + turnoDto.getProfesionalId() + ", Paciente="
				+ turnoDto.getPacienteId() + ", Estado=" + turnoDto.getEstadoId() + ", Agenda=" + turnoDto.getAgendaId()
				+ ", Hora=" + turnoDto.getHora());

		Paciente paciente = pacienteRepository.findById(turnoDto.getPacienteId())
				.orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

		Profesional profesional = profesionalRepository.findById(turnoDto.getProfesionalId())
				.orElseThrow(() -> new RuntimeException("Profesional no encontrado"));

		EstadoTurno estado = estadoTurnoRepository.findById(turnoDto.getEstadoId())
				.orElseThrow(() -> new RuntimeException("Estado no encontrado"));

		Agenda agenda = agendaRepository.findById(turnoDto.getAgendaId())
				.orElseThrow(() -> new RuntimeException("Agenda no encontrada"));

		Turno turno = new Turno();
		turno.setPaciente(paciente);
		turno.setProfesional(profesional);
		turno.setEstado(estado);
		turno.setAgenda(agenda);
		turno.setHora(turnoDto.getHora());

		System.out.println("Entidad Turno preparada: Profesional=" + turno.getProfesional().getIdUsuario()
				+ ", Paciente=" + turno.getPaciente().getIdUsuario() + ", Estado=" + turno.getEstado().getIdEstado()
				+ ", Agenda=" + turno.getAgenda().getIdAgenda() + ", Hora=" + turno.getHora());
		Turno guardado = turnoRepository.save(turno);
		return MapperUtil.toDto(guardado);
	}

	@Override
	public TurnoDTO cancelarTurno(Long idTurno) {
		Turno turno = turnoRepository.findById(idTurno)
				.orElseThrow(() -> new TurnoInvalido("Turno no encontrado: " + idTurno));
		EstadoTurno cancelado = MapperUtil.toEntity(estadoTurnoService.obtenerEstadoCancelado());
		turno.setEstado(cancelado);
		Turno guardado = turnoRepository.save(turno);
		return MapperUtil.toDto(guardado);
	}

	@Override
	public List<TurnoDTO> obtenerTurnosDisponibles() {
		return turnoRepository.findByEstadoNombre("Disponible").stream().map(MapperUtil::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<TurnoDTO> obtenerTodos() {
		return turnoRepository.findAll().stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

	@Override
	public void eliminarPorId(Long idTurno) {
		if (turnoRepository.existsById(idTurno)) {
			turnoRepository.deleteById(idTurno);
		} else {
			throw new RuntimeException("Turno no encontrado con id: " + idTurno);
		}
	}

	@Override
	public List<TurnoDTO> findByPacienteId(Long pacienteId) {
		System.out.println("=== BUSCANDO TURNOS PARA PACIENTE ID: " + pacienteId);
		Paciente paciente = pacienteRepository.findById(pacienteId)
				.orElseThrow(() -> new PacienteInvalido("Paciente no encontrado: " + pacienteId));
		List<Turno> turnos = turnoRepository.findByPaciente(paciente);
		System.out.println("Turnos encontrados: " + (turnos != null ? turnos.size() : 0));
		return turnos.stream().map(MapperUtil::toDto).collect(Collectors.toList());
	}

}
