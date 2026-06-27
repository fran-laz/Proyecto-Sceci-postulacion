package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.proyecto.ProyectoRequestDTO;
import com.flazarte.study_match.dtos.proyecto.ProyectoResponseDTO;
import com.flazarte.study_match.services.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(@RequestBody ProyectoRequestDTO dto) {
        ProyectoResponseDTO nuevoProyecto = proyectoService.crearProyecto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProyecto);
    }
    @GetMapping
    public ResponseEntity<List<ProyectoResponseDTO>> listarProyectos() {
        List<ProyectoResponseDTO> proyectos = proyectoService.obtenerTodosLosProyectos();
        return ResponseEntity.ok(proyectos);
    }
}