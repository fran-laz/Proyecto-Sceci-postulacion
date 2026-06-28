package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.solicitud.SolicitudRequestDTO;
import com.flazarte.study_match.dtos.solicitud.SolicitudResponseDTO;
import com.flazarte.study_match.services.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> enviarSolicitud(@RequestBody SolicitudRequestDTO dto) {
        SolicitudResponseDTO nuevaSolicitud = solicitudService.crearSolicitud(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSolicitud);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<SolicitudResponseDTO> responderSolicitud(
            @PathVariable Long id,
            @RequestParam String respuesta) {

        SolicitudResponseDTO solicitudActualizada = solicitudService.responderSolicitud(id, respuesta);
        return ResponseEntity.ok(solicitudActualizada);
    }
}