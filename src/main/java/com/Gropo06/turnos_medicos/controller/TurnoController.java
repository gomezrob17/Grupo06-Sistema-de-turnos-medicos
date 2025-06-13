package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.FiltroTurnoDTO;
import com.Gropo06.turnos_medicos.dto.SlotDTO;
import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.*;
import com.Gropo06.turnos_medicos.repository.*;
import com.Gropo06.turnos_medicos.service.EmailService;
import com.Gropo06.turnos_medicos.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping
@Tag(name = "Turnos", description = "API para gestionar turnos médicos")
public class TurnoController {

    private final EspecialidadRepository espRepo;
    private final SucursalRepository sucRepo;
    private final PacienteRepository pacienteRepo;
    private final DisponibilidadRepository dispRepo;
    private final TurnoRepository turnoRepo;
    private final AgendaRepository agendaRepo;
    private final ProfesionalRepository profRepo;
    private final EstadoTurnoRepository estadoRepo;

    @Autowired private EmailService emailService;
    @Autowired private TurnoService turnoService;

    public TurnoController(EspecialidadRepository espRepo,
                           SucursalRepository sucRepo,
                           PacienteRepository pacienteRepo,
                           DisponibilidadRepository dispRepo,
                           TurnoRepository turnoRepo,
                           AgendaRepository agendaRepo,
                           ProfesionalRepository profRepo,
                           EstadoTurnoRepository estadoRepo) {
        this.espRepo = espRepo;
        this.sucRepo = sucRepo;
        this.pacienteRepo = pacienteRepo;
        this.dispRepo = dispRepo;
        this.turnoRepo = turnoRepo;
        this.agendaRepo = agendaRepo;
        this.profRepo = profRepo;
        this.estadoRepo = estadoRepo;
    }

    public static class SucursalDTO {
        private Long id;
        private String nombre;

        public SucursalDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }
    }

    @Operation(
        summary = "Buscar slots disponibles",
        description = "Devuelve una lista de intervalos disponibles para turnos según los filtros enviados",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de turnos disponibles",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = SlotDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos", content = @Content)
        }
    )
    @GetMapping("/api/turnos/slots")
    @ResponseBody
    public ResponseEntity<List<SlotDTO>> getSlotsPorFiltro(
            @Parameter(description = "ID de la especialidad") @RequestParam("idEspecialidad") Long idEsp,
            @Parameter(description = "ID de la sucursal") @RequestParam("idSucursal") Long idSuc,
            @Parameter(description = "Fecha desde (yyyy-MM-dd)") @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha hasta (yyyy-MM-dd)") @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        Especialidad espElegida = espRepo.findById(idEsp)
                .orElseThrow(() -> new CustomException("Especialidad inválida Id: " + idEsp));
        Sucursal sucElegida = sucRepo.findById(idSuc)
                .orElseThrow(() -> new CustomException("Sucursal inválida Id: " + idSuc));

        List<Disponibilidad> listaDisp = dispRepo
                .findByProfesional_EspecialidadAndSucursalAndFechaBetween(
                        espElegida, sucElegida, desde, hasta
                );

        int duracionMinutos = espElegida.getDuracion();
        List<SlotDTO> resultado = new ArrayList<>();

        for (Disponibilidad disp : listaDisp) {
            Profesional prof = disp.getProfesional();
            LocalDate fechaDispon = disp.getFecha();
            LocalTime inicio = disp.getHoraInicio();
            LocalTime fin = disp.getHoraFin();

            Optional<Agenda> opAg = agendaRepo
                    .findByDiaAndSucursalAndTipoEspecialidad(fechaDispon, sucElegida, espElegida);

            Set<LocalTime> horasOcupadas = new HashSet<>();
            if (opAg.isPresent()) {
                Agenda agenda = opAg.get();
                for (Turno t : agenda.getTurnos()) {
                    if (t.getProfesional().getIdUsuario().equals(prof.getIdUsuario())) {
                        horasOcupadas.add(t.getHora());
                    }
                }
            }

            LocalTime cursor = inicio;
            while (!cursor.plusMinutes(duracionMinutos).isAfter(fin)) {
                LocalTime tope = cursor.plusMinutes(duracionMinutos);
                if (!horasOcupadas.contains(cursor)) {
                    SlotDTO slot = new SlotDTO(
                            prof.getIdUsuario(),
                            espElegida.getIdEspecialidad(),
                            sucElegida.getIdSucursal(),
                            fechaDispon,
                            cursor,
                            tope
                    );
                    resultado.add(slot);
                }
                cursor = tope;
            }
        }

        return ResponseEntity.ok(resultado);
    }

    //========================= VISTA: GET /turnos =========================
    @Operation(hidden = true)
    @GetMapping("/turnos")
    public String mostrarFiltroInicial(Model model, Principal principal) {
        List<Especialidad> especialidades = espRepo.findAll();
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("sucursales", List.of());
        model.addAttribute("filtro", new FiltroTurnoDTO());
        model.addAttribute("disponibilidadesPorProfesional", null);
        return "paciente/turnos";
    }

    //========================= VISTA: POST /turnos/buscar =========================
    @Operation(hidden = true)
    @PostMapping("/turnos/buscar")
    public String buscarDisponibilidades(@ModelAttribute("filtro") FiltroTurnoDTO filtro, Model model) {
        List<Especialidad> especialidades = espRepo.findAll();
        model.addAttribute("especialidades", especialidades);

        if (filtro.getIdEspecialidad() != null) {
            List<Sucursal> sucursalesQueAtienden = sucRepo.findDistinctByProfesionalesEspecialidadId(filtro.getIdEspecialidad());
            model.addAttribute("sucursales", sucursalesQueAtienden);
        } else {
            model.addAttribute("sucursales", List.of());
        }

        if (filtro.getIdEspecialidad() == null ||
            filtro.getIdSucursal() == null ||
            filtro.getFechaDesde() == null ||
            filtro.getFechaHasta() == null) {
            model.addAttribute("disponibilidadesPorProfesional", null);
            return "paciente/turnos";
        }

        Especialidad espElegida = espRepo.findById(filtro.getIdEspecialidad())
                .orElseThrow(() -> new CustomException("Especialidad inválida"));
        Sucursal sucElegida = sucRepo.findById(filtro.getIdSucursal())
                .orElseThrow(() -> new CustomException("Sucursal inválida"));

        List<Disponibilidad> listaDisp = dispRepo
                .findByProfesional_EspecialidadAndSucursalAndFechaBetween(
                        espElegida, sucElegida, filtro.getFechaDesde(), filtro.getFechaHasta()
                );

        int duracionMinutos = espElegida.getDuracion();
        Map<Profesional, List<SlotDTO>> mapSlots = new LinkedHashMap<>();

        for (Disponibilidad disp : listaDisp) {
            Profesional prof = disp.getProfesional();
            LocalDate fechaDispon = disp.getFecha();
            LocalTime inicio = disp.getHoraInicio();
            LocalTime fin = disp.getHoraFin();

            Optional<Agenda> opAg = agendaRepo.findByDiaAndSucursalAndTipoEspecialidad(
                    fechaDispon, sucElegida, espElegida
            );

            Set<LocalTime> horasOcupadas = new HashSet<>();
            if (opAg.isPresent()) {
                for (Turno t : opAg.get().getTurnos()) {
                    if (t.getProfesional().getIdUsuario().equals(prof.getIdUsuario())) {
                        horasOcupadas.add(t.getHora());
                    }
                }
            }

            LocalTime cursor = inicio;
            List<SlotDTO> listaSlots = new ArrayList<>();
            while (!cursor.plusMinutes(duracionMinutos).isAfter(fin)) {
                LocalTime tope = cursor.plusMinutes(duracionMinutos);
                if (!horasOcupadas.contains(cursor)) {
                    SlotDTO slot = new SlotDTO(
                            prof.getIdUsuario(),
                            espElegida.getIdEspecialidad(),
                            sucElegida.getIdSucursal(),
                            fechaDispon,
                            cursor,
                            tope
                    );
                    listaSlots.add(slot);
                }
                cursor = tope;
            }

            if (!listaSlots.isEmpty()) {
                mapSlots.put(prof, listaSlots);
            }
        }

        model.addAttribute("disponibilidadesPorProfesional", mapSlots);
        return "paciente/turnos";
    }
}