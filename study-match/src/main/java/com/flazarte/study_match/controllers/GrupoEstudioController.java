package com.flazarte.study_match.controllers;

import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioRequestDTO;
import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioResponseDTO;
import com.flazarte.study_match.services.GrupoEstudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/grupos-estudio")
@CrossOrigin(origins = "*")
public class GrupoEstudioController {

    @Autowired
    private GrupoEstudioService grupoEstudioService;

    @PostMapping
    public ResponseEntity<?> crearGrupo(@RequestBody GrupoEstudioRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailEstudianteLogueado = authentication.getName();

        try {
            GrupoEstudioResponseDTO nuevoGrupo = grupoEstudioService.crearGrupoEstudio(dto, emailEstudianteLogueado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGrupo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<GrupoEstudioResponseDTO>> listarGrupos() {
        List<GrupoEstudioResponseDTO> grupos = grupoEstudioService.obtenerTodosLosGrupos();
        return ResponseEntity.ok(grupos);
    }
    @GetMapping("/mis-grupos")
    public ResponseEntity<List<GrupoEstudioResponseDTO>> misGrupos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogueado = authentication.getName();

        List<GrupoEstudioResponseDTO> misGrupos = grupoEstudioService.obtenerGruposPorEstudiante(emailLogueado);
        return ResponseEntity.ok(misGrupos);
    }
}