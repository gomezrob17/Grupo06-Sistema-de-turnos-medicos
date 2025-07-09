package com.Gropo06.turnos_medicos.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.Gropo06.turnos_medicos.dto.*;
import com.Gropo06.turnos_medicos.model.*;
import com.Gropo06.turnos_medicos.model.Usuario;
import com.Gropo06.turnos_medicos.dto.UsuarioDTO;

public class MapperUtil {

	// ---------- Especialidad ----------
	public static EspecialidadDTO toDto(Especialidad e) {
		if (e == null)
			return null;
		EspecialidadDTO dto = new EspecialidadDTO();
		dto.setIdEspecialidad(e.getIdEspecialidad());
		dto.setNombre(e.getNombre());
		dto.setDuracion(e.getDuracion());
		return dto;
	}

	public static Especialidad toEntity(EspecialidadDTO dto) {
		if (dto == null)
			return null;
		Especialidad e = new Especialidad();
		e.setIdEspecialidad(dto.getIdEspecialidad());
		e.setNombre(dto.getNombre());
		e.setDuracion(dto.getDuracion());
		return e;
	}

	// ---------- EstadoTurno ----------
	public static EstadoTurnoDTO toDto(EstadoTurno e) {
		if (e == null)
			return null;
		EstadoTurnoDTO dto = new EstadoTurnoDTO();
		dto.setIdEstado(e.getIdEstado());
		dto.setNombre(e.getNombre());
		return dto;
	}

	public static EstadoTurno toEntity(EstadoTurnoDTO dto) {
		if (dto == null)
			return null;
		EstadoTurno e = new EstadoTurno();
		e.setIdEstado(dto.getIdEstado());
		e.setNombre(dto.getNombre());
		return e;
	}

	// ---------- Agenda ----------
	public static AgendaDTO toDto(Agenda a) {
		if (a == null)
			return null;
		AgendaDTO dto = new AgendaDTO();
		dto.setIdAgenda(a.getIdAgenda());
		dto.setDia(a.getDia());
		dto.setTipoEspecialidadId(a.getTipoEspecialidad().getIdEspecialidad());
		dto.setSucursalId(a.getSucursal().getIdSucursal());
		dto.setTurnoIds(a.getTurnos().stream().map(Turno::getIdTurno).collect(Collectors.toList()));
		return dto;
	}

	public static Agenda toEntity(AgendaDTO dto) {
		if (dto == null)
			return null;
		Agenda a = new Agenda();
		a.setIdAgenda(dto.getIdAgenda());
		a.setDia(dto.getDia());
		Especialidad esp = new Especialidad();
		esp.setIdEspecialidad(dto.getTipoEspecialidadId());
		a.setTipoEspecialidad(esp);

		Sucursal suc = new Sucursal();
		suc.setIdSucursal(dto.getSucursalId());
		a.setSucursal(suc);

		return a;
	}

	// ---------- Contacto ----------
	public static ContactoDTO toDto(Contacto c) {
		if (c == null)
			return null;
		ContactoDTO dto = new ContactoDTO();
		dto.setIdContacto(c.getIdContacto());
		dto.setEmail(c.getEmail());
		dto.setTelefono(c.getTelefono());
		dto.setUsuarioId(c.getUsuario().getIdUsuario());
		return dto;
	}

	public static Contacto toEntity(ContactoDTO dto) {
		if (dto == null)
			return null;
		Contacto c = new Contacto();
		c.setIdContacto(dto.getIdContacto());
		c.setEmail(dto.getEmail());
		c.setTelefono(dto.getTelefono());

		Usuario u = new Usuario();
		u.setIdUsuario(dto.getUsuarioId());
		c.setUsuario(u);

		return c;
	}

	// ---------- Disponibilidad ----------
	public static DisponibilidadDTO toDto(Disponibilidad d) {
		if (d == null)
			return null;
		DisponibilidadDTO dto = new DisponibilidadDTO();
		dto.setIdDisponibilidad(d.getIdDisponibilidad());
		dto.setFecha(d.getFecha());
		dto.setDiaSemana(d.getDiaSemana());
		dto.setHoraInicio(d.getHoraInicio());
		dto.setHoraFin(d.getHoraFin());
		dto.setProfesionalId(d.getProfesional().getIdUsuario());
		dto.setSucursalId(d.getSucursal().getIdSucursal());
		return dto;
	}

	public static Disponibilidad toEntity(DisponibilidadDTO dto) {
		if (dto == null)
			return null;
		Disponibilidad d = new Disponibilidad();
		d.setIdDisponibilidad(dto.getIdDisponibilidad());
		d.setFecha(dto.getFecha());
		d.setDiaSemana(dto.getDiaSemana());
		d.setHoraInicio(dto.getHoraInicio());
		d.setHoraFin(dto.getHoraFin());

		Profesional p = new Profesional();
		p.setIdUsuario(dto.getProfesionalId());
		d.setProfesional(p);

		Sucursal s = new Sucursal();
		s.setIdSucursal(dto.getSucursalId());
		d.setSucursal(s);

		return d;
	}

	// ---------- Usuario ----------
	public static UsuarioDTO toDto(Usuario u) {
		if (u == null)
			return null;
		UsuarioDTO dto = new UsuarioDTO();
		dto.setIdUsuario(u.getIdUsuario());
		dto.setDni(u.getDni());
		dto.setNombre(u.getNombre());
		dto.setApellido(u.getApellido());
		dto.setFechaNacimiento(u.getFechaNacimiento());
		dto.setGenero(u.getGenero());
		dto.setContactoId(u.getContacto().getIdContacto());
		dto.setRolId(u.getRol().getIdRol());
		dto.setPassword(u.getPassword());
		return dto;
	}

	public static Usuario toEntity(UsuarioDTO dto) {
		if (dto == null)
			return null;
		Usuario u = new Usuario();
		u.setIdUsuario(dto.getIdUsuario());
		u.setDni(dto.getDni());
		u.setNombre(dto.getNombre());
		u.setApellido(dto.getApellido());
		u.setFechaNacimiento(dto.getFechaNacimiento());
		u.setGenero(dto.getGenero());
		u.setPassword(dto.getPassword());

		Contacto c = new Contacto();
		c.setIdContacto(dto.getContactoId());
		u.setContacto(c);

		Rol r = new Rol();
		r.setIdRol(dto.getRolId());
		u.setRol(r);

		return u;
	}

	private static UsuarioDTO toUsuarioDTO(PacienteDTO dto) {
		if (dto == null)
			return null;
		UsuarioDTO u = new UsuarioDTO();
		u.setIdUsuario(dto.getIdUsuario());
		u.setDni(dto.getDni());
		u.setNombre(dto.getNombre());
		u.setApellido(dto.getApellido());
		u.setFechaNacimiento(dto.getFechaNacimiento());
		u.setGenero(dto.getGenero());
		u.setContactoId(dto.getContactoId());
		u.setRolId(dto.getRolId());
		u.setPassword(dto.getPassword());
		return u;
	}

	private static UsuarioDTO toUsuarioDTO(ProfesionalDTO dto) {
		if (dto == null)
			return null;
		UsuarioDTO u = new UsuarioDTO();
		u.setIdUsuario(dto.getIdUsuario());
		u.setDni(dto.getDni());
		u.setNombre(dto.getNombre());
		u.setApellido(dto.getApellido());
		u.setFechaNacimiento(dto.getFechaNacimiento());
		u.setGenero(dto.getGenero());
		u.setContactoId(dto.getContactoId());
		u.setRolId(dto.getRolId());
		u.setPassword(dto.getPassword());
		return u;
	}

	private static UsuarioDTO toUsuarioDTO(EmpleadoDTO dto) {
		if (dto == null)
			return null;
		UsuarioDTO u = new UsuarioDTO();
		u.setIdUsuario(dto.getIdUsuario());
		u.setDni(dto.getDni());
		u.setNombre(dto.getNombre());
		u.setApellido(dto.getApellido());
		u.setFechaNacimiento(dto.getFechaNacimiento());
		u.setGenero(dto.getGenero());
		u.setContactoId(dto.getContactoId());
		u.setRolId(dto.getRolId());
		u.setPassword(dto.getPassword());
		return u;
	}

	// ---------- Paciente ----------
	public static PacienteDTO toDto(Paciente p) {
		if (p == null)
			return null;
		PacienteDTO dto = new PacienteDTO();
		UsuarioDTO base = toDto((Usuario) p);
		dto.setIdUsuario(base.getIdUsuario());
		dto.setDni(base.getDni());
		dto.setNombre(base.getNombre());
		dto.setApellido(base.getApellido());
		dto.setFechaNacimiento(base.getFechaNacimiento());
		dto.setGenero(base.getGenero());
		dto.setContactoId(base.getContactoId());
		dto.setRolId(base.getRolId());
		dto.setContactoEmail(p.getContacto().getEmail());
		dto.setContactoTelefono(p.getContacto().getTelefono());
		dto.setPassword(base.getPassword());
		dto.setObraSocial(p.getObraSocial());
		dto.setTurnoIds(p.getTurnos().stream().map(Turno::getIdTurno).collect(Collectors.toList()));
		return dto;
	}

	public static Paciente toEntity(PacienteDTO dto) {
		if (dto == null)
			return null;
		Paciente p = new Paciente();
		Usuario u = toEntity(toUsuarioDTO(dto));
		p.setIdUsuario(u.getIdUsuario());
		p.setDni(u.getDni());
		p.setNombre(u.getNombre());
		p.setApellido(u.getApellido());
		p.setFechaNacimiento(u.getFechaNacimiento());
		p.setGenero(u.getGenero());
		p.setRol(u.getRol());
		p.setPassword(u.getPassword());
		p.setObraSocial(dto.getObraSocial());
		Contacto contacto = new Contacto();
		contacto.setEmail(dto.getContactoEmail());
		contacto.setTelefono(dto.getContactoTelefono());
		p.setContacto(contacto);
		return p;
	}

	// ---------- Profesional ----------
	public static ProfesionalDTO toDto(Profesional p) {
		if (p == null)
			return null;
		ProfesionalDTO dto = new ProfesionalDTO();
		UsuarioDTO base = toDto((Usuario) p);

		dto.setIdUsuario(base.getIdUsuario());
		dto.setDni(base.getDni());
		dto.setNombre(base.getNombre());
		dto.setApellido(base.getApellido());
		dto.setFechaNacimiento(base.getFechaNacimiento());
		dto.setGenero(base.getGenero());
		dto.setContactoId(base.getContactoId());
		dto.setRolId(base.getRolId());
		dto.setPassword(base.getPassword());
		dto.setMatricula(p.getMatricula());

		if (p.getContacto() != null) {
			dto.setContactoEmail(p.getContacto().getEmail());
			dto.setContactoTelefono(p.getContacto().getTelefono());
		}

		if (p.getEspecialidad() != null) {
			dto.setEspecialidadId(p.getEspecialidad().getIdEspecialidad());
			dto.setEspecialidadNombre(p.getEspecialidad().getNombre());
		}

		// Sucursales
		if (p.getSucursales() != null) {
			dto.setSucursalIds(p.getSucursales().stream().map(Sucursal::getIdSucursal).collect(Collectors.toList()));

			dto.setSucursales(p.getSucursales().stream().map(s -> {
				SucursalDTO sDto = new SucursalDTO();
				sDto.setIdSucursal(s.getIdSucursal());
				sDto.setNombre(s.getNombre());
				sDto.setDireccion(s.getDireccion());
				return sDto;
			}).collect(Collectors.toList()));
		}

		// Disponibilidades
		if (p.getDisponibilidades() != null) {
			dto.setDisponibilidadIds(p.getDisponibilidades().stream().map(Disponibilidad::getIdDisponibilidad)
					.collect(Collectors.toList()));

			dto.setDisponibilidades(p.getDisponibilidades().stream().map(d -> {
				DisponibilidadDTO dDto = new DisponibilidadDTO();
				dDto.setIdDisponibilidad(d.getIdDisponibilidad());
				dDto.setFecha(d.getFecha());
				dDto.setDiaSemana(d.getDiaSemana());
				dDto.setHoraInicio(d.getHoraInicio());
				dDto.setHoraFin(d.getHoraFin());
				dDto.setProfesionalId(p.getIdUsuario());
				if (d.getSucursal() != null) {
					dDto.setSucursalId(d.getSucursal().getIdSucursal());
					dDto.setSucursalNombre(d.getSucursal().getNombre());
				}
				return dDto;
			}).collect(Collectors.toList()));
		}

		return dto;
	}

	public static Profesional toEntity(ProfesionalDTO dto) {
		if (dto == null)
			return null;
		Profesional p = new Profesional();
		Usuario u = toEntity(toUsuarioDTO(dto));

		p.setIdUsuario(u.getIdUsuario());
		p.setDni(u.getDni());
		p.setNombre(u.getNombre());
		p.setApellido(u.getApellido());
		p.setFechaNacimiento(u.getFechaNacimiento());
		p.setGenero(u.getGenero());
		p.setRol(u.getRol());

		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			p.setPassword(dto.getPassword());
		}

		p.setMatricula(dto.getMatricula());

		// Contacto
		Contacto contacto = new Contacto();
		if (dto.getContactoId() != null)
			contacto.setIdContacto(dto.getContactoId());
		contacto.setEmail(dto.getContactoEmail());
		contacto.setTelefono(dto.getContactoTelefono());
		p.setContacto(contacto);

		// Especialidad
		if (dto.getEspecialidadId() != null) {
			Especialidad esp = new Especialidad();
			esp.setIdEspecialidad(dto.getEspecialidadId());
			p.setEspecialidad(esp);
		}

		// Sucursales (solo por ID)
		if (dto.getSucursalIds() != null) {
			Set<Sucursal> sucursales = dto.getSucursalIds().stream().map(id -> {
				Sucursal s = new Sucursal();
				s.setIdSucursal(id);
				return s;
			}).collect(Collectors.toSet());
			p.setSucursales(sucursales);
		}

		// Disponibilidades
		if (dto.getDisponibilidades() != null) {
			List<Disponibilidad> disponibilidades = dto.getDisponibilidades().stream().map(dDto -> {
				Disponibilidad d = new Disponibilidad();
				d.setIdDisponibilidad(dDto.getIdDisponibilidad());
				d.setFecha(dDto.getFecha());
				d.setDiaSemana(dDto.getDiaSemana());
				d.setHoraInicio(dDto.getHoraInicio());
				d.setHoraFin(dDto.getHoraFin());
				d.setProfesional(p);

				if (dDto.getSucursalId() != null) {
					Sucursal s = new Sucursal();
					s.setIdSucursal(dDto.getSucursalId());
					d.setSucursal(s);
				}
				return d;
			}).collect(Collectors.toList());
			p.setDisponibilidades(disponibilidades);
		}

		return p;
	}

	public static void updateEntity(Profesional p, ProfesionalDTO dto) {
		if (dto == null || p == null)
			return;

		p.setDni(dto.getDni());
		p.setNombre(dto.getNombre());
		p.setApellido(dto.getApellido());
		p.setFechaNacimiento(dto.getFechaNacimiento());
		p.setGenero(dto.getGenero());
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			p.setPassword(dto.getPassword());
		}
		p.setMatricula(dto.getMatricula());

		if (dto.getRolId() != null) {
			Rol rol = new Rol();
			rol.setIdRol(dto.getRolId());
			p.setRol(rol);
		}

		if (dto.getEspecialidadId() != null) {
			Especialidad esp = new Especialidad();
			esp.setIdEspecialidad(dto.getEspecialidadId());
			p.setEspecialidad(esp);
		}

		if (p.getContacto() != null) {
			p.getContacto().setEmail(dto.getContactoEmail());
			p.getContacto().setTelefono(dto.getContactoTelefono());
		} else {

			Contacto nuevo = new Contacto();
			nuevo.setEmail(dto.getContactoEmail());
			nuevo.setTelefono(dto.getContactoTelefono());
			p.setContacto(nuevo);
		}

		if (dto.getSucursalIds() != null) {
			Set<Sucursal> sucursales = dto.getSucursalIds().stream().map(id -> {
				Sucursal s = new Sucursal();
				s.setIdSucursal(id);
				return s;
			}).collect(Collectors.toSet());
			p.setSucursales(sucursales);
		}

		if (dto.getDisponibilidades() != null) {
			Map<Long, Disponibilidad> actualesPorId = p.getDisponibilidades().stream()
					.collect(Collectors.toMap(Disponibilidad::getIdDisponibilidad, d -> d));

			List<Disponibilidad> nuevas = new ArrayList<>();

			for (DisponibilidadDTO dDto : dto.getDisponibilidades()) {
				Disponibilidad d;

				if (dDto.getIdDisponibilidad() != null && actualesPorId.containsKey(dDto.getIdDisponibilidad())) {
					d = actualesPorId.get(dDto.getIdDisponibilidad());
				} else {
					d = new Disponibilidad();
					d.setProfesional(p);
				}

				d.setFecha(dDto.getFecha());
				d.setDiaSemana(dDto.getDiaSemana());
				d.setHoraInicio(dDto.getHoraInicio());
				d.setHoraFin(dDto.getHoraFin());

				if (dDto.getSucursalId() != null) {
					Sucursal s = new Sucursal();
					s.setIdSucursal(dDto.getSucursalId());
					d.setSucursal(s);
				}

				nuevas.add(d);
			}

			p.getDisponibilidades().clear();
			p.getDisponibilidades().addAll(nuevas);
		}
	}

	// ---------- Rol ----------
	public static RolDTO toDto(Rol r) {
		if (r == null)
			return null;
		RolDTO dto = new RolDTO();
		dto.setIdRol(r.getIdRol());
		dto.setNombre(r.getNombre());
		dto.setUsuarioIds(r.getPersonas().stream().map(Usuario::getIdUsuario).collect(Collectors.toList()));
		return dto;
	}

	public static Rol toEntity(RolDTO dto) {
		if (dto == null)
			return null;
		Rol r = new Rol();
		r.setIdRol(dto.getIdRol());
		r.setNombre(dto.getNombre());
		return r;
	}

	// ---------- Sucursal ----------
	public static SucursalDTO toDto(Sucursal s) {
		if (s == null)
			return null;
		SucursalDTO dto = new SucursalDTO();
		dto.setIdSucursal(s.getIdSucursal());
		dto.setNombre(s.getNombre());
		dto.setDireccion(s.getDireccion());
		dto.setEspecialidadIds(s.getEspecialidades() != null
				? s.getEspecialidades().stream().map(Especialidad::getIdEspecialidad).collect(Collectors.toSet())
				: new HashSet<>());
		dto.setProfesionalIds(s.getProfesionales() != null
				? s.getProfesionales().stream().map(Profesional::getIdUsuario).collect(Collectors.toSet())
				: new HashSet<>());
		dto.setDisponibilidadIds(s.getDisponibilidades() != null
				? s.getDisponibilidades().stream().map(Disponibilidad::getIdDisponibilidad).collect(Collectors.toList())
				: new ArrayList<>());
		return dto;
	}

	public static Sucursal toEntity(SucursalDTO dto) {
		if (dto == null)
			return null;
		Sucursal s = new Sucursal();
		s.setIdSucursal(dto.getIdSucursal());
		s.setNombre(dto.getNombre());
		s.setDireccion(dto.getDireccion());
		if (dto.getEspecialidadIds() != null) {
			Set<Especialidad> especialidades = dto.getEspecialidadIds().stream().map(id -> {
				Especialidad esp = new Especialidad();
				esp.setIdEspecialidad(id);
				return esp;
			}).collect(Collectors.toSet());
			s.setEspecialidades(especialidades);
		} else {
			s.setEspecialidades(new HashSet<>());
		}
		return s;
	}

	// ---------- Turno ----------
	public static TurnoDTO toDto(Turno t) {
		if (t == null)
			return null;
		TurnoDTO dto = new TurnoDTO();
		dto.setIdTurno(t.getIdTurno());
		dto.setHora(t.getHora());
		dto.setPacienteId(t.getPaciente().getIdUsuario());
		dto.setProfesionalId(t.getProfesional().getIdUsuario());
		dto.setEstadoId(t.getEstado().getIdEstado());
		dto.setAgendaId(t.getAgenda().getIdAgenda());
		return dto;
	}

	public static Turno toEntity(TurnoDTO dto) {
		if (dto == null)
			return null;
		Turno t = new Turno();
		t.setIdTurno(dto.getIdTurno());
		t.setHora(dto.getHora());

		Paciente p = new Paciente();
		p.setIdUsuario(dto.getPacienteId());
		t.setPaciente(p);

		Profesional prof = new Profesional();
		prof.setIdUsuario(dto.getProfesionalId());
		t.setProfesional(prof);

		EstadoTurno et = new EstadoTurno();
		et.setIdEstado(dto.getEstadoId());
		t.setEstado(et);

		Agenda a = new Agenda();
		a.setIdAgenda(dto.getAgendaId());
		t.setAgenda(a);

		return t;
	}

	// ---------- Empleado ----------
	public static EmpleadoDTO toDto(Empleado e) {
		if (e == null)
			return null;
		EmpleadoDTO dto = new EmpleadoDTO();
		UsuarioDTO base = toDto((Usuario) e);
		dto.setIdUsuario(base.getIdUsuario());
		dto.setDni(base.getDni());
		dto.setNombre(base.getNombre());
		dto.setApellido(base.getApellido());
		dto.setFechaNacimiento(base.getFechaNacimiento());
		dto.setGenero(base.getGenero());
		dto.setContactoId(base.getContactoId());
		dto.setRolId(base.getRolId());
		dto.setPassword(base.getPassword());
		dto.setActivo(e.getActivo());
		return dto;
	}

	public static Empleado toEntity(EmpleadoDTO dto) {
		if (dto == null)
			return null;
		Empleado e = new Empleado();
		Usuario u = toEntity(toUsuarioDTO(dto));
		e.setIdUsuario(u.getIdUsuario());
		e.setDni(u.getDni());
		e.setNombre(u.getNombre());
		e.setApellido(u.getApellido());
		e.setFechaNacimiento(u.getFechaNacimiento());
		e.setGenero(u.getGenero());
		e.setContacto(u.getContacto());
		e.setRol(u.getRol());
		e.setPassword(u.getPassword());
		e.setActivo(dto.getActivo());
		return e;
	}
}
