package com.flazarte.study_match.dtos.proyecto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Integer maximoIntegrantes;
    private LocalDate fechaLimite;
    private String nombreCreador;
    private String nombreMateria;
    private String numeroGrupo;
}