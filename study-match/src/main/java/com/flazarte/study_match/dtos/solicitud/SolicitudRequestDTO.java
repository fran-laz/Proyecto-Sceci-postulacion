package com.flazarte.study_match.dtos.solicitud;

import lombok.Data;

@Data
public class SolicitudRequestDTO {
    private Long postulanteId;
    private Long proyectoId;
    private Long grupoEstudioId;

    private String mensajePostulacion;
}