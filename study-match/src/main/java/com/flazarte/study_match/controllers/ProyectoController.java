package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.proyecto.ProyectoRequestDTO;
import com.flazarte.study_match.dtos.proyecto.ProyectoResponseDTO;
import com.flazarte.study_match.services.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProyectoResponseDTO> crearProyecto(@RequestBody ProyectoRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(proyectoService.crearProyecto(dto, email));
    }

    @GetMapping("/mis-proyectos")
    public ResponseEntity<List<ProyectoResponseDTO>> obtenerMisProyectos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(proyectoService.obtenerProyectosPorEstudiante(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }

    @GetMapping("/materia/{grupoMateriaId}")
    public ResponseEntity<List<ProyectoResponseDTO>> disponibles(@PathVariable Long grupoMateriaId) {
        return ResponseEntity.ok(proyectoService.obtenerDisponiblesPorMateria(grupoMateriaId));
    }
    @GetMapping("/{id}/integrantes")
    public ResponseEntity<List<String>> verIntegrantes(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerIntegrantes(id));
    }
}
