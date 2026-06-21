package com.flazarte.study_match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "grupos_estudio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrupoEstudio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "maximo_integrantes")
    private Integer maximoIntegrantes;

    @Column(length = 50)
    private String modalidad;

    @Column(name = "horario_habitual", length = 100)
    private String horarioHabitual;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private Perfil creador;

    @ManyToOne
    @JoinColumn(name = "grupo_materia_id", nullable = false)
    private GrupoMateria grupoMateria;

    @ManyToMany
    @JoinTable(
            name = "grupo_estudio_integrantes",
            joinColumns = @JoinColumn(name = "grupo_estudio_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<Perfil> integrantes;
}