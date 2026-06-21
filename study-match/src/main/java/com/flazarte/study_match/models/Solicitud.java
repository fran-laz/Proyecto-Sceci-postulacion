package com.flazarte.study_match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitudes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postulante_id", nullable = false)
    private Perfil postulante;

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "grupo_estudio_id")
    private GrupoEstudio grupoEstudio;

    @Column(name = "mensaje_postulacion", columnDefinition = "TEXT")
    private String mensajePostulacion;

    @Column(nullable = false, length = 20)
    private String estado; // PENDIENTE, ACEPTADA, RECHAZADA
}