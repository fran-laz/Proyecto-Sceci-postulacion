package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.materia.GrupoMateriaResponseDTO;
import com.flazarte.study_match.dtos.materia.MateriaResponseDTO;
import com.flazarte.study_match.services.MateriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;


    @GetMapping
    public ResponseEntity<List<MateriaResponseDTO>> listarMaterias() {
        List<MateriaResponseDTO> materias = materiaService.obtenerTodasLasMaterias();
        return ResponseEntity.ok(materias); // Retorna un estado HTTP 200 OK con la lista
    }

    @GetMapping("/{id}/grupos")
    public ResponseEntity<List<GrupoMateriaResponseDTO>> listarGruposPorMateria(@PathVariable Long id) {
        List<GrupoMateriaResponseDTO> grupos = materiaService.obtenerGruposPorMateria(id);
        return ResponseEntity.ok(grupos);
    }
}