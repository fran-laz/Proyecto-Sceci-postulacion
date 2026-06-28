package com.flazarte.study_match.services;

import com.flazarte.study_match.dtos.materia.GrupoMateriaResponseDTO;
import com.flazarte.study_match.dtos.materia.MateriaResponseDTO;
import com.flazarte.study_match.models.GrupoMateria;
import com.flazarte.study_match.models.Materia;
import com.flazarte.study_match.repositories.GrupoMateriaRepository;
import com.flazarte.study_match.repositories.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    @Autowired
    private GrupoMateriaRepository grupoMateriaRepository;

    public List<MateriaResponseDTO> obtenerTodasLasMaterias() {
        List<Materia> materias = materiaRepository.findAll();

        return materias.stream().map(materia -> {
            MateriaResponseDTO dto = new MateriaResponseDTO();
            dto.setId(materia.getId());
            dto.setNombre(materia.getNombre());
            dto.setCodigo(materia.getCodigo());
            dto.setSemestre(materia.getSemestre());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<GrupoMateriaResponseDTO> obtenerGruposPorMateria(Long materiaId, Long carreraId) {
        List<GrupoMateria> grupos = grupoMateriaRepository.findByMateriaIdAndCarrerasId(materiaId, carreraId);

        return grupos.stream().map(grupo -> {
            GrupoMateriaResponseDTO dto = new GrupoMateriaResponseDTO();
            dto.setId(grupo.getId());
            dto.setNumeroGrupo(grupo.getNumeroGrupo());
            dto.setNombreDocente(grupo.getNombreDocente());
            dto.setMateriaId(grupo.getMateria().getId());
            return dto;
        }).collect(Collectors.toList());
    }
}