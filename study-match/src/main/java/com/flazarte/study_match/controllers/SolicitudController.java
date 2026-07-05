package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.solicitud.SolicitudRequestDTO;
import com.flazarte.study_match.dtos.solicitud.SolicitudResponseDTO;
import com.flazarte.study_match.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> enviarSolicitud(@RequestBody SolicitudRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        SolicitudResponseDTO nuevaSolicitud = solicitudService.crearSolicitud(dto, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }

    @GetMapping("/mis-pendientes")
    public ResponseEntity<List<SolicitudResponseDTO>> obtenerMisPendientes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(solicitudService.obtenerSolicitudesPendientesPorCreador(email));
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<SolicitudResponseDTO> responderSolicitud(
            @PathVariable Long id,
            @RequestParam String accion) {

        String estado = accion.equalsIgnoreCase("aceptar") ? "ACEPTADA" : "RECHAZADA";
        SolicitudResponseDTO solicitudActualizada = solicitudService.responderSolicitud(id, estado);
        return ResponseEntity.ok(solicitudActualizada);
    }
}