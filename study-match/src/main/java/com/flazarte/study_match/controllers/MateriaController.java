package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.materia.GrupoMateriaResponseDTO;
import com.flazarte.study_match.dtos.materia.MateriaResponseDTO;
import com.flazarte.study_match.services.MateriaService; // ¡No olvides este import!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    @Autowired
    private MateriaService materiaService;

    @GetMapping
    public ResponseEntity<List<MateriaResponseDTO>> listarMaterias(@RequestParam Long carreraId) {
        List<MateriaResponseDTO> respuesta = materiaService.obtenerMateriasPorCarrera(carreraId);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}/grupos")
    public ResponseEntity<List<GrupoMateriaResponseDTO>> listarGruposPorMateria(
            @PathVariable Long id,
            @RequestParam Long carreraId) {
        List<GrupoMateriaResponseDTO> grupos = materiaService.obtenerGruposPorMateria(id, carreraId);
        return ResponseEntity.ok(grupos);
    }
}