package com.flazarte.study_match.dtos.proyecto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoRequestDTO {
    private String titulo;
    private String descripcion;
    private Integer maximoIntegrantes;
    private LocalDate fechaLimite;
    private Long creadorId;
    private Long grupoMateriaId;
}