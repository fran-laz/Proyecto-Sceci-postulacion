package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioRequestDTO;
import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioResponseDTO;
import com.flazarte.study_match.services.GrupoEstudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupos-estudio")
public class GrupoEstudioController {

    @Autowired
    private GrupoEstudioService grupoEstudioService;

    @PostMapping
    public ResponseEntity<GrupoEstudioResponseDTO> crearGrupo(@RequestBody GrupoEstudioRequestDTO dto) {
        GrupoEstudioResponseDTO nuevoGrupo = grupoEstudioService.crearGrupoEstudio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGrupo);
    }

    @GetMapping
    public ResponseEntity<List<GrupoEstudioResponseDTO>> listarGrupos() {
        List<GrupoEstudioResponseDTO> grupos = grupoEstudioService.obtenerTodosLosGrupos();
        return ResponseEntity.ok(grupos);
    }
}