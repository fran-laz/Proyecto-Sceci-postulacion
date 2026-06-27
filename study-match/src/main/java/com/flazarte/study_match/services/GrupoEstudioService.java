package com.flazarte.study_match.services;

import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioRequestDTO;
import com.flazarte.study_match.dtos.grupo_estudio.GrupoEstudioResponseDTO;
import com.flazarte.study_match.models.GrupoEstudio;
import com.flazarte.study_match.models.GrupoMateria;
import com.flazarte.study_match.models.Perfil;
import com.flazarte.study_match.repositories.GrupoEstudioRepository;
import com.flazarte.study_match.repositories.GrupoMateriaRepository;
import com.flazarte.study_match.repositories.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrupoEstudioService {

    @Autowired
    private GrupoEstudioRepository grupoEstudioRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private GrupoMateriaRepository grupoMateriaRepository;


    public GrupoEstudioResponseDTO crearGrupoEstudio(GrupoEstudioRequestDTO dto) {
        Perfil creador = perfilRepository.findById(dto.getCreadorId())
                .orElseThrow(() -> new RuntimeException("Error: Perfil del creador no encontrado"));

        GrupoMateria grupoMateria = grupoMateriaRepository.findById(dto.getGrupoMateriaId())
                .orElseThrow(() -> new RuntimeException("Error: Paralelo/Materia no encontrado"));

        GrupoEstudio grupo = new GrupoEstudio();
        grupo.setTitulo(dto.getTitulo());
        grupo.setDescripcion(dto.getDescripcion());
        grupo.setMaximoIntegrantes(dto.getMaximoIntegrantes());
        grupo.setModalidad(dto.getModalidad());
        grupo.setHorarioHabitual(dto.getHorarioHabitual());
        grupo.setCreador(creador);
        grupo.setGrupoMateria(grupoMateria);

        GrupoEstudio grupoGuardado = grupoEstudioRepository.save(grupo);

        return convertirADTO(grupoGuardado);
    }

    public List<GrupoEstudioResponseDTO> obtenerTodosLosGrupos() {
        return grupoEstudioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private GrupoEstudioResponseDTO convertirADTO(GrupoEstudio grupo) {
        GrupoEstudioResponseDTO dto = new GrupoEstudioResponseDTO();
        dto.setId(grupo.getId());
        dto.setTitulo(grupo.getTitulo());
        dto.setDescripcion(grupo.getDescripcion());
        dto.setMaximoIntegrantes(grupo.getMaximoIntegrantes());
        dto.setModalidad(grupo.getModalidad());
        dto.setHorarioHabitual(grupo.getHorarioHabitual());

        dto.setNombreCreador(grupo.getCreador().getNombres() + " " + grupo.getCreador().getApellidos());
        dto.setNombreMateria(grupo.getGrupoMateria().getMateria().getNombre());
        dto.setNumeroGrupo(grupo.getGrupoMateria().getNumeroGrupo());

        return dto;
    }
}