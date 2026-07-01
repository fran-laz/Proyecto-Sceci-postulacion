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

@RestController
@RequestMapping("/api/proyectos")
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
}