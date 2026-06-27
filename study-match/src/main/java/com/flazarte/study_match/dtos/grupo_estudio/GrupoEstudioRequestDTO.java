package com.flazarte.study_match.dtos.grupo_estudio;

import lombok.Data;

@Data
public class GrupoEstudioRequestDTO {
    private String titulo;
    private String descripcion;
    private Integer maximoIntegrantes;
    private String modalidad;
    private String horarioHabitual;
    private Long creadorId;
    private Long grupoMateriaId;
}