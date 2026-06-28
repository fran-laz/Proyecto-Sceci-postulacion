package com.flazarte.study_match.dtos.solicitud;

import lombok.Data;

@Data
public class SolicitudResponseDTO {
    private Long id;
    private String nombrePostulante;
    private String carreraPostulante;
    private String mensajePostulacion;
    private String estado;
    private String tipoPublicacion;
    private String tituloPublicacion;
}