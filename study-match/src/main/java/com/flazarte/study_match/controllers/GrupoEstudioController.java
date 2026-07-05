package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioRequestDTO;
import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioResponseDTO;
import com.flazarte.study_match.services.GrupoEstudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos-estudio")
@CrossOrigin(origins = "*")
public class GrupoEstudioController {

    @Autowired
    private GrupoEstudioService grupoEstudioService;

    @PostMapping
    public ResponseEntity<GrupoEstudioResponseDTO> crearGrupo(@RequestBody GrupoEstudioRequestDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(grupoEstudioService.crearGrupoEstudio(dto, email));
    }

    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoEstudioResponseDTO>> obtenerMisGrupos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(grupoEstudioService.obtenerGruposPorEstudiante(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        grupoEstudioService.eliminarGrupo(id);
        return ResponseEntity.ok("Eliminado correctamente");
    }

    @GetMapping("/materia/{grupoMateriaId}")
    public ResponseEntity<List<GrupoEstudioResponseDTO>> disponibles(@PathVariable Long grupoMateriaId) {
        return ResponseEntity.ok(grupoEstudioService.obtenerDisponiblesPorMateria(grupoMateriaId));
    }
    @GetMapping("/{id}/integrantes")
    public ResponseEntity<List<String>> verIntegrantes(@PathVariable Long id) {
        return ResponseEntity.ok(grupoEstudioService.obtenerIntegrantes(id));
    }
}