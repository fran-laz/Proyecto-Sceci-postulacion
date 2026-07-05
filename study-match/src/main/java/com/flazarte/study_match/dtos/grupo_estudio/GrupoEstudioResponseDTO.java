package com.flazarte.study_match.dtos.grupo_estudio;

import lombok.Data;

@Data
public class GrupoEstudioResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer maximoIntegrantes;
    private String modalidad;
    private String horarioHabitual;
    private String nombreCreador;
    private String nombreMateria;
    private String numeroGrupo;
    private String nombreDocente;
    private String emailCreador;
}