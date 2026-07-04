package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.proyecto.ProyectoRequestDTO;
import com.flazarte.study_match.dtos.proyecto.ProyectoResponseDTO;
import com.flazarte.study_match.services.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailEstudianteLogueado = authentication.getName();

        try {
            ProyectoResponseDTO proyectoCreado = proyectoService.crearProyecto(dto, emailEstudianteLogueado);
            return ResponseEntity.status(HttpStatus.CREATED).body(proyectoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/mis-proyectos")
    public ResponseEntity<List<ProyectoResponseDTO>> misProyectos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogueado = authentication.getName();

        List<ProyectoResponseDTO> misProyectos = proyectoService.obtenerProyectosPorEstudiante(emailLogueado);
        return ResponseEntity.ok(misProyectos);
    }
}