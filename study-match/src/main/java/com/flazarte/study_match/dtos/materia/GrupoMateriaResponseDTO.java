package com.flazarte.study_match.dtos.materia;

import lombok.Data;

@Data
public class GrupoMateriaResponseDTO {
    private Long id;
    private String numeroGrupo;
    private String nombreDocente;
    private Long materiaId;
}