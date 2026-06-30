package com.flazarte.study_match.dtos.materia;

import lombok.Data;

@Data
public class MateriaResponseDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private Integer semestre;
    private String carrerasHabilitadas;
}