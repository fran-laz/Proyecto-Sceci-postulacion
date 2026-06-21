package com.flazarte.study_match.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "proyectos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "maximo_integrantes")
    private Integer maximoIntegrantes;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    @ManyToOne
    @JoinColumn(name = "creador_id", nullable = false)
    private Perfil creador;

    @ManyToOne
    @JoinColumn(name = "grupo_materia_id", nullable = false)
    private GrupoMateria grupoMateria;

    @ManyToMany
    @JoinTable(
            name = "proyecto_integrantes",
            joinColumns = @JoinColumn(name = "proyecto_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private List<Perfil> integrantes;
}