package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.exceptions.CustomException;
import com.Gropo06.turnos_medicos.model.Disponibilidad;
import com.Gropo06.turnos_medicos.model.Especialidad;
import com.Gropo06.turnos_medicos.model.Profesional;
import com.Gropo06.turnos_medicos.model.Sucursal;
import com.Gropo06.turnos_medicos.repository.DisponibilidadRepository;
import com.Gropo06.turnos_medicos.repository.EspecialidadRepository;
import com.Gropo06.turnos_medicos.repository.ProfesionalRepository;
import com.Gropo06.turnos_medicos.repository.SucursalRepository;
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
    private final ProfesionalRepository profesionalRepo;
    private final EspecialidadRepository especialidadRepo;
    private final SucursalRepository sucursalRepo;  // ◀— inyectamos también SucursalRepository

    public PacienteController(DisponibilidadRepository dispRepo,
                              ProfesionalRepository profesionalRepo,
                              EspecialidadRepository especialidadRepo,
                              SucursalRepository sucursalRepo) {
        this.dispRepo = dispRepo;
        this.profesionalRepo = profesionalRepo;
        this.especialidadRepo = especialidadRepo;
        this.sucursalRepo = sucursalRepo;
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ResultadoParaFront>> buscarTurnosDisponibles(
            @RequestParam("idEspecialidad") Long idEsp,
            @RequestParam("idSucursal")     Long idSuc,
            @RequestParam("desde") 
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") 
              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        // 1) Recuperar la entidad Especialidad
        Especialidad esp = especialidadRepo.findById(idEsp)
                .orElseThrow(() -> new CustomException("Especialidad no válida: " + idEsp));

        // 2) Recuperar la entidad Sucursal (¡ojo! no pasar simplemente el Long)
        Sucursal suc = sucursalRepo.findById(idSuc)
                .orElseThrow(() -> new CustomException("Sucursal no válida: " + idSuc));

        // 3) Invocar al repositorio PASANDO la ENTIDAD Sucursal (no el Long)
        List<Disponibilidad> listaDisp = dispRepo
            .findByProfesional_EspecialidadAndSucursalAndFechaBetween(
                esp,
                suc,
                desde,
                hasta
            );

        // 4) Agrupar por (profesionalId + fecha)
        Map<String, List<Disponibilidad>> agrupado = new HashMap<>();
        for (Disponibilidad d : listaDisp) {
            String key = d.getProfesional().getIdUsuario() + "|" + d.getFecha().toString();
            agrupado.computeIfAbsent(key, k -> new ArrayList<>()).add(d);
        }

        // 5) Para cada grupo (profesional + día) generar las franjas según la duración
        List<ResultadoParaFront> resultado = new ArrayList<>();
        for (Map.Entry<String, List<Disponibilidad>> entry : agrupado.entrySet()) {
            List<Disponibilidad> dList = entry.getValue();
            Profesional prof = dList.get(0).getProfesional();
            LocalDate dia    = dList.get(0).getFecha();

            // Asumo que, para un día dado, el profesional tiene UNA disponibilidad única con horaInicio / horaFin.
            // (Si hubiera más de una fila, habría que hacer merge; aquí uso la primera.)
            Disponibilidad dispUnica = dList.get(0);
            LocalTime inicio = dispUnica.getHoraInicio();
            LocalTime fin    = dispUnica.getHoraFin();
            int durMin = esp.getDuracion(); // p.ej. 30 min

            List<String> franjas = new ArrayList<>();
            LocalTime cursor = inicio;
            while (!cursor.plusMinutes(durMin).isAfter(fin)) {
                LocalTime tope = cursor.plusMinutes(durMin);
                franjas.add(cursor.toString() + "-" + tope.toString());
                cursor = tope;
            }

            resultado.add(new ResultadoParaFront(
                new ProfesionalDTO(prof.getIdUsuario(), prof.getNombre(), prof.getApellido()),
                dia.toString(),
                franjas
            ));
        }

        return ResponseEntity.ok(resultado);
    }

    // ────────────────────────────────────────────────────────────────────────
    // DTO ligero para enviar al front:
    public static class ResultadoParaFront {
        private ProfesionalDTO profesional;
        private String dia;
        private List<String> franjas;

        public ResultadoParaFront(ProfesionalDTO profesional, String dia, List<String> franjas) {
            this.profesional = profesional;
            this.dia = dia;
            this.franjas = franjas;
        }
        public ProfesionalDTO getProfesional() { return profesional; }
        public String getDia()              { return dia; }
        public List<String> getFranjas()    { return franjas; }
    }

    public static class ProfesionalDTO {
        private Long idUsuario;
        private String nombre;
        private String apellido;

        public ProfesionalDTO(Long idUsuario, String nombre, String apellido) {
            this.idUsuario  = idUsuario;
            this.nombre     = nombre;
            this.apellido   = apellido;
        }
        public Long getIdUsuario()    { return idUsuario; }
        public String getNombre()     { return nombre; }
        public String getApellido()   { return apellido; }
    }
}
